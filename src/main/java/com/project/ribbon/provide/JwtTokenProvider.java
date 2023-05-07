package com.project.ribbon.provide;

import com.project.ribbon.dto.TokenInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    private static final int MAX_ATTEMPTS = 5;

    // 인증 실패 횟수 카운트를 저장할 맵
    private Map<String, Integer> loginAttempts = new HashMap<>();

    // 재시도 대기 시간을 저장할 맵
    private Map<String, Long> retryTimes = new HashMap<>();

    @EventListener
    public void handleAuthenticationFailureEvent(AbstractAuthenticationFailureEvent event) {
        String username = (String) event.getAuthentication().getPrincipal();
        int attempts = loginAttempts.getOrDefault(username, 0) + 1;
        loginAttempts.put(username, attempts);
        long retryTime = 0;

        if (attempts >= MAX_ATTEMPTS) {
            if (attempts == MAX_ATTEMPTS) {
                retryTime = Instant.now().toEpochMilli() + (15 * 60 * 1000); // 15분 대기
            } else if (attempts == MAX_ATTEMPTS * 2) {
                retryTime = Instant.now().toEpochMilli() + (30 * 60 * 1000); // 30분 대기
            } else if (attempts == MAX_ATTEMPTS * 3) {
                retryTime = Instant.now().toEpochMilli() + (60 * 60 * 1000); // 1시간 대기
            }
            retryTimes.put(username, retryTime);
        }

        // 로그인 실패 횟수가 최대 시도 횟수에 도달한 경우, 예외를 발생시킴
        if (attempts >= MAX_ATTEMPTS) {
            throw new LockedException("로그인 실패 횟수가 초과되어 " + getRetryTime(username) + " 까지 잠김 처리됩니다.");
        }
    }

    // 유저의 재시도 대기 시간을 반환하는 메서드
    public Date getRetryTime(String username) {
        long retryTime = retryTimes.getOrDefault(username, 0L);
        return new Date(retryTime);
    }

    // 유저의 로그인 실패 횟수를 초기화하는 메서드
    public void resetLoginAttempts(String username) {
        loginAttempts.remove(username);
        retryTimes.remove(username);
    }

    private final Key key;


    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public TokenInfo generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 60*60*2*1000);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 60*60*24*14*1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();


        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    // AccessToken을 갱신하는 메서드
    public TokenInfo generateAccessToken(String username, List<String> roles) {

        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + 60*60*2*1000);
        String accessToken = Jwts.builder()
                .setSubject(username)
                .claim("auth", String.join(",", roles))
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .build();
    }



    // JWT 토큰에서 클레임 정보 추출
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new RuntimeException("토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new RuntimeException("지원되지 않는 JWT 토큰입니다.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new RuntimeException("유효하지 않은 JWT 토큰입니다.");
        } catch (io.jsonwebtoken.security.SecurityException | IllegalArgumentException e) {
            log.info("Invalid JWT Token", e);
            throw new RuntimeException("유효하지 않은 JWT 토큰입니다.");
        }
    }


    // Access Token에서 권한 정보 추출
    public List<String> getRoles(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            String roles = claims.get("auth", String.class);
            return Arrays.asList(roles.split(","));
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new RuntimeException("토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new RuntimeException("지원되지 않는 JWT 토큰입니다.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new RuntimeException("유효하지 않은 JWT 토큰입니다.");
        } catch (io.jsonwebtoken.security.SecurityException | IllegalArgumentException e) {
            log.info("Invalid JWT Token", e);
            throw new RuntimeException("유효하지 않은 JWT 토큰입니다.");
        }
    }

}