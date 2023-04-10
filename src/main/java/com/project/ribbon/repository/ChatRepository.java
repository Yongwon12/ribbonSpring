package com.project.ribbon.repository;

import com.project.ribbon.dto.ChatMessageEntity;
import com.project.ribbon.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRepository {
    @Autowired
    private ChatMapper chatMapper;

    public void save(ChatMessageEntity chatMessageEntity) {
        chatMapper.insert(chatMessageEntity);
    }
}





