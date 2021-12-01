package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.UserHasNoActiveSalaryException;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.Salary;
import com.ktsnvt.ktsnvt.model.User;
import com.ktsnvt.ktsnvt.service.impl.SalaryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SalaryServiceTest {
    @Autowired
    private SalaryServiceImpl salaryService;

    @ParameterizedTest
    @MethodSource("provideUsersWithActiveSalary")
    void endActiveSalaryForUser_calledWithUserWithActiveSalary_isSuccess(User user) {
        assertDoesNotThrow(() -> salaryService.endActiveSalaryForUser(user));
    }

    @ParameterizedTest
    @MethodSource("provideUsersWithNoActiveSalary")
    void endActiveSalaryForUser_calledWithUserWithNoActiveSalary_throwsException(User user) {
        assertThrows(UserHasNoActiveSalaryException.class, () -> salaryService.endActiveSalaryForUser(user));
    }

    @ParameterizedTest
    @MethodSource("provideUsersWithActiveSalary")
    void updateUserSalary_whenCalledWithValidUserAndSalary_isSuccess(User user) {
        var salary = new Salary(LocalDate.of(2021, 11, 30), null, BigDecimal.valueOf(123), user);
        assertDoesNotThrow(() -> salaryService.updateUserSalary(user.getId(), salary));
    }

    @Test
    void updateUserSalary_whenCalledWithUserWithNoActiveSalary_throwsException() {
        var user = makeUser(6);
        var salary = new Salary(LocalDate.of(2021, 11, 30), null, BigDecimal.valueOf(123), user);
        assertThrows(UserHasNoActiveSalaryException.class, () -> salaryService.updateUserSalary(user.getId(), salary));
    }

    @ParameterizedTest
    @MethodSource("provideDatesAndExpenses")
    void readExpensesForDate_whenCalledWithValidDate_isSuccess(LocalDate date, BigDecimal expected) {
        assertEquals(0, salaryService.readExpensesForDate(date).compareTo(expected));
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideDatesAndExpenses() {
        return Stream.of(
                Arguments.of(LocalDate.of(2021, 11, 11), BigDecimal.valueOf(1512)),
                Arguments.of(LocalDate.of(2021, 12, 1), BigDecimal.valueOf(2562)),
                Arguments.of(LocalDate.of(2022, 12, 1), BigDecimal.valueOf(2562)),
                Arguments.of(LocalDate.of(2020, 12, 1), BigDecimal.ZERO)
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideUsersWithActiveSalary() {
        return Stream.of(
                Arguments.of(makeUser(1)),
                Arguments.of(makeUser(2)),
                Arguments.of(makeUser(3))
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideUsersWithNoActiveSalary() {
        return Stream.of(
                Arguments.of(makeUser(6)),
                Arguments.of(makeUser(7)),
                Arguments.of(makeUser(8))
        );
    }
    private static User makeUser(Integer id) {
        var user = new Employee();
        user.setId(id);
        return user;
    }

}
