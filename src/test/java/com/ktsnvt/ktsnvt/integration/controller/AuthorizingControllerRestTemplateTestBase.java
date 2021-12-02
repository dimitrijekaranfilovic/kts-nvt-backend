package com.ktsnvt.ktsnvt.integration.controller;

import com.ktsnvt.ktsnvt.dto.auth.AuthRequest;
import com.ktsnvt.ktsnvt.dto.auth.AuthResponse;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;

public abstract class AuthorizingControllerRestTemplateTestBase {
    protected TestRestTemplate restTemplate;
    protected HttpHeaders httpHeaders;

    protected AuthorizingControllerRestTemplateTestBase(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @AfterEach
    public void clearToken() {
        logout();
    }

    protected void login(String email, String password) {
        var authResponse = restTemplate.postForEntity("/api/super-users/authenticate",
                                                                                new AuthRequest(email, password),
                                                                                AuthResponse.class);
        httpHeaders.set("Authorization", "Bearer " + Objects.requireNonNull(authResponse.getBody()).getJwt());
    }

    protected void logout() {
        httpHeaders.set("Authorization", "Bearer ");
    }


    protected HttpEntity<Void> makeEntity() {
        return new HttpEntity<>(httpHeaders);
    }

    protected <T> HttpEntity<T> makeEntity(T request) {
        return new HttpEntity<>(request, httpHeaders);
    }
}
