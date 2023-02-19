package com.project.ribbon.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.project.ribbon.domain.post.FcmGroupMessage;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class FirebaseCloudGroupMessageCommentsService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/sportscommunity-d11ee/messages:send";
    private final ObjectMapper objectMapper;
    @Scheduled(cron = "00 00 18 * * *")
    //@Scheduled(fixedRate = 5000)
    public void sendMessageTo() throws IOException {
        String message = makeMessage();

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessTopic())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());
    }

    private String makeMessage() throws JsonParseException, JsonProcessingException {
        FcmGroupMessage fcmGroupMessage = FcmGroupMessage.builder()
                .message(FcmGroupMessage.Message.builder()
                        .topic("topic")
                        .data(FcmGroupMessage.Data.builder()
                                .title("맺음")
                                .body("지금 바로 운동메이트를 구해보세요!")
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmGroupMessage);
    }

    private String getAccessTopic() throws IOException {
        String firebaseConfigPath = "/firebase/sportscommunity-d11ee-firebase-adminsdk-i14sx-647d1e0507.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}