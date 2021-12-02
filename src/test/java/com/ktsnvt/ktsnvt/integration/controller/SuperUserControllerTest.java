package com.ktsnvt.ktsnvt.integration.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktsnvt.ktsnvt.dto.auth.AuthRequest;
import com.ktsnvt.ktsnvt.dto.readsuperusers.ReadSuperUsersRequest;
import com.ktsnvt.ktsnvt.dto.updatepassword.UpdatePasswordRequest;
import com.ktsnvt.ktsnvt.dto.updatesalary.UpdateSalaryRequest;
import com.ktsnvt.ktsnvt.dto.updatesuperuser.UpdateSuperUserRequest;
import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class SuperUserControllerTest extends AuthorizingControllerMockMvcTestBase {
    @LocalServerPort
    private int port;

    @Autowired
    public SuperUserControllerTest(WebApplicationContext webApplicationContext, ObjectMapper mapper) {
        super(webApplicationContext, mapper);
    }

    @ParameterizedTest
    @MethodSource("provideValidUpdatePasswordCredentials")
    void updatePassword_whenCalledWithValidData_isSuccess(String email, String password, int id) throws Exception {
        login(email, password);
        var request = new UpdatePasswordRequest(password, "new password");
        mockMvc.perform(put("/api/super-users/{id}/password", id)
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + token)
                    .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk()
                );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUpdatePasswordCredentials")
    void updatePassword_whenCalledWithInvalidData_isFailure(String email, String password, String oldPassword, int id, int status) throws Exception {
        login(email, password);
        var request = new UpdatePasswordRequest(oldPassword, "new password");
        mockMvc.perform(put("/api/super-users/{id}/password", id)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().is(status)
                );
    }

    @Test
    void updateSuperUser_whenCalledWithValidData_isSuccess() throws Exception {
        login("email1@email.com", "password");
        var request = new UpdateSuperUserRequest("pera", "peric", "pera@gmail.com");
        var id = 4;
        mockMvc.perform(put("/api/super-users/{id}", id)
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + token)
                    .content(mapper.writeValueAsBytes(request)))
                .andExpectAll(
                        status().isOk()
                );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUpdateSuperUserRequests")
    void updateSuperUser_whenCalledWithInvalidData_isFailure(String email, String password, UpdateSuperUserRequest request, int id, int status) throws Exception {
        login(email, password);
        mockMvc.perform(put("/api/super-users/{id}", id)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .content(mapper.writeValueAsBytes(request)))
                .andExpectAll(
                        status().is(status)
                );
    }

    @Test
    void updateSalary_whenCalledWithValidData_isSuccess() throws Exception {
        login("email2@email.com", "password");
        var id = 4;
        var request = new UpdateSalaryRequest(BigDecimal.valueOf(242424L));
        mockMvc.perform(put("/api/super-users/{id}/salary", id)
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + token)
                    .content(mapper.writeValueAsBytes(request)))
                .andExpectAll(
                        status().isOk()
                );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidUpdateSalaryRequests")
    void updateSalary_whenCalledWIthInvalidData_isFailure(String email, String password, int id, UpdateSalaryRequest request, int status) throws Exception {
        login(email, password);
        mockMvc.perform(put("/api/super-users/{id}/salary", id)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .content(mapper.writeValueAsBytes(request)))
                .andExpectAll(
                        status().is(status)
                );
    }

    @Test
    void deleteManager_whenCalledWithValidData_isSuccess() throws Exception {
        login("email2@email.com", "password");
        var id = 4;
        mockMvc.perform(delete("/api/super-users/{id}", id)
                .header("Authorization", "Bearer " + token))
                .andExpectAll(
                        status().isNoContent()
                );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDeleteManagerRequests")
    void deleteManager_whenCalledWithInvalidData_isFailure(String email, String password, int id, int status) throws Exception {
        login(email, password);
        mockMvc.perform(delete("/api/super-users/{id}", id)
                .header("Authorization", "Bearer " + token))
                .andExpectAll(
                        status().is(status)
                );
    }

    @ParameterizedTest
    @MethodSource("provideQueryParameters")
    void readSuperUsers_whenCalledWithValidQueryParameters_isSuccess(String query, BigDecimal from, BigDecimal to, SuperUserType type, Pageable pageable, int expected) throws Exception {
        login("email2@email.com", "password");
        mockMvc.perform(get("/api/super-users")
                .header("Authorization", "Bearer " + token)
                .param("query", query)
                .param("salaryLowerBound", Objects.requireNonNullElse(from, "").toString())
                .param("salaryUpperBound", Objects.requireNonNullElse(to, "").toString())
                .param("type", Objects.requireNonNullElse(type, "").toString())
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize())))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.totalElements", equalTo(expected)),
                        jsonPath("$.totalPages", lessThanOrEqualTo(1)),
                        jsonPath("$.content", hasSize(expected))
                );
    }

    @ParameterizedTest
    @MethodSource("provideValidLoginParams")
    void authenticate_withValidData_isSuccess(String email, String password) throws Exception {
        var authRequest = new AuthRequest(email, password);

        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/super-users/authenticate", this.port))
                        .content(mapper.writeValueAsString(authRequest))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidLoginParams")
    void authenticate_withInvalidData_throwsException(String email, String password, int status) throws Exception {
        var authRequest = new AuthRequest(email, password);

        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/super-users/authenticate", this.port))
                        .content(mapper.writeValueAsString(authRequest))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().is(status));
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideValidUpdatePasswordCredentials() {
        return Stream.of(
                Arguments.of("email1@email.com", "password", 4),
                Arguments.of("email2@email.com", "password", 5)
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideInvalidUpdatePasswordCredentials() {
        return Stream.of(
                Arguments.of("email1@email.com", "password", "password", 5, 403),
                Arguments.of("email2@email.com", "password", "password", 4, 403),
                Arguments.of("email1@email.com", "password", "password123", 4, 400),
                Arguments.of("email2@email.com", "password", "password123", 5, 400)
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideInvalidUpdateSuperUserRequests() {
        return Stream.of(
                Arguments.of("email1@email.com", "password", new UpdateSuperUserRequest("", "", ""), 4, 400),
                Arguments.of("email1@email.com", "password", new UpdateSuperUserRequest("a", "b", "c@gmail.com"), 5, 403),
                Arguments.of("email1@email.com", "password", new UpdateSuperUserRequest("a", "b", "email2@email.com"), 4, 400)
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideInvalidUpdateSalaryRequests() {
        return Stream.of(
                Arguments.of("email1@email.com", "password", 4, new UpdateSalaryRequest(BigDecimal.ONE), 403),
                Arguments.of("email2@email.com", "password", 4, new UpdateSalaryRequest(BigDecimal.valueOf(-12L)), 400),
                Arguments.of("email2@email.com", "password", 6, new UpdateSalaryRequest(BigDecimal.ONE), 400),
                Arguments.of("email2@email.com", "password", 1, new UpdateSalaryRequest(BigDecimal.ONE), 404)
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideInvalidDeleteManagerRequests() {
        return Stream.of(
                Arguments.of("email1@email.com", "password", 5, 403),
                Arguments.of("email2@email.com", "password", 1, 404),
                Arguments.of("email2@email.com", "password", 6, 404)
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideQueryParameters() {
        var pageable = PageRequest.of(0, 10, Sort.unsorted());
        return Stream.of(
                Arguments.of("", null, null, null, pageable, 3),
                Arguments.of("iKO", null, null, null, pageable, 1),
                Arguments.of("iKO", null, BigDecimal.valueOf(500), null, pageable, 1),
                Arguments.of("iKO", null, null, SuperUserType.MANAGER, pageable, 0),
                Arguments.of("", BigDecimal.valueOf(200), BigDecimal.valueOf(600), null, pageable, 2)
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideValidLoginParams(){
        return Stream.of(
                Arguments.of("email1@email.com", "password"),
                Arguments.of("email2@email.com", "password")
        );
    }


    @SuppressWarnings("unused")
    private static Stream<Arguments> provideInvalidLoginParams(){
        return Stream.of(
                Arguments.of("someemail@email.com", "password", 404),
                Arguments.of("email1@email.com", "some-random-password", 401)
        );
    }
}
