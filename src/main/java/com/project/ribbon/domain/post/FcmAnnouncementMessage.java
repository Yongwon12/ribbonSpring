package com.project.ribbon.domain.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter

public class FcmAnnouncementMessage {
    private boolean validateOnly;
    private Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private Data data;
        private String topic;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Data {
        private String title;
        private String body;
        private String image;
    }
}