package com.project.ribbon.domain.post;

import lombok.Data;

@Data
public class MemberLoginRequestDto {
    private String userid;
    private String password;
}