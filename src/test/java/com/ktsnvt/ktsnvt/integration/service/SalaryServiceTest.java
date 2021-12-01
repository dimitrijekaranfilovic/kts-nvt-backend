package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.UserHasNoActiveSalaryException;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.service.impl.SalaryServiceImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class SalaryServiceTest {
    @Autowired
    private SalaryServiceImpl salaryService;

    @ParameterizedTest
    @MethodSource("provideUserIdsWithActiveSalary")
    void endActiveSalaryForUser_calledWithUserWithActiveSalary_isSuccess(Integer userId) {
        var user = new Employee(); user.setId(userId);
        assertDoesNotThrow(() -> salaryService.endActiveSalaryForUser(user));
    }

    @ParameterizedTest
    @MethodSource("provideUserIdsWithNoActiveSalary")
    void endActiveSalaryForUser_calledWithUserWithNoActiveSalary_throwsException(Integer userId) {
        var user = new Employee(); user.setId(userId);
        assertThrows(UserHasNoActiveSalaryException.class, () -> salaryService.endActiveSalaryForUser(user));
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideUserIdsWithActiveSalary() {
        return Stream.of(
                Arguments.of(1), Arguments.of(2), Arguments.of(3)
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideUserIdsWithNoActiveSalary() {
        return Stream.of(
                Arguments.of(6), Arguments.of(7), Arguments.of(8)
        );
    }
}
