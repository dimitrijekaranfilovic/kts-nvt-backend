package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.EmployeeNotFoundException;
import com.ktsnvt.ktsnvt.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Test
    public void read_calledWithValidId_Success(){
        Assertions.assertEquals(employeeService.read(1).getId(), 1);
    }

    @Test
    public void read_calledWithInvalidId_ThrowsEmployeeNotFoundException(){
        Assertions.assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.read(999);
        });
    }
}
