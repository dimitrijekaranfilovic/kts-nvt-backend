package com.ktsnvt.ktsnvt.config.jwt;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenFilter {

    @Value("${jwt.header.string}")
    public String headerString;

    @Value("${jwt.token.prefix}")
    public String tokenPrefix;
}
