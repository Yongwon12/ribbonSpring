package com.project.ribbon.controller;

import com.project.ribbon.dto.User;
import com.project.ribbon.repository.MemberRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class PostPortOneCertify {
    private final MemberRepository memberRepository;
    @Value("${myapp.secretToken}")
    private String secretToken;
    @Value("${myapp.impKey}")
    private String impKey;
    @Value("${myapp.impSecret}")
    private String impSecret;

    public PostPortOneCertify(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 엑세스 토큰 얻기
    @PostMapping("/getRibbonAccessToken")
    public ResponseEntity<String> getAccessToken(@RequestBody Map<String, Object> requestBody) {
        try {
            String clientSecretToken = (String) requestBody.get("secretToken");
            if (!secretToken.equals(clientSecretToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid secret token");
            }

            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.iamport.kr/users/getToken";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject request = new JSONObject();
            request.put("imp_key", impKey);
            request.put("imp_secret", impSecret);
            HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                String result = response.getBody();
                // 결과 처리
                return ResponseEntity.ok(result);
            } else {
                // 오류 처리
                return ResponseEntity.status(response.getStatusCode()).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    // 폰 인증
    @PostMapping("/certificationsRibbon")
    public ResponseEntity<String> handleCertificationsRequest(@RequestBody Map<String, Object> body) {
        String impUid = (String) body.get("imp_uid");
        try {
            String getTokenUrl = "https://api.iamport.kr/users/getToken";
            HttpHeaders getTokenHeaders = new HttpHeaders();
            getTokenHeaders.setContentType(MediaType.APPLICATION_JSON);
            Map<String, String> getTokenData = new HashMap<>();
            getTokenData.put("imp_key", impKey);
            getTokenData.put("imp_secret", impSecret);
            HttpEntity<Map<String, String>> getTokenRequest = new HttpEntity<>(getTokenData, getTokenHeaders);
            ResponseEntity<Map> getTokenResponse = new RestTemplate().exchange(getTokenUrl, HttpMethod.POST, getTokenRequest, Map.class);
            String accessToken = (String) getTokenResponse.getBody().get("access_token");


            // imp_uid로 인증 정보 조회
            String getCertificationsUrl = "https://api.iamport.kr/certifications/" + impUid;
            HttpHeaders getCertificationsHeaders = new HttpHeaders();
            getCertificationsHeaders.set("Authorization", accessToken);
            HttpEntity<Void> getCertificationsRequest = new HttpEntity<>(getCertificationsHeaders);
            ResponseEntity<Map> getCertificationsResponse = new RestTemplate().exchange(getCertificationsUrl, HttpMethod.GET, getCertificationsRequest, Map.class);
            Map certificationsInfo = getCertificationsResponse.getBody();

            // unique_key: 개인식별 고유 키, unique_in_site: 사이트 별 개인식별 고유 키
            String uniqueKey = (String) certificationsInfo.get("unique_key");
            String uniqueInSite = (String) certificationsInfo.get("unique_in_site");
            String name = (String) certificationsInfo.get("name");
            String gender = (String) certificationsInfo.get("gender");
            String birth = (String) certificationsInfo.get("birth");

            // 연령 제한 로직
            int birthYear = Integer.parseInt(birth.substring(0, 4));
            if (birthYear <= 1999) {
                // 연령 만족
            } else {
                // 연령 미달
            }

            // 1인 1계정 허용 로직
            Optional<User> user = memberRepository.findByUserid(uniqueKey);
            if (user.isEmpty()) {
                throw new RuntimeException("계정이 존재하지 않습니다.");
            } else {
                ResponseEntity.ok("인증이 완료되었습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
        return ResponseEntity.ok("Success");
    }
}
