package com.project.ribbon.config;

import com.project.ribbon.filter.JwtAuthenticationFilter;
import com.project.ribbon.provide.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .csrf().ignoringRequestMatchers("/api/ribbon")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/api/ribbon").permitAll()
                .requestMatchers("/api/login").permitAll()
                .requestMatchers("/api/sign").permitAll()
                .requestMatchers("/api/boardimage/**").permitAll()
                .requestMatchers("/api/userimage/**").permitAll()
                .requestMatchers("/api/groupimage/**").permitAll()
                .requestMatchers("/api/usedimage/**").permitAll()
                .requestMatchers("/api/writementortitleimage/**").permitAll()
                .requestMatchers("/ribbon/ribbon.gif").permitAll()
                .requestMatchers("/ribbon/ribbonding.png").permitAll()
                .requestMatchers("/ribbon").permitAll()
                .requestMatchers("/ribbon/admin/**").hasRole("ADMIN")
                .requestMatchers("/ribbon/admin/form/**").hasRole("ADMIN")
                .requestMatchers("/ws/chat").hasAnyRole("USER","INSTRUCTOR")
                .requestMatchers("/chat/form/**").hasAnyRole("USER","INSTRUCTOR")
                .requestMatchers("/payments/**").hasAnyRole("USER","INSTRUCTOR")
                .requestMatchers("/ribbonaccesstoken").hasAnyRole("USER","INSTRUCTOR")
                .requestMatchers("/certificationsribbon").hasAnyRole("USER","INSTRUCTOR")
                .requestMatchers("/api/searchwritementor").hasAnyRole("USER","INSTRUCTOR")
                .requestMatchers("/api/ribbonrefresh").hasAnyRole("USER","INSTRUCTOR")
                .requestMatchers("/api/realtimeup").hasAnyRole("USER","INSTRUCTOR")
                .requestMatchers("/api/board").hasAnyRole("USER","INSTRUCTOR")
                .requestMatchers("/api/individual").hasAnyRole("USER","INSTRUCTOR")
                .requestMatchers("/api/group").hasAnyRole("USER","INSTRUCTOR")
                .requestMatchers("/api/used").hasAnyRole("USER","INSTRUCTOR")
                .requestMatchers("/api/writementor").hasAnyRole("USER","INSTRUCTOR")
                .requestMatchers("/api/form/**").hasAnyRole("USER","INSTRUCTOR")
                .requestMatchers("/ribbon/admin/announcementinfo").hasAnyRole("USER","ADMIN","INSTRUCTOR")
                .requestMatchers("/ribbon/admin/reportinsertuser").hasAnyRole("USER","ADMIN","INSTRUCTOR")
                .requestMatchers("/ribbon/admin/reportinsertmentor").hasAnyRole("USER","ADMIN","INSTRUCTOR")
                .requestMatchers("/ribbon/admin/reportinsertboard").hasAnyRole("USER","ADMIN","INSTRUCTOR")
                .requestMatchers("/ribbon/admin/reportinsertindividual").hasAnyRole("USER","ADMIN","INSTRUCTOR")
                .requestMatchers("/ribbon/admin/reportinsertgroup").hasAnyRole("USER","ADMIN","INSTRUCTOR")
                .requestMatchers("/ribbon/admin/reportinsertused").hasAnyRole("USER","ADMIN","INSTRUCTOR")
                .requestMatchers("/ribbon/admin/reportinsertcomments").hasAnyRole("USER","ADMIN","INSTRUCTOR")
                .requestMatchers("/ribbon/admin/reportinsertindividualcomments").hasAnyRole("USER","ADMIN","INSTRUCTOR")
                .requestMatchers("/ribbon/admin/reportinsertgroupcomments").hasAnyRole("USER","ADMIN","INSTRUCTOR")
                .requestMatchers("/ribbon/admin/reportinsertusedcomments").hasAnyRole("USER","ADMIN","INSTRUCTOR")
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}