package com.ktsnvt.ktsnvt.integration.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.ktsnvt.ktsnvt.dto.auth.AuthRequest;
import com.ktsnvt.ktsnvt.dto.auth.AuthResponse;
import com.ktsnvt.ktsnvt.dto.createsection.CreateSectionRequest;
import com.ktsnvt.ktsnvt.dto.createsection.CreateSectionResponse;
import com.ktsnvt.ktsnvt.dto.readsection.ReadSectionResponse;
import com.ktsnvt.ktsnvt.dto.readsectiontablesresponse.ReadSectionTablesResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collection;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getAllSections_isSuccess() {
        var response = restTemplate.getForEntity("/api/sections", ReadSectionResponse[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals(4, Objects.requireNonNull(response.getBody()).length);
    }

    @Test
    void getAllSectionTables_isSuccess() {
        var response = restTemplate.getForEntity("/api/sections/1/tables", ReadSectionTablesResponse[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals(3, Objects.requireNonNull(response.getBody()).length);
    }

    @Test
    void createSection_calledWithValidData_isSuccess() {
        var authResponse = restTemplate.postForEntity("/api/super-users/authenticate", new AuthRequest("email1@email.com", "password"), AuthResponse.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + Objects.requireNonNull(authResponse.getBody()).getJwt());

        HttpEntity<String> entity = new HttpEntity<String>("{name: \"New Section\"}", headers);
        var response = restTemplate.postForObject("/api/sections", entity, CreateSectionResponse.class);

        assertNotNull(response);
    }
}
