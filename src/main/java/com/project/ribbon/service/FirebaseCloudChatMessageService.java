package com.project.ribbon.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.project.ribbon.domain.post.FcmChatMessage;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FirebaseCloudChatMessageService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/sportscommunity-d11ee/messages:send";
    private final ObjectMapper objectMapper;

    public void sendMessageTo(String token,String sender) throws IOException {
        String message = makeMessage(token,sender);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());
    }

    private String makeMessage(String token,String sender) throws JsonParseException, JsonProcessingException {
        FcmChatMessage fcmChatMessage = FcmChatMessage.builder()
                .message(FcmChatMessage.Message.builder()
                        .token(token)
                        .data(FcmChatMessage.Data.builder()
                                .title("맺음")
                                .body(sender+"님이 새로운 메세지를 보냈습니다.")
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();
        return objectMapper.writeValueAsString(fcmChatMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "/firebase/sportscommunity-d11ee-firebase-adminsdk-i14sx-647d1e0507.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}