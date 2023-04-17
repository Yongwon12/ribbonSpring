package com.project.ribbon.controller;

import com.project.ribbon.domain.post.PostUserUpdateRequest;
import com.project.ribbon.dto.User;
import com.project.ribbon.repository.MemberRepository;
import com.project.ribbon.service.PostService;
import net.minidev.json.JSONObject;
import org.mybatis.spring.SqlSessionTemplate;
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
    private final SqlSessionTemplate sqlSession;

    private final MemberRepository memberRepository;
    private final PostService postService;
    @Value("${myapp.secretToken}")
    private String secretToken;
    @Value("${myapp.impKey}")
    private String impKey;
    @Value("${myapp.impSecret}")
    private String impSecret;

    public PostPortOneCertify(MemberRepository memberRepository, PostService postService, SqlSessionTemplate sqlSession) {
        this.memberRepository = memberRepository;
        this.postService = postService;
        this.sqlSession = sqlSession;
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
    public ResponseEntity<?> handleCertificationsRequest(@RequestBody Map<String, Object> body) {
        String impUid = (String) body.get("imp_uid");
        Long userid = (Long) body.get("userid");
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
//            int birthYear = Integer.parseInt(birth.substring(0, 4));
//            if (birthYear <= 1999) {
//                // 연령 만족
//            } else {
//                // 연령 미달
//            }

            // 1인 1계정 허용 로직
            Optional<User> user = memberRepository.findByUniqueKey(uniqueKey);
            if (user.isEmpty()) {
                PostUserUpdateRequest newUserUniqueInfo = new PostUserUpdateRequest();
                newUserUniqueInfo.setImpUid(impUid);
                newUserUniqueInfo.setUniqueKey(uniqueKey);
                newUserUniqueInfo.setUniqueInSite(uniqueInSite);
                newUserUniqueInfo.setUserid(userid);
                postService.updateUserUniquePost(newUserUniqueInfo);
                ResponseEntity.ok("인증 정보를 성공적으로 저장했습니다.");
            } else {
                ResponseEntity.ok("인증이 완료되었습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
        return ResponseEntity.ok("Success");


    }
    // 결제완료 검증
//    @PostMapping("/payments/complete")
//    public ResponseEntity<?> completePayment(@RequestBody PaymentRequest paymentRequest) {
//        try {
//            String imp_uid = paymentRequest.getImpUid();
//            String merchant_uid = paymentRequest.getMerchantUid();
//            // 엑세스 토큰 발급
//            String getTokenUrl = "https://api.iamport.kr/users/getToken";
//            HttpHeaders getTokenHeaders = new HttpHeaders();
//            getTokenHeaders.setContentType(MediaType.APPLICATION_JSON);
//            Map<String, String> getTokenData = new HashMap<>();
//            getTokenData.put("imp_key", impKey);
//            getTokenData.put("imp_secret", impSecret);
//
//            HttpEntity<Map<String, String>> getTokenRequest = new HttpEntity<>(getTokenData, getTokenHeaders);
//            ResponseEntity<Map> getTokenResponse = new RestTemplate().exchange(getTokenUrl, HttpMethod.POST, getTokenRequest, Map.class);
//            String accessToken = (String) getTokenResponse.getBody().get("access_token");
//
//            // imp_uid로 결제 정보 조회
//            String paymentUrl = "https://api.iamport.kr/payments/" + imp_uid;
//            HttpHeaders headers = new HttpHeaders();
//            headers.setBearerAuth(accessToken);
//            RestTemplate restTemplate = new RestTemplate();
//            HttpEntity<String> paymentEntity = new HttpEntity<>(headers);
//            ResponseEntity<PaymentResponse> paymentResponse = restTemplate.exchange(paymentUrl, HttpMethod.GET, paymentEntity, PaymentResponse.class);
//            PaymentData paymentData = paymentResponse.getBody().getResponse(); // 조회한 결제 정보
//
//            // DB에서 결제되어야 하는 금액 조회
//            Map<String, Object> paramMap = new HashMap<>();
//            paramMap.put("merchantUid", paymentData.getMerchantUid());
//            Order order = sqlSession.selectOne("postMapper.findMentorOnePricePost", paramMap);
//            Integer amountToBePaid = order.getAmount(); // 결제 되어야 하는 금액
//
//            // 결제 검증하기
//            Integer amount = paymentData.getAmount();
//            String status = paymentData.getStatus();
//            if (amount == amountToBePaid) {
//                ordersRepository.updatePayment(paymentData); // DB에 결제 정보 저장
//// 결제 성공시 응답
//                switch (status) {
//                    case "paid":
//                        return ResponseEntity.ok().build();
//                    default:
//                        return ResponseEntity.badRequest().build();
//                }
//            } else {
//// 결제 금액 불일치
//                return ResponseEntity.badRequest().build();
//            }
//        } catch (Exception e) {
//// 결제 실패
//            return ResponseEntity.badRequest().build();
//        }
//    }
}
