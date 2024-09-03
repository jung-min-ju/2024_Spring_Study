package com.example.filemanage.dto;

public record TokenRes (
        String accessToken,
        String refreshToken
){
    public static TokenRes of(String accessToken, String refreshToken) {
        return new TokenRes(accessToken, refreshToken);
    }
}
