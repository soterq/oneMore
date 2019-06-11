package com.unifun.voice.jsonmapper.model;
/*
res.json({jwt: token, refreshToken: refreshToken});
 */

import lombok.Data;

@Data
public class TokenResponse {
    private String jwt;
    private String refreshToken;
}
