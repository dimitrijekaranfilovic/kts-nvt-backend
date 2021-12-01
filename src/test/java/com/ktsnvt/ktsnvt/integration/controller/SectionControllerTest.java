package com.ktsnvt.ktsnvt.integration.controller;

import com.ktsnvt.ktsnvt.dto.auth.AuthRequest;
import com.ktsnvt.ktsnvt.dto.auth.AuthResponse;
import com.ktsnvt.ktsnvt.dto.createrestauranttable.CreateRestaurantTableRequest;
import com.ktsnvt.ktsnvt.dto.createrestauranttable.CreateRestaurantTableResponse;
import com.ktsnvt.ktsnvt.dto.createsection.CreateSectionRequest;
import com.ktsnvt.ktsnvt.dto.createsection.CreateSectionResponse;
import com.ktsnvt.ktsnvt.dto.movetable.MoveTableRequest;
import com.ktsnvt.ktsnvt.dto.readsection.ReadSectionResponse;
import com.ktsnvt.ktsnvt.dto.readsectiontablesresponse.ReadSectionTablesResponse;
import com.ktsnvt.ktsnvt.dto.updatesection.UpdateSectionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class SectionControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        var authResponse = restTemplate.postForEntity("/api/super-users/authenticate", new AuthRequest("email2@email.com", "password"), AuthResponse.class);
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + Objects.requireNonNull(authResponse.getBody()).getJwt());
    }

    @Test
    void getAllSections_isSuccess() {
        var response = restTemplate.getForEntity("/api/sections", ReadSectionResponse[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertTrue(Objects.requireNonNull(response.getBody()).length > 0);
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
        HttpEntity<CreateSectionRequest> entity = new HttpEntity<>(new CreateSectionRequest("Newest Section"), headers);
        var response = restTemplate.postForEntity("/api/sections", entity, CreateSectionResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void createSection_calledWithExistingName_returnsBadRequest() {
        HttpEntity<CreateSectionRequest> entity = new HttpEntity<>(new CreateSectionRequest("Terrace"), headers);
        var response = restTemplate.postForEntity("/api/sections", entity, CreateSectionResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateSection_calledWithValidData_isSuccess() {
        HttpEntity<UpdateSectionRequest> entity = new HttpEntity<>(new UpdateSectionRequest("Updated name"), headers);

        var response = restTemplate.exchange("/api/sections/4", HttpMethod.PUT, entity, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void updateSection_calledWithExistingName_returnsBadRequest() {
        HttpEntity<UpdateSectionRequest> entity = new HttpEntity<>(new UpdateSectionRequest("Terrace"), headers);

        var response = restTemplate.exchange("/api/sections/2", HttpMethod.PUT, entity, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteSection_calledWithValidData_isSuccess() {
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        var response = restTemplate.exchange("/api/sections/1", HttpMethod.DELETE, entity, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteSection_calledWithInvalidId_returnsNotFound() {
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        var response = restTemplate.exchange("/api/sections/0", HttpMethod.DELETE, entity, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteSection_calledWithSectionWithOccupiedTable_returnsBadRequest() {
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        var response = restTemplate.exchange("/api/sections/4", HttpMethod.DELETE, entity, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createTable_whenCalledWithValidData_isSuccess() {
        HttpEntity<CreateRestaurantTableRequest> entity = new HttpEntity<>(new CreateRestaurantTableRequest(123,123,123,1), headers);

        var response = restTemplate.postForEntity("/api/sections/3/tables", entity, CreateRestaurantTableResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void createTable_whenCalledWithOverlapingTable_returnsBadRequest() {
        HttpEntity<CreateRestaurantTableRequest> entity = new HttpEntity<>(new CreateRestaurantTableRequest(123,0,0,5), headers);

        var response = restTemplate.postForEntity("/api/sections/3/tables", entity, CreateRestaurantTableResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createTable_whenCalledWithExistingTableNumber_returnsBadRequest() {
        HttpEntity<CreateRestaurantTableRequest> entity = new HttpEntity<>(new CreateRestaurantTableRequest(1,213,213,1), headers);

        var response = restTemplate.postForEntity("/api/sections/3/tables", entity, CreateRestaurantTableResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createTable_whenCalledWithInvalidSectionId_returnsBadRequest() {
        HttpEntity<CreateRestaurantTableRequest> entity = new HttpEntity<>(new CreateRestaurantTableRequest(1,213,213,1), headers);

        var response = restTemplate.postForEntity("/api/sections/0/tables", entity, CreateRestaurantTableResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateTable_whenCalledWithValidData_isSuccess() {
        HttpEntity<MoveTableRequest> entity = new HttpEntity<>(new MoveTableRequest(100,100), headers);

        var response = restTemplate.exchange("/api/sections/4/tables/11", HttpMethod.PUT, entity, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void updateTable_whenCalledWithOverlapingTable_returnsBadRequest() {
        HttpEntity<MoveTableRequest> entity = new HttpEntity<>(new MoveTableRequest(0,0), headers);

        var response = restTemplate.exchange("/api/sections/4/tables/11", HttpMethod.PUT, entity, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateTable_whenCalledWithInvalidSectionId_returnsNotFound() {
        HttpEntity<MoveTableRequest> entity = new HttpEntity<>(new MoveTableRequest(213,213), headers);

        var response = restTemplate.exchange("/api/sections/0/tables/11", HttpMethod.PUT, entity, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateTable_whenCalledWithInvalidTableId_returnsNotFound() {
        HttpEntity<MoveTableRequest> entity = new HttpEntity<>(new MoveTableRequest(213,213), headers);

        var response = restTemplate.exchange("/api/sections/4/tables/0", HttpMethod.PUT, entity, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteTable_calledWithValidData_isSuccess() {
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        var response = restTemplate.exchange("/api/sections/tables/5", HttpMethod.DELETE, entity, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteTable_calledWithInvalidId_returnsNotFound() {
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        var response = restTemplate.exchange("/api/sections/tables/0", HttpMethod.DELETE, entity, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteTable_calledWithOccupiedTable_returnsBadRequest() {
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        var response = restTemplate.exchange("/api/sections/tables/10", HttpMethod.DELETE, entity, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
