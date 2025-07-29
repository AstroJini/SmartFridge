package com.be16_2nd.SmartFridge.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginResDto {
    private String accessToken;
    private String refreshToken;
}
