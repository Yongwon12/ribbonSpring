package com.project.ribbon.mapper;

import com.project.ribbon.dto.ChatMessageEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {
    @Insert("INSERT INTO chat (message, sender, timestamp) VALUES (#{message}, #{sender}, #{timestamp})")
    void insert(ChatMessageEntity chatEntity);
}