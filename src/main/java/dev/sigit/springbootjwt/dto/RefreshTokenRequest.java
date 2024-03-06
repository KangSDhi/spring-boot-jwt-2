package dev.sigit.springbootjwt.dto;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String token;
}
