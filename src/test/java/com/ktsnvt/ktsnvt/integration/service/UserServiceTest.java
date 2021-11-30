package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.UserNotFoundException;
import com.ktsnvt.ktsnvt.service.impl.UserServiceImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserServiceImpl userService;

    @ParameterizedTest
    @MethodSource("provideExistingIds")
    void readForSalaryUpdate_whenCalledWithExistingId_isSuccess(Integer id) {
        var user = userService.readForSalaryUpdate(id);
        assertEquals(id, user.getId());
    }

    @ParameterizedTest
    @MethodSource("provideNonExistingIds")
    void readForSalaryUpdate_whenCalledWithNonExistingId_isSuccess(Integer id) {
        assertThrows(UserNotFoundException.class, () -> userService.readForSalaryUpdate(id));
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideExistingIds() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(2),
                Arguments.of(3),
                Arguments.of(4),
                Arguments.of(5)
        );
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideNonExistingIds() {
        return Stream.of(
                Arguments.of(11),
                Arguments.of(12),
                Arguments.of(13),
                Arguments.of(14),
                Arguments.of(15)
        );
    }
}
