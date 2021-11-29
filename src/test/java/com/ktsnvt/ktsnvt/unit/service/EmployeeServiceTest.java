package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.exception.PinAlreadyExistsException;
import com.ktsnvt.ktsnvt.model.Authority;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.repository.EmployeeRepository;
import com.ktsnvt.ktsnvt.service.*;
import com.ktsnvt.ktsnvt.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeQueryService employeeQueryService;
    @Mock
    private AuthorityService authorityService;
    @Mock
    private SalaryService salaryService;
    @Mock
    private OrderService orderService;
    @Mock
    private OrderItemService orderItemService;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void create_whenCalledWithValidData_isSuccess() {
        // GIVEN
        var employee = new Employee();
        employee.setPin("9999");
        employee.setType(EmployeeType.WAITER);
        var authority = new Authority(employee.getType().toString());
        doReturn(Optional.empty()).when(employeeQueryService).findByPinUnchecked(employee.getPin());
        doReturn(authority).when(authorityService).findByName(authority.getName());
        doReturn(employee).when(employeeRepository).save(employee);

        // WHEN
        employeeService.create(employee);

        // THEN
        assertEquals(authority, employee.getAuthority());
        verify(employeeRepository, times(1)).save(employee);
        verify(employeeQueryService, times(1)).findByPinUnchecked(employee.getPin());
    }

    @Test
    void create_whenCalledWithPinWhichIsAlreadyTaken_throwsException() {
        // GIVEN
        var employee = new Employee();
        employee.setPin("9999");
        employee.setType(EmployeeType.WAITER);
        var authority = new Authority(employee.getType().toString());
        doReturn(Optional.of(new Employee())).when(employeeQueryService).findByPinUnchecked(employee.getPin());
        doReturn(authority).when(authorityService).findByName(authority.getName());
        doReturn(employee).when(employeeRepository).save(employee);

        // WHEN
        assertThrows(PinAlreadyExistsException.class, () -> employeeService.create(employee));

        // THEN
        verify(employeeQueryService, times(1)).findByPinUnchecked(employee.getPin());
        verifyNoMoreInteractions(authorityService);
        verifyNoMoreInteractions(employeeRepository);
    }

    @Test
    void delete_whenCalledWithValidId_isSuccess() {
        Employee retEmp = new Employee();
        retEmp.setId(1);

        doNothing().when(salaryService).endActiveSalaryForUser(retEmp);

        EmployeeService employeeServiceSpy = spy(employeeService);

        doReturn(retEmp).when(employeeServiceSpy).readForUpdate(retEmp.getId());

        doReturn(false).when(orderService).hasAssignedActiveOrders(retEmp);
        doReturn(false).when(orderItemService).hasActiveOrderItems(retEmp);

        employeeServiceSpy.delete(1);

        assertFalse(retEmp.getIsActive());
    }
}
