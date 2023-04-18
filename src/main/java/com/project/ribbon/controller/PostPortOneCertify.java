package com.project.ribbon.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project.ribbon.domain.post.PostUserUpdateRequest;
import com.project.ribbon.dto.PaymentData;
import com.project.ribbon.dto.PaymentRequest;
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
import java.util.List;
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
            // 토큰 발급
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            String url = "https://api.iamport.kr/users/getToken";
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> request = new HashMap<>();
            request.put("imp_key", impKey);
            request.put("imp_secret", impSecret);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String responseBody = response.getBody();
            JsonElement responseJson = JsonParser.parseString(responseBody);
            JsonObject responseObj = responseJson.getAsJsonObject().getAsJsonObject("response");
            String accessToken = "Bearer " + responseObj.get("access_token").getAsString();
            headers.set("Authorization", accessToken);
            System.out.println(accessToken);

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
    @PostMapping("/payments/ribboncomplete")
    public ResponseEntity<?> completePayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            String imp_uid = paymentRequest.getImpUid();
            String merchant_uid = paymentRequest.getMerchantUid();
            System.out.println(merchant_uid);
            // 엑세스 토큰 발급
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            String url = "https://api.iamport.kr/users/getToken";
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, Object> request = new HashMap<>();
            request.put("imp_key", impKey);
            request.put("imp_secret", impSecret);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String responseBody = response.getBody();
            JsonElement responseJson = JsonParser.parseString(responseBody);
            JsonObject responseObj = responseJson.getAsJsonObject().getAsJsonObject("response");
            String accessToken = "Bearer " + responseObj.get("access_token").getAsString();

            // merchant_uid로 결제 정보 조회
//            String payment_status = "-paid";
            headers.set("Authorization", accessToken);
            System.out.println(headers);
            String paymentUrl = "https://api.iamport.kr/payments/find/" + merchant_uid; //+ "/" + payment_status;
            HttpEntity<String> paymentEntity = new HttpEntity<>(headers);
            ResponseEntity<PaymentData> paymentResponse = restTemplate.exchange(paymentUrl, HttpMethod.GET, paymentEntity, PaymentData.class);
            PaymentData paymentData = paymentResponse.getBody(); // 조회한 결제 정보
            System.out.println(paymentData);

            // DB에서 결제되어야 하는 금액 조회

            List<PaymentRequest> orderList = postService.findMentorOnePricePost(paymentRequest.getMerchantUid());
            PaymentRequest order = orderList.get(0);
            Integer amountToBePaid = order.getAmount();
            System.out.println(amountToBePaid);

            // 결제 검증하기
            Integer amount = paymentData.getResponse().getAmount();
            System.out.println(amount);
            String status = paymentData.getResponse().getStatus();
            System.out.println(status);
            if (amount == amountToBePaid) {
                PaymentData paymentData1 = new PaymentData();
                paymentData1.setMerchant_uid(paymentData.getResponse().getMerchant_uid());
                paymentData1.setAmount(paymentData.getResponse().getAmount());
                paymentData1.setImp_uid(paymentData.getResponse().getImp_uid());
                paymentData1.setBuyer_name(paymentData.getResponse().getBuyer_name());
                postService.saveWritementorPaymentInfoPost(paymentData1);
            // 결제 성공시 응답
                switch (status) {
                    case "paid":
                        return ResponseEntity.ok("결제가 성공적으로 완료되었습니다.");
                    case "ready":
                        return ResponseEntity.ok("결제 되지 않았습니다.");
                    case "failed":
                        return ResponseEntity.ok("결제에 실패했습니다.");
                    case "cancelled":
                        return ResponseEntity.ok("취소 및 환불된 결제입니다.");
                    default:
                        return ResponseEntity.badRequest().build();
                }

            } else {
            // 결제 금액 불일치
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {
            // 결제 실패
            return ResponseEntity.badRequest().build();
        }
    }

}
