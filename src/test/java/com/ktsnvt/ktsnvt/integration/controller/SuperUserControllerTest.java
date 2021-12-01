package com.ktsnvt.ktsnvt.integration.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktsnvt.ktsnvt.dto.auth.AuthRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SuperUserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;


    @ParameterizedTest
    @MethodSource("provideLoginParams")
    void authenticate_withValidData_isSuccess(String email, String password, int status) throws Exception {
        var authRequest = new AuthRequest(email, password);

        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("http://localhost:%d/api/super-users/authenticate", this.port))
                        .content(mapper.writeValueAsString(authRequest))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().is(status));


    }


    private static Stream<Arguments> provideLoginParams(){
        return Stream.of(
                Arguments.of("email1@email.com", "password", 200),
                Arguments.of("email2@email.com", "password", 200),
                Arguments.of("someemail@email.com", "password", 404)
        );
    }
}
