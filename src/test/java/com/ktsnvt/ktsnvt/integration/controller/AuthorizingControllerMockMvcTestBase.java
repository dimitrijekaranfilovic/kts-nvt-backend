package com.ktsnvt.ktsnvt.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktsnvt.ktsnvt.dto.auth.AuthRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public abstract class AuthorizingControllerMockMvcTestBase {
    protected WebApplicationContext webApplicationContext;
    protected MockMvc mockMvc;
    protected String token;
    protected ObjectMapper mapper;

    protected AuthorizingControllerMockMvcTestBase(WebApplicationContext webApplicationContext, ObjectMapper mapper) {
        this.webApplicationContext = webApplicationContext;
        this.mapper = mapper;
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    public void clearToken() {
        logout();
    }

    protected void login(String email, String password) throws Exception {
        var request = new AuthRequest(email, password);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/super-users/authenticate")
                                    .contentType("application/json")
                                    .content(mapper.writeValueAsString(request)))
                                    .andExpect(status().isOk()).andReturn();
        var response = result.getResponse().getContentAsString();
        response = response.replace("{\"access_token\": \"", "");
        token = response.replace("\"}", "");
    }

    protected void logout() {
        token = "";
    }

}
