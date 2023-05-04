package com.project.ribbon.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project.ribbon.dto.*;
import com.project.ribbon.repository.MemberRepository;
import com.project.ribbon.service.PostService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

            WebClient webClient = WebClient.builder().build();
            String url = "https://api.iamport.kr/users/getToken";
            Map<String, Object> request = new HashMap<>();
            request.put("imp_key", impKey);
            request.put("imp_secret", impSecret);
            Mono<String> response = webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class);
            String responseBody = response.block();
            JsonElement responseJson = JsonParser.parseString(responseBody);
            JsonObject responseObj = responseJson.getAsJsonObject().getAsJsonObject("response");

            if (response instanceof ClientResponse && ((ClientResponse) response).statusCode().equals(HttpStatus.OK)) {
                // 결과 처리
                return ResponseEntity.ok(responseObj.toString());
            } else {
                // 오류 처리
                return ResponseEntity.status(((ClientResponse) response).statusCode()).body(responseBody);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }




    // 폰 인증
//    @PostMapping("/certificationsRibbon")
//    public ResponseEntity<?> handleCertificationsRequest(@RequestBody Map<String, Object> body) {
//        String impUid = (String) body.get("imp_uid");
//        Long userid = (Long) body.get("userid");
//        try {
//            // 토큰 발급
//            WebClient webClient = WebClient.builder().build();
//            String url = "https://api.iamport.kr/users/getToken";
//            Map<String, Object> request = new HashMap<>();
//            request.put("imp_key", impKey);
//            request.put("imp_secret", impSecret);
//            Mono<String> response = webClient.post()
//                    .uri(url)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .bodyValue(request)
//                    .retrieve()
//                    .bodyToMono(String.class);
//            String responseBody = response.block();
//            JsonElement responseJson = JsonParser.parseString(responseBody);
//            JsonObject responseObj = responseJson.getAsJsonObject().getAsJsonObject("response");
//
//            // imp_uid로 인증 정보 조회
//            String getCertificationsUrl = "https://api.iamport.kr/certifications/" + impUid;
//            HttpHeaders getCertificationsHeaders = new HttpHeaders();
//            String accessToken = "Bearer " + responseObj.get("access_token").getAsString();
//            getCertificationsHeaders.set("Authorization", accessToken);
//            HttpEntity<Void> getCertificationsRequest = new HttpEntity<>(getCertificationsHeaders);
//            ResponseEntity<Map> getCertificationsResponse = new RestTemplate().exchange(getCertificationsUrl, HttpMethod.GET, getCertificationsRequest, Map.class);
//            Map certificationsInfo = getCertificationsResponse.getBody();
//
//            // unique_key: 개인식별 고유 키, unique_in_site: 사이트 별 개인식별 고유 키
//            String uniqueKey = (String) certificationsInfo.get("unique_key");
//            String uniqueInSite = (String) certificationsInfo.get("unique_in_site");
//            String name = (String) certificationsInfo.get("name");
//            String gender = (String) certificationsInfo.get("gender");
//            String birth = (String) certificationsInfo.get("birth");
//
//            // 연령 제한 로직
////            int birthYear = Integer.parseInt(birth.substring(0, 4));
////            if (birthYear <= 1999) {
////                // 연령 만족
////            } else {
////                // 연령 미달
////            }
//
//            // 1인 1계정 허용 로직
//            Optional<User> user = memberRepository.findByUniqueKey(uniqueKey);
//            if (user.isEmpty()) {
//                PostUserUpdateRequest newUserUniqueInfo = new PostUserUpdateRequest();
//                newUserUniqueInfo.setImpUid(impUid);
//                newUserUniqueInfo.setUniqueKey(uniqueKey);
//                newUserUniqueInfo.setUniqueInSite(uniqueInSite);
//                newUserUniqueInfo.setUserid(userid);
//                postService.updateUserUniquePost(newUserUniqueInfo);
//                ResponseEntity.ok("인증 정보를 성공적으로 저장했습니다.");
//            } else {
//                ResponseEntity.ok("인증이 완료되었습니다.");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
//        }
//        return ResponseEntity.ok("Success");
//
//
//    }
    // 결제완료 검증
    @PostMapping("/payments/ribboncomplete")
    public ResponseEntity<?> completePayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            String merchant_uid = paymentRequest.getMerchantUid();
            Long userid = paymentRequest.getUserid();
            Long inherentid = paymentRequest.getInherentid();

            // 엑세스 토큰 발급
            WebClient webClient = WebClient.builder().build();
            String url = "https://api.iamport.kr/users/getToken";
            Map<String, Object> request = new HashMap<>();
            request.put("imp_key", impKey);
            request.put("imp_secret", impSecret);
            Mono<String> response = webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class);
            String responseBody = response.block();
            JsonElement responseJson = JsonParser.parseString(responseBody);
            JsonObject responseObj = responseJson.getAsJsonObject().getAsJsonObject("response");
            if (responseObj != null && !responseObj.isJsonNull()) {
            String accessToken = "Bearer " + responseObj.get("access_token").getAsString();
            HttpHeaders headers = new HttpHeaders();

            // merchant_uid로 결제 정보 조회
            headers.set("Authorization", accessToken);
            String paymentUrl = "https://api.iamport.kr/payments/find/" + merchant_uid;
                Mono<ResponseEntity<PaymentData>> paymentResponse = webClient.get()
                        .uri(paymentUrl)
                        .headers(httpHeaders -> httpHeaders.addAll(headers))
                        .retrieve()
                        .toEntity(PaymentData.class);
                PaymentData paymentData = webClient.get()
                        .uri(paymentUrl)
                        .headers(httpHeaders -> httpHeaders.addAll(headers))
                        .retrieve()
                        .bodyToMono(PaymentData.class)
                        .block();

            // DB에서 결제되어야 하는 금액 조회

            List<PaymentRequest> orderList = postService.findMentorOnePricePost(paymentRequest.getMerchantUid());
            PaymentRequest order = orderList.get(0);
            Integer amountToBePaid = order.getAmount();

            // 결제 검증하기
            Integer amount = paymentData.getResponse().getAmount();
            String status = paymentData.getResponse().getStatus();
            if (Objects.equals(amount, amountToBePaid)) {
                PaymentDTO paymentDTO = new PaymentDTO();
                paymentDTO.setPaymentid(paymentData.getResponse().getPaymentid());
                paymentDTO.setPaydate(paymentData.getResponse().getPaydate());
                paymentDTO.setAmount(paymentData.getResponse().getAmount());
                paymentDTO.setMerchantUid(paymentData.getResponse().getMerchant_uid());
                paymentDTO.setImpUid(paymentData.getResponse().getImp_uid());
                paymentDTO.setBuyerName(paymentData.getResponse().getBuyer_name());
                paymentDTO.setUserid(userid);
                paymentDTO.setInherentid(inherentid);
                postService.saveWritementorPaymentInfoPost(paymentDTO);
            // 결제 성공시 응답
                switch (status) {
                    case "paid":
                        return ResponseEntity.ok("결제 완료 상태");
                    case "ready":
                        return ResponseEntity.ok("결제 준비 상태");
                    case "failed":
                        return ResponseEntity.ok("결제 실패 상태");
                    case "cancelled":
                        return ResponseEntity.ok("취소 및 환불된 결제");
                    default:
                        return ResponseEntity.badRequest().body("not found status!");
                }

            } else {
            // 결제 금액 불일치
                return ResponseEntity.badRequest().body("결제 금액 불일치");
            }
            }else {
                throw new RuntimeException("Access token not found in response");
            }

        } catch (Exception e) {
            // 결제 검증 실패
            return ResponseEntity.badRequest().body("결제 검증 실패");
        }
    }

    // 결제 취소
    @PostMapping("/payments/ribbonCancel")
    public ResponseEntity<?> ribbonCancel(@RequestBody PaymentCancelRequest paymentCancelRequest) {
        try {
            String merchant_uid = paymentCancelRequest.getMerchantUid();
            Integer amount = paymentCancelRequest.getAmount();
            String tax_free = paymentCancelRequest.getTaxFree();
            String vat_amount = paymentCancelRequest.getVatAmount();
            String checksum = paymentCancelRequest.getChecksum();
            String reason = paymentCancelRequest.getReason();
            String refund_holder = paymentCancelRequest.getRefundHolder();
            String refund_bank = paymentCancelRequest.getRefundBank();
            String refund_account = paymentCancelRequest.getRefundAccount();
            String refund_tel = paymentCancelRequest.getRefundTel();
            String canceldate = paymentCancelRequest.getCanceldate();
                // DB에서 결제 시간 조회
                List<PaymentCancelRequest> orderList = postService.findPayDateOnePost(paymentCancelRequest.getMerchantUid());
                PaymentCancelRequest order = orderList.get(0);
                String dateToBePaid = order.getPaydate();
                String cancelDate = canceldate;
                // 날짜 형식 지정
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                // 날짜 변환
                LocalDateTime dateToBePaidLocalDateTime = LocalDateTime.parse(dateToBePaid, formatter);
                LocalDateTime cancelDateLocalDateTime = LocalDateTime.parse(cancelDate, formatter);

                // 날짜 차이 계산로직
                long secondsBetween = ChronoUnit.SECONDS.between(dateToBePaidLocalDateTime, cancelDateLocalDateTime);

                // 3일이내 결제 취소허용
                if (secondsBetween < 24 * 3600 * 3) {
                        // 엑세스 토큰 발급
                        WebClient webClient = WebClient.builder().build();
                        String url = "https://api.iamport.kr/users/getToken";
                        Map<String, Object> request = new HashMap<>();
                        request.put("imp_key", impKey);
                        request.put("imp_secret", impSecret);
                        Mono<String> response = webClient.post()
                                .uri(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(request)
                                .retrieve()
                                .bodyToMono(String.class);
                        String responseBody = response.block();
                        JsonElement responseJson = JsonParser.parseString(responseBody);
                        JsonObject responseObj = responseJson.getAsJsonObject().getAsJsonObject("response");
                        if (responseObj != null && !responseObj.isJsonNull()) {
                            String accessToken = "Bearer " + responseObj.get("access_token").getAsString();
                            // 해쉬맵에 데이터 담기
                            Map<String, Object> data = new HashMap<>();
                            data.put("amount", amount);
                            data.put("tax_free", tax_free);
                            data.put("vat_amount", vat_amount);
                            data.put("checksum", checksum);
                            data.put("reason", reason);
                            data.put("refund_holder", refund_holder);
                            data.put("refund_bank", refund_bank);
                            data.put("refund_account", refund_account);
                            data.put("refund_tel", refund_tel);
                            if (merchant_uid != null) {
                                data.put("merchant_uid", merchant_uid);
                            }
                            // API 호출 URL 구성
                            String apiUrl = "https://api.iamport.kr/payments/cancel";

                            // 각각의 가격에 대한 API 호출 실행
                            Mono<ResponseEntity<String>> apiResponse = null;

                            if (merchant_uid != null) {
                                apiResponse = webClient.post()
                                        .uri(apiUrl)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                                        .bodyValue(data)
                                        .retrieve()
                                        .toEntity(String.class);
                            }
                            HttpStatusCode apiStatus = null;
                            if (apiResponse != null) {
                                apiStatus = Objects.requireNonNull(apiResponse.block()).getStatusCode();
                            }
                            if (Objects.requireNonNull(apiStatus).is2xxSuccessful()) {

                                String paymentUrl = "https://api.iamport.kr/payments/find/" + merchant_uid;
                                Mono<ResponseEntity<PaymentData>> paymentResponse = webClient.get()
                                        .uri(paymentUrl)
                                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                                        .retrieve()
                                        .toEntity(PaymentData.class);
                                PaymentData paymentData = webClient.get()
                                        .uri(paymentUrl)
                                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                                        .retrieve()
                                        .bodyToMono(PaymentData.class)
                                        .block();

                                String status = Objects.requireNonNull(paymentData).getResponse().getStatus();
                                // 결과를 처리하는 로직
                                switch (status) {
                                    case "paid":
                                        return ResponseEntity.ok("결제 완료 상태입니다.");
                                    case "ready":
                                        return ResponseEntity.ok("결제 준비 상태입니다.");
                                    case "failed":
                                        return ResponseEntity.ok("결제 실패 상태입니다.");
                                    case "cancelled":
                                        return ResponseEntity.ok("취소 및 환불된 결제입니다.");
                                    default:
                                        return ResponseEntity.badRequest().build();
                                }

                            }
                            return ResponseEntity.ok().build();

                        } else {
                            throw new RuntimeException("Access token not found in response");
                        }
                    }else{
                    throw new RuntimeException("결제일로부터 3일이 경과하였습니다");
                }
            } catch(Exception e){
                // 취소 실패
                return ResponseEntity.badRequest().build();
            }
        }

    // 대여 결제완료 검증
    @PostMapping("/payments/ribbonCompleteRental")
    public ResponseEntity<?> completePaymentRental(@RequestBody PaymentRequest paymentRequest) {
        try {
            String merchant_uid = paymentRequest.getMerchantUid();
            Long userid = paymentRequest.getUserid();
            Long inherentid = paymentRequest.getInherentid();
            // 엑세스 토큰 발급
            WebClient webClient = WebClient.builder().build();
            String url = "https://api.iamport.kr/users/getToken";
            Map<String, Object> request = new HashMap<>();
            request.put("imp_key", impKey);
            request.put("imp_secret", impSecret);
            Mono<String> response = webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class);
            String responseBody = response.block();
            JsonElement responseJson = JsonParser.parseString(responseBody);
            JsonObject responseObj = responseJson.getAsJsonObject().getAsJsonObject("response");
            if (responseObj != null && !responseObj.isJsonNull()) {
                String accessToken = "Bearer " + responseObj.get("access_token").getAsString();
                HttpHeaders headers = new HttpHeaders();
                // merchant_uid로 결제 정보 조회
                headers.set("Authorization", accessToken);
                String paymentUrl = "https://api.iamport.kr/payments/find/" + merchant_uid;
                Mono<ResponseEntity<PaymentData>> paymentResponse = webClient.get()
                        .uri(paymentUrl)
                        .headers(httpHeaders -> httpHeaders.addAll(headers))
                        .retrieve()
                        .toEntity(PaymentData.class);
                PaymentData paymentData = webClient.get()
                        .uri(paymentUrl)
                        .headers(httpHeaders -> httpHeaders.addAll(headers))
                        .retrieve()
                        .bodyToMono(PaymentData.class)
                        .block();

                // DB에서 결제되어야 하는 금액 조회

                List<PaymentRequest> orderList = postService.findRentalOnePricePost(paymentRequest.getMerchantUid());
                PaymentRequest order = orderList.get(0);
                Integer amountToBePaid = order.getAmount();

                // 결제 검증하기
                Integer amount = Objects.requireNonNull(paymentData).getResponse().getAmount();
                String status = paymentData.getResponse().getStatus();
                if (Objects.equals(amount, amountToBePaid)) {
                    PaymentDTO paymentDTO = new PaymentDTO();
                    paymentDTO.setPaymentid(paymentData.getResponse().getPaymentid());
                    paymentDTO.setPaydate(paymentData.getResponse().getPaydate());
                    paymentDTO.setAmount(paymentData.getResponse().getAmount());
                    paymentDTO.setMerchantUid(paymentData.getResponse().getMerchant_uid());
                    paymentDTO.setImpUid(paymentData.getResponse().getImp_uid());
                    paymentDTO.setBuyerName(paymentData.getResponse().getBuyer_name());
                    paymentDTO.setUserid(userid);
                    paymentDTO.setInherentid(inherentid);
                    postService.savePaymentRentalInfoPost(paymentDTO);
                    // 결제 성공시 응답
                    switch (status) {
                        case "paid":
                            return ResponseEntity.ok("결제 완료 상태");
                        case "ready":
                            return ResponseEntity.ok("결제 준비 상태");
                        case "failed":
                            return ResponseEntity.ok("결제 실패 상태");
                        case "cancelled":
                            return ResponseEntity.ok("취소 및 환불된 결제");
                        default:
                            return ResponseEntity.badRequest().build();
                    }

                } else {
                    // 결제 금액 불일치
                    return ResponseEntity.badRequest().build();
                }
            }else {
                throw new RuntimeException("Access token not found in response");
            }

        } catch (Exception e) {
            // 결제 실패
            return ResponseEntity.badRequest().build();
        }
    }
    // 대여 결제 취소
    @PostMapping("/payments/ribbonCancelRental")
    public ResponseEntity<?> ribbonCancelRental(@RequestBody PaymentCancelRequest paymentCancelRequest) {
        try {
            String merchant_uid = paymentCancelRequest.getMerchantUid();
            Integer amount = paymentCancelRequest.getAmount();
            String tax_free = paymentCancelRequest.getTaxFree();
            String vat_amount = paymentCancelRequest.getVatAmount();
            String checksum = paymentCancelRequest.getChecksum();
            String reason = paymentCancelRequest.getReason();
            String refund_holder = paymentCancelRequest.getRefundHolder();
            String refund_bank = paymentCancelRequest.getRefundBank();
            String refund_account = paymentCancelRequest.getRefundAccount();
            String refund_tel = paymentCancelRequest.getRefundTel();
            String canceldate = paymentCancelRequest.getCanceldate();

            // DB에서 결제 시간 조회
            List<PaymentCancelRequest> orderList = postService.findRentalPayDateOnePost(paymentCancelRequest.getMerchantUid());
            PaymentCancelRequest order = orderList.get(0);
            String dateToBePaid = order.getPaydate();
            String cancelDate = canceldate;
            // 날짜 형식 지정
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // 날짜 변환
            LocalDateTime dateToBePaidLocalDateTime = LocalDateTime.parse(dateToBePaid, formatter);
            LocalDateTime cancelDateLocalDateTime = LocalDateTime.parse(cancelDate, formatter);

            // 날짜 차이 계산로직
            long secondsBetween = ChronoUnit.SECONDS.between(dateToBePaidLocalDateTime, cancelDateLocalDateTime);

            // 3일이내 결제 최소허용
            if (secondsBetween < 24 * 3600 * 3) {
                // 엑세스 토큰 발급
                WebClient webClient = WebClient.builder().build();
                String url = "https://api.iamport.kr/users/getToken";
                Map<String, Object> request = new HashMap<>();
                request.put("imp_key", impKey);
                request.put("imp_secret", impSecret);
                Mono<String> response = webClient.post()
                        .uri(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(String.class);
                String responseBody = response.block();
                JsonElement responseJson = JsonParser.parseString(responseBody);
                JsonObject responseObj = responseJson.getAsJsonObject().getAsJsonObject("response");
                if (responseObj != null && !responseObj.isJsonNull()) {
                    String accessToken = "Bearer " + responseObj.get("access_token").getAsString();
                    // 헤더 추가
                    Map<String, Object> data = new HashMap<>();
                    data.put("amount", amount);
                    data.put("tax_free", tax_free);
                    data.put("vat_amount", vat_amount);
                    data.put("checksum", checksum);
                    data.put("reason", reason);
                    data.put("refund_holder", refund_holder);
                    data.put("refund_bank", refund_bank);
                    data.put("refund_account", refund_account);
                    data.put("refund_tel", refund_tel);
                    if (merchant_uid != null) {
                        data.put("merchant_uid", merchant_uid);
                    }
                    // API 호출 URL 구성
                    String apiUrl = "https://api.iamport.kr/payments/cancel";

                    // 각각의 가격에 대한 API 호출 실행
                    Mono<ResponseEntity<String>> apiResponse = null;

                    if (merchant_uid != null) {
                        apiResponse = webClient.post()
                                .uri(apiUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .bodyValue(data)
                                .retrieve()
                                .toEntity(String.class);
                    }
                    HttpStatusCode apiStatus = null;
                    if (apiResponse != null) {
                        apiStatus = Objects.requireNonNull(apiResponse.block()).getStatusCode();
                    }
                    if (Objects.requireNonNull(apiStatus).is2xxSuccessful()) {

                        String paymentUrl = "https://api.iamport.kr/payments/find/" + merchant_uid;
                        Mono<ResponseEntity<PaymentData>> paymentResponse = webClient.get()
                                .uri(paymentUrl)
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .retrieve()
                                .toEntity(PaymentData.class);
                        PaymentData paymentData = webClient.get()
                                .uri(paymentUrl)
                                .header(HttpHeaders.AUTHORIZATION, accessToken)
                                .retrieve()
                                .bodyToMono(PaymentData.class)
                                .block();

                        String status = Objects.requireNonNull(paymentData).getResponse().getStatus();
                        System.out.println(status);
                        // 결과를 처리하는 로직
                        switch (status) {
                            case "paid":
                                return ResponseEntity.ok("결제 완료 상태입니다.");
                            case "ready":
                                return ResponseEntity.ok("결제 준비 상태입니다.");
                            case "failed":
                                return ResponseEntity.ok("결제 실패 상태입니다.");
                            case "cancelled":
                                return ResponseEntity.ok("취소 및 환불된 결제입니다.");
                            default:
                                return ResponseEntity.badRequest().build();
                        }

                    }
                    return ResponseEntity.ok().build();

                } else {
                    throw new RuntimeException("Access token not found in response");
                }
            }else {
                throw new RuntimeException("결제일로부터 3일이 경과하였습니다");
            }
        } catch(Exception e){
            // 취소 실패
            return ResponseEntity.badRequest().build();
        }
    }
    }
