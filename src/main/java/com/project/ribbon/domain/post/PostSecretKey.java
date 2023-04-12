package com.project.ribbon.domain.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSecretKey {

    @NotBlank
    private String secrettoken;
}