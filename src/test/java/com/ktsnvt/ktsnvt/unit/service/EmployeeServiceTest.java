package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.exception.BusyEmployeeDeletionException;
import com.ktsnvt.ktsnvt.exception.IllegalEmployeeTypeChangeException;
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
        var createdEmployee = new Employee();
        createdEmployee.setPin("9999");
        createdEmployee.setType(EmployeeType.WAITER);
        var authority = new Authority(createdEmployee.getType().toString());
        doReturn(Optional.empty()).when(employeeQueryService).findByPinUnchecked(createdEmployee.getPin());
        doReturn(authority).when(authorityService).findByName(authority.getName());
        doReturn(createdEmployee).when(employeeRepository).save(createdEmployee);

        // WHEN
        employeeService.create(createdEmployee);

        // THEN
        assertEquals(authority, createdEmployee.getAuthority());
        verify(employeeRepository, times(1)).save(createdEmployee);
        verify(employeeQueryService, times(1)).findByPinUnchecked(createdEmployee.getPin());
    }

    @Test
    void create_whenCalledWithPinWhichIsAlreadyTaken_throwsException() {
        // GIVEN
        var createdEmployee = new Employee();
        createdEmployee.setPin("9999");
        createdEmployee.setType(EmployeeType.WAITER);
        var authority = new Authority(createdEmployee.getType().toString());
        doReturn(Optional.of(new Employee())).when(employeeQueryService).findByPinUnchecked(createdEmployee.getPin());
        doReturn(authority).when(authorityService).findByName(authority.getName());
        doReturn(createdEmployee).when(employeeRepository).save(createdEmployee);

        // WHEN
        assertThrows(PinAlreadyExistsException.class, () -> employeeService.create(createdEmployee));

        // THEN
        verify(employeeQueryService, times(1)).findByPinUnchecked(createdEmployee.getPin());
        verifyNoInteractions(authorityService);
        verifyNoInteractions(employeeRepository);
    }

    @Test
    void delete_whenCalledWithEmployeeWithNoPendingItems_isSuccess() {
        // GIVEN
        Employee deletedEmployee = new Employee();
        deletedEmployee.setId(999);
        doNothing().when(salaryService).endActiveSalaryForUser(deletedEmployee);
        EmployeeService employeeServiceSpy = spy(employeeService);
        doReturn(deletedEmployee).when(employeeServiceSpy).readForUpdate(deletedEmployee.getId());
        doReturn(false).when(orderService).hasAssignedActiveOrders(deletedEmployee);
        doReturn(false).when(orderItemService).hasActiveOrderItems(deletedEmployee);

        // WHEN
        employeeServiceSpy.delete(deletedEmployee.getId());

        // THEN
        assertFalse(deletedEmployee.getIsActive());
        verify(salaryService, times(1)).endActiveSalaryForUser(deletedEmployee);
        verify(orderService, times(1)).hasAssignedActiveOrders(deletedEmployee);
        verify(orderItemService, times(1)).hasActiveOrderItems(deletedEmployee);
        verifyNoInteractions(employeeRepository);
    }

    @Test
    void delete_whenCalledWithEmployeeWithAssignedActiveOrders_throwsException() {
        // GIVEN
        var deletedEmployeeId = 999;
        Employee deletedEmployee = new Employee();
        deletedEmployee.setId(deletedEmployeeId);
        doNothing().when(salaryService).endActiveSalaryForUser(deletedEmployee);
        EmployeeService employeeServiceSpy = spy(employeeService);
        doReturn(deletedEmployee).when(employeeServiceSpy).readForUpdate(deletedEmployee.getId());
        doReturn(true).when(orderService).hasAssignedActiveOrders(deletedEmployee);
        doReturn(false).when(orderItemService).hasActiveOrderItems(deletedEmployee);

        // WHEN
        assertThrows(BusyEmployeeDeletionException.class, () -> employeeServiceSpy.delete(deletedEmployeeId));

        // THEN
        assertTrue(deletedEmployee.getIsActive());
        verify(orderService, times(1)).hasAssignedActiveOrders(deletedEmployee);
        verifyNoInteractions(salaryService);
        verifyNoInteractions(orderItemService);
        verifyNoInteractions(employeeRepository);
    }

    @Test
    void delete_whenCalledWithEmployeeWithAssignedActiveOrderItems_throwsException() {
        // GIVEN
        var deletedEmployeeId = 999;
        Employee deletedEmployee = new Employee();
        deletedEmployee.setId(deletedEmployeeId);
        doNothing().when(salaryService).endActiveSalaryForUser(deletedEmployee);
        EmployeeService employeeServiceSpy = spy(employeeService);
        doReturn(deletedEmployee).when(employeeServiceSpy).readForUpdate(deletedEmployee.getId());
        doReturn(false).when(orderService).hasAssignedActiveOrders(deletedEmployee);
        doReturn(true).when(orderItemService).hasActiveOrderItems(deletedEmployee);

        // WHEN
        assertThrows(BusyEmployeeDeletionException.class, () -> employeeServiceSpy.delete(deletedEmployeeId));

        // THEN
        assertTrue(deletedEmployee.getIsActive());
        verify(orderService, times(1)).hasAssignedActiveOrders(deletedEmployee);
        verify(orderItemService, times(1)).hasActiveOrderItems(deletedEmployee);
        verifyNoInteractions(salaryService);
        verifyNoInteractions(employeeRepository);
    }

    @Test
    void update_whenCalledWithValidChangesAndNoTypeUpdate_isSuccess() {
        // GIVEN
        var updatedEmployee = new Employee("name", "surname", new Authority("WAITER"), "9999", EmployeeType.WAITER);
        updatedEmployee.setId(999);
        EmployeeService employeeServiceSpy = spy(employeeService);
        doReturn(updatedEmployee).when(employeeServiceSpy).readForUpdate(updatedEmployee.getId());
        doReturn(Optional.of(updatedEmployee)).when(employeeQueryService).findByPinUnchecked(updatedEmployee.getPin());
        doReturn(false).when(orderService).hasAssignedActiveOrders(updatedEmployee);
        doReturn(false).when(orderItemService).hasActiveOrderItems(updatedEmployee);
        doReturn(updatedEmployee.getAuthority()).when(authorityService).findByName(updatedEmployee.getType().toString());

        // WHEN
        employeeServiceSpy.update(updatedEmployee.getId(), "Dusan", updatedEmployee.getSurname(), updatedEmployee.getPin(), updatedEmployee.getType());

        // THEN
        assertEquals("Dusan", updatedEmployee.getName());
        verify(authorityService, times(1)).findByName(updatedEmployee.getType().toString());
        verifyNoInteractions(orderService);
        verifyNoInteractions(orderItemService);
        verifyNoInteractions(employeeRepository);
    }

    @Test
    void update_whenCalledWithValidChangesAndValidTypeFromWaiter_isSuccess() {
        // GIVEN
        var updatedEmployee = new Employee("name", "surname", new Authority("WAITER"), "9999", EmployeeType.WAITER);
        updatedEmployee.setId(999);
        var chefAuthority = new Authority("CHEF");
        EmployeeService employeeServiceSpy = spy(employeeService);
        doReturn(updatedEmployee).when(employeeServiceSpy).readForUpdate(updatedEmployee.getId());
        doReturn(Optional.of(updatedEmployee)).when(employeeQueryService).findByPinUnchecked(updatedEmployee.getPin());
        doReturn(false).when(orderService).hasAssignedActiveOrders(updatedEmployee);
        doReturn(false).when(orderItemService).hasActiveOrderItems(updatedEmployee);
        doReturn(chefAuthority).when(authorityService).findByName(EmployeeType.CHEF.toString());

        // WHEN
        employeeServiceSpy.update(updatedEmployee.getId(), "Dusan", updatedEmployee.getSurname(), updatedEmployee.getPin(), EmployeeType.CHEF);

        // THEN
        assertEquals("Dusan", updatedEmployee.getName());
        assertEquals(EmployeeType.CHEF, updatedEmployee.getType());
        assertEquals(chefAuthority, updatedEmployee.getAuthority());
        verify(authorityService, times(1)).findByName(chefAuthority.getName());
        verify(orderService, times(1)).hasAssignedActiveOrders(updatedEmployee);
        verifyNoInteractions(orderItemService);
        verifyNoInteractions(employeeRepository);
    }

    @Test
    void update_whenCalledWithValidChangesAndValidTypeChangeNotFromWaiter_isSuccess() {
        // GIVEN
        var updatedEmployee = new Employee("name", "surname", new Authority("CHEF"), "9999", EmployeeType.CHEF);
        updatedEmployee.setId(999);
        var waiterAuthority = new Authority("WAITER");
        EmployeeService employeeServiceSpy = spy(employeeService);
        doReturn(updatedEmployee).when(employeeServiceSpy).readForUpdate(updatedEmployee.getId());
        doReturn(Optional.of(updatedEmployee)).when(employeeQueryService).findByPinUnchecked(updatedEmployee.getPin());
        doReturn(false).when(orderService).hasAssignedActiveOrders(updatedEmployee);
        doReturn(false).when(orderItemService).hasActiveOrderItems(updatedEmployee);
        doReturn(waiterAuthority).when(authorityService).findByName(EmployeeType.WAITER.toString());

        // WHEN
        employeeServiceSpy.update(updatedEmployee.getId(), "Dusan", updatedEmployee.getSurname(), updatedEmployee.getPin(), EmployeeType.WAITER);

        // THEN
        assertEquals("Dusan", updatedEmployee.getName());
        assertEquals(EmployeeType.WAITER, updatedEmployee.getType());
        assertEquals(waiterAuthority, updatedEmployee.getAuthority());
        verify(authorityService, times(1)).findByName(waiterAuthority.getName());
        verify(orderItemService, times(1)).hasActiveOrderItems(updatedEmployee);
        verifyNoInteractions(orderService);
        verifyNoInteractions(employeeRepository);
    }

    @Test
    void update_whenCalledWithTakenPin_throwsException() {
        // GIVEN
        var updatedEmployeeId = 999;
        var updatedEmployeeSurname = "surname";
        var updatedEmployeeType = EmployeeType.WAITER;
        var updatedEmployee = new Employee("name", updatedEmployeeSurname, new Authority("WAITER"), "9999", updatedEmployeeType);
        updatedEmployee.setId(updatedEmployeeId);
        var updatedPin = "8888";
        var employeeWithSamePin = new Employee();
        employeeWithSamePin.setId(123);
        EmployeeService employeeServiceSpy = spy(employeeService);
        doReturn(updatedEmployee).when(employeeServiceSpy).readForUpdate(updatedEmployee.getId());
        doReturn(Optional.of(employeeWithSamePin)).when(employeeQueryService).findByPinUnchecked(updatedPin);
        doReturn(false).when(orderService).hasAssignedActiveOrders(updatedEmployee);
        doReturn(false).when(orderItemService).hasActiveOrderItems(updatedEmployee);
        doReturn(updatedEmployee.getAuthority()).when(authorityService).findByName(updatedEmployee.getType().toString());

        // WHEN
        assertThrows(PinAlreadyExistsException.class, () -> employeeServiceSpy.update(updatedEmployeeId, "Dusan", updatedEmployeeSurname, updatedPin, updatedEmployeeType));

        // THEN
        verifyNoInteractions(authorityService);
        verifyNoInteractions(orderService);
        verifyNoInteractions(orderItemService);
        verifyNoInteractions(employeeRepository);
    }

    @Test
    void update_whenCalledWithTypeChangeFromWaiterWithAssignedOrders_throwsException() {
        // GIVEN
        var updatedEmployeeId = 999;
        var updatedEmployeeSurname = "surname";
        var updatedEmployeePin = "9999";
        var updatedEmployee = new Employee("name", updatedEmployeeSurname, new Authority("WAITER"), updatedEmployeePin, EmployeeType.WAITER);
        updatedEmployee.setId(updatedEmployeeId);
        var chefAuthority = new Authority("CHEF");
        EmployeeService employeeServiceSpy = spy(employeeService);
        doReturn(updatedEmployee).when(employeeServiceSpy).readForUpdate(updatedEmployee.getId());
        doReturn(Optional.of(updatedEmployee)).when(employeeQueryService).findByPinUnchecked(updatedEmployee.getPin());
        doReturn(true).when(orderService).hasAssignedActiveOrders(updatedEmployee);
        doReturn(false).when(orderItemService).hasActiveOrderItems(updatedEmployee);
        doReturn(chefAuthority).when(authorityService).findByName(EmployeeType.CHEF.toString());

        // WHEN
        assertThrows(IllegalEmployeeTypeChangeException.class, () -> employeeServiceSpy.update(updatedEmployeeId, "Dusan", updatedEmployeeSurname, updatedEmployeePin, EmployeeType.CHEF));

        // THEN
        verify(orderService, times(1)).hasAssignedActiveOrders(updatedEmployee);
        verifyNoInteractions(authorityService);
        verifyNoInteractions(orderItemService);
        verifyNoInteractions(employeeRepository);
    }

    @Test
    void update_whenCalledWithTypeChangeFromNotWaiterWithActiveOrderItems_throwsException() {
        // GIVEN
        var updatedEmployeeId = 999;
        var updatedEmployeeSurname = "surname";
        var updatedEmployeePin = "9999";
        var updatedEmployee = new Employee("name", updatedEmployeeSurname, new Authority("CHEF"), updatedEmployeePin, EmployeeType.CHEF);
        updatedEmployee.setId(updatedEmployeeId);
        var waiterAuthority = new Authority("WAITER");
        EmployeeService employeeServiceSpy = spy(employeeService);
        doReturn(updatedEmployee).when(employeeServiceSpy).readForUpdate(updatedEmployee.getId());
        doReturn(Optional.of(updatedEmployee)).when(employeeQueryService).findByPinUnchecked(updatedEmployee.getPin());
        doReturn(false).when(orderService).hasAssignedActiveOrders(updatedEmployee);
        doReturn(true).when(orderItemService).hasActiveOrderItems(updatedEmployee);
        doReturn(waiterAuthority).when(authorityService).findByName(EmployeeType.WAITER.toString());

        // WHEN
        assertThrows(IllegalEmployeeTypeChangeException.class, () -> employeeServiceSpy.update(updatedEmployeeId, "Dusan", updatedEmployeeSurname, updatedEmployeePin, EmployeeType.WAITER));

        // THEN
        verify(orderItemService, times(1)).hasActiveOrderItems(updatedEmployee);
        verifyNoInteractions(authorityService);
        verifyNoInteractions(orderService);
        verifyNoInteractions(employeeRepository);
    }
}
