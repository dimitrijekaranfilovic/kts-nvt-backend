package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.repository.EmployeeRepository;
import com.ktsnvt.ktsnvt.service.*;
import com.ktsnvt.ktsnvt.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeQueryService employeeQueryService;

    @Mock
    private SalaryService salaryService;

    @Mock
    private OrderService orderService;

    @Mock
    private OrderItemService orderItemService;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    public void setUp() {
        Employee retEmp = new Employee();
        doReturn(Optional.of(retEmp)).when(employeeQueryService).findByPinUnchecked(anyString());
    }

    @Test
    public void test() {
        Assertions.assertEquals(true, true);
    }

    @Test
    public void delete_whenCalledWithValidId_isSuccess(){
        Employee retEmp = new Employee();
        retEmp.setId(1);

        doNothing().when(salaryService).endActiveSalaryForUser(retEmp);

        EmployeeService employeeServiceSpy = spy(employeeService);

        doReturn(retEmp).when(employeeServiceSpy).readForUpdate(retEmp.getId());

        doReturn(false).when(orderService).hasAssignedActiveOrders(retEmp);
        doReturn(false).when(orderItemService).hasActiveOrderItems(retEmp);

        employeeServiceSpy.delete(1);

        Assertions.assertEquals(false, retEmp.getIsActive());
    }
}
