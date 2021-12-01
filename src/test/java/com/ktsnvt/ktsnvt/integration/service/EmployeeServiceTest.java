package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.EmployeeNotFoundException;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class EmployeeServiceTest {
    @Autowired
    private EmployeeServiceImpl employeeService;

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void read_whenCalledWithValidId_isSuccess(Integer id) {
        var employee = employeeService.read(id);
        assertEquals(id, employee.getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 5, 6, 7, 8})
    void read_whenCalledWithInvalidId_throwsException(Integer id) {
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.read(id));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void readForUpdate_whenCalledWithValidId_isSuccess(Integer id) {
        var employee = employeeService.readForUpdate(id);
        assertEquals(id, employee.getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 5, 6, 7, 8})
    void readForUpdate_whenCalledWithInvalidId_throwsException(Integer id) {
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.readForUpdate(id));
    }

    @ParameterizedTest
    @MethodSource("provideTestsForPaginatedRead")
    void read_whenCalledWithPagination_isSuccess(String query, BigDecimal salaryFrom, BigDecimal salaryTo, EmployeeType type, Pageable pageable, int expected) {
        var page = employeeService.read(query, salaryFrom, salaryTo, type, pageable);
        assertEquals(expected, page.getTotalElements());
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideTestsForPaginatedRead() {
        var pageable = PageRequest.of(0, 10, Sort.unsorted());
        return Stream.of(
                Arguments.of("", null, null, null, pageable, 3),
                Arguments.of("arK    ", null, null, null, pageable, 2),
                Arguments.of("  arK", null, BigDecimal.valueOf(500), null, pageable, 1),
                Arguments.of("aRk", null, null, EmployeeType.WAITER, pageable, 1),
                Arguments.of("aRk", null, null, EmployeeType.CHEF, pageable, 0),
                Arguments.of("", BigDecimal.valueOf(200), BigDecimal.valueOf(600), null, pageable, 3)
        );
    }

}
