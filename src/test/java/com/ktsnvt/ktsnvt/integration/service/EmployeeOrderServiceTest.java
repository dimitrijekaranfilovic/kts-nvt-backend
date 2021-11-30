package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.InvalidEmployeeTypeException;
import com.ktsnvt.ktsnvt.service.impl.EmployeeOrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeeOrderServiceTest {
    @Autowired
    private EmployeeOrderServiceImpl employeeOrderService;

    @Test
    void throwIfWaiterNotResponsible_whenCalledWithValidWaiter_isSuccess() {
        assertDoesNotThrow(() -> employeeOrderService.throwIfWaiterNotResponsible("4321", 3));
    }

    @Test
    void throwIfWaiterNotResponsible_whenCalledWithNotWaiter_throwsException() {
        assertThrows(InvalidEmployeeTypeException.class, () -> employeeOrderService.throwIfWaiterNotResponsible("5678", 2));
    }

    @Test
    void throwIfWaiterNotResponsible_whenCalledWithInvalidWaiter_throwsException() {
        assertThrows(InvalidEmployeeTypeException.class, () -> employeeOrderService.throwIfWaiterNotResponsible("4321", 2));
    }
}
