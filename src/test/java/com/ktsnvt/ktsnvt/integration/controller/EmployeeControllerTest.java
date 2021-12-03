package com.ktsnvt.ktsnvt.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class EmployeeControllerTest extends AuthorizingControllerMockMvcTestBase {

    @Autowired
    protected EmployeeControllerTest(WebApplicationContext webApplicationContext, ObjectMapper mapper) {
        super(webApplicationContext, mapper);
    }

    @ParameterizedTest
    @MethodSource("provideQueryParamsForPaginatedRead")
    void readEmployees_whenCalledWithValidData_isSuccess(String query, BigDecimal from, BigDecimal to, EmployeeType type, Pageable pageable, int expected) throws Exception {
        login("email1@email.com", "password");
        mockMvc.perform(get("/api/employees")
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

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideQueryParamsForPaginatedRead() {
        var pageable = PageRequest.of(0, 10);
        return Stream.of(
                Arguments.of("", null, null, null, pageable, 4),
                Arguments.of("arK    ", null, null, null, pageable, 2),
                Arguments.of("  arK", null, BigDecimal.valueOf(500), null, pageable, 1),
                Arguments.of("aRk", null, null, EmployeeType.WAITER, pageable, 1),
                Arguments.of("aRk", null, null, EmployeeType.CHEF, pageable, 0),
                Arguments.of("", BigDecimal.valueOf(200), BigDecimal.valueOf(600), null, pageable, 3)
        );
    }
}
