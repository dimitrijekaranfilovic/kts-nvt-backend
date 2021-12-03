package com.ktsnvt.ktsnvt.integration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktsnvt.ktsnvt.dto.createemployee.CreateEmployeeRequest;
import com.ktsnvt.ktsnvt.dto.updateemployee.UpdateEmployeeRequest;
import com.ktsnvt.ktsnvt.dto.updatesalary.UpdateSalaryRequest;
import com.ktsnvt.ktsnvt.exception.IllegalEmployeeTypeChangeException;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @BeforeEach
    public void loginAsSuperUser() throws Exception {
        login("email1@email.com", "password");
    }

    @ParameterizedTest
    @MethodSource("provideValidUpdateRequests")
    void update_whenCalledWithValidData_isSuccess(UpdateEmployeeRequest request, Integer id) throws Exception {
        mockMvc.perform(put("/api/employees/{id}", id)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk()
                );
    }

    @Test
    void update_whenCalledWithDuplicatePin_isBadRequest() throws Exception {
        var id = 1;
        var request = new UpdateEmployeeRequest("pera", "peric", "4321", EmployeeType.WAITER);
        mockMvc.perform(put("/api/employees/{id}", id)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    void update_whenCalledWithWaiterWithAssignedOrders_isBadRequest() throws Exception {
        var id = 3;
        var request = new UpdateEmployeeRequest( "pera", "peric", "4321", EmployeeType.BARTENDER);
        mockMvc.perform(put("/api/employees/{id}", id)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    void update_whenCalledWithNotWaiterWithActiveOrderItems_isBadRequest() throws Exception {
        var id = 1;
        var request = new UpdateEmployeeRequest("pera", "peric", "1234", EmployeeType.WAITER);
        mockMvc.perform(put("/api/employees/{id}", id)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 100})
    void update_whenCalledWithNonExistingId_isNotFound(Integer id) throws Exception {
        var request = new UpdateEmployeeRequest("pera", "peric", "9999", EmployeeType.WAITER);
        mockMvc.perform(put("/api/employees/{id}", id)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void updateSalary_whenCalledWithValidData_isSuccess() throws Exception {
        var id = 1;
        var request = new UpdateSalaryRequest(BigDecimal.valueOf(789L));
        mockMvc.perform(put("/api/employees/{id}/salary", id)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isOk()
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 100})
    void updateSalary_whenCalledWithInvalidData_isNotFound(Integer id) throws Exception {
        var request = new UpdateSalaryRequest(BigDecimal.valueOf(789L));
        mockMvc.perform(put("/api/employees/{id}/salary", id)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void createEmployee_whenCalledWithValidData_isSuccess() throws Exception {
        var pin = "9999";
        var request = new CreateEmployeeRequest(pin, "pera", "peric", BigDecimal.valueOf(123L), EmployeeType.WAITER);
        mockMvc.perform(post("/api/employees")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id", notNullValue()),
                        jsonPath("$.id", greaterThanOrEqualTo(0)),
                        jsonPath("$.pin", equalTo(pin))
                );
    }

    @Test
    void createEmployee_whenCalledWithDuplicatePin_isBadRequest() throws Exception {
        var request = new CreateEmployeeRequest("1234", "pera", "peric", BigDecimal.valueOf(123L), EmployeeType.WAITER);
        mockMvc.perform(post("/api/employees")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    void deleteEmployee_whenCalledWithValidId_isSuccess() throws Exception {
        var id = 2;
        mockMvc.perform(delete("/api/employees/{id}", id)
                .header("Authorization", "Bearer " + token))
                .andExpectAll(
                        status().isNoContent()
                );
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 100})
    void deleteEmployee_whenCalledWithNonExistingId_isNotFound(Integer id) throws Exception {
        mockMvc.perform(delete("/api/employees/{id}", id)
                .header("Authorization", "Bearer " + token))
                .andExpectAll(
                        status().isNotFound()
                );
    }

    @Test
    void deleteEmployee_whenCalledWithEmployeeWithAssignedOrders_isBadRequest() throws Exception {
        var id = 3;
        mockMvc.perform(delete("/api/employees/{id}", id)
                .header("Authorization", "Bearer " + token))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @Test
    void deleteEmployee_whenCalledWithEmployeeWithActiveOrderItems_isBadRequest() throws Exception {
        var id = 1;
        mockMvc.perform(delete("/api/employees/{id}", id)
                .header("Authorization", "Bearer " + token))
                .andExpectAll(
                        status().isBadRequest()
                );
    }

    @ParameterizedTest
    @MethodSource("provideQueryParamsForPaginatedRead")
    void readEmployees_whenCalledWithValidData_isSuccess(String query, BigDecimal from, BigDecimal to, EmployeeType type, Pageable pageable, int expected) throws Exception {
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
    private static Stream<Arguments> provideValidUpdateRequests() {
        return Stream.of(
                Arguments.of(new UpdateEmployeeRequest("pera", "peric", "5678", EmployeeType.WAITER), 2),
                Arguments.of(new UpdateEmployeeRequest("pera", "peric", "5678", EmployeeType.BARTENDER), 2),
                Arguments.of(new UpdateEmployeeRequest("pera", "peric", "5678", EmployeeType.CHEF), 2),
                Arguments.of(new UpdateEmployeeRequest("pera", "peric", "9999", EmployeeType.BARTENDER), 2),
                Arguments.of(new UpdateEmployeeRequest("pera", "peric", "1234", EmployeeType.CHEF), 1),
                Arguments.of(new UpdateEmployeeRequest("pera", "peric", "4321", EmployeeType.WAITER), 3)
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
