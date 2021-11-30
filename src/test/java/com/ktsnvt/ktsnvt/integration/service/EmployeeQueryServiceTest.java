package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.EmployeeNotFoundException;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.service.impl.EmployeeQueryServiceImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

@SpringBootTest
class EmployeeQueryServiceTest {
    @Autowired
    private EmployeeQueryServiceImpl employeeQueryService;

    @ParameterizedTest
    @MethodSource("provideExistingPins")
    void findByPin_whenCalledWithExistingPin_isSuccess(String pin) {
        var employee = employeeQueryService.findByPin(pin);
        assertEquals(employee.getPin(), pin);
    }

    @ParameterizedTest
    @MethodSource("provideNonExistentPins")
    void findByPin_whenCalledWithNonExistingPin_throwsException(String pin) {
        assertThrows(EmployeeNotFoundException.class, () -> employeeQueryService.findByPin(pin));
    }

    private static Stream<Arguments> provideExistingPins() {
        return Stream.of(
                Arguments.of("1234"),
                Arguments.of("5678"),
                Arguments.of("4321")
        );
    }

    private static Stream<Arguments> provideNonExistentPins() {
        return Stream.of(
                Arguments.of("4567"),
                Arguments.of("9874"),
                Arguments.of("9512")
        );
    }

    private static Stream<Arguments> provideExistingPinsWithEmployeeType() {
        return Stream.of(
                Arguments.of("1234", EmployeeType.CHEF),
                Arguments.of("5678", EmployeeType.BARTENDER),
                Arguments.of("4321", EmployeeType.WAITER)
        );
    }
}