package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.BusyEmployeeDeletionException;
import com.ktsnvt.ktsnvt.exception.EmployeeNotFoundException;
import com.ktsnvt.ktsnvt.exception.IllegalEmployeeTypeChangeException;
import com.ktsnvt.ktsnvt.exception.PinAlreadyExistsException;
import com.ktsnvt.ktsnvt.model.Authority;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.Salary;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Test
    void delete_whenCalledWithValidId_isSuccess() {
        assertDoesNotThrow(() -> employeeService.delete(2));
    }

    @Test
    void delete_whenCalledWithEmployeeWithAssignedOrders_throwsException() {
        assertThrows(BusyEmployeeDeletionException.class, () -> employeeService.delete(3));
    }

    @Test
    void delete_whenCalledWithEmployeeWithActiveOrderItems_throwsException() {
        assertThrows(BusyEmployeeDeletionException.class, () -> employeeService.delete(1));
    }

    @ParameterizedTest
    @EnumSource(EmployeeType.class)
    void create_whenCalledWithValidData_isSuccess(EmployeeType employeeType) {
        var employee = makeEmployee("pera", "peric", "99999999", employeeType, 123L);
        var createdEmployee = employeeService.create(employee);
        assertTrue(createdEmployee.getId() > 0);
        assertEquals(employeeType.toString(), employee.getAuthority().getName());
        assertEquals(1, employee.getSalaries().size());
        assertEquals(0, BigDecimal.valueOf(123L).compareTo(employee.getCurrentSalary()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "5678", "4321"})
    void create_whenCalledWithDuplicatePin_throwsException(String pin) {
        var employee = makeEmployee("pera", "peric", pin, EmployeeType.WAITER, 456L);
        assertThrows(PinAlreadyExistsException.class, () -> employeeService.create(employee));
    }

    @ParameterizedTest
    @MethodSource("provideTestsForValidUpdate")
    void update_whenCalledWithValidData_isSuccess(Integer id, String pin, EmployeeType employeeType) {
        assertDoesNotThrow(() -> employeeService.update(id, "ime", "prezime", pin, employeeType));
    }

    @Test
    void update_whenCalledWithWaiterWithAssignedOrders_throwsException() {
        assertThrows(IllegalEmployeeTypeChangeException.class, () -> employeeService.update(3, "a", "a", "4321", EmployeeType.BARTENDER));
    }

    @Test
    void update_whenCalledWithNotWaiterWithActiveOrderItems_throwsException() {
        assertThrows(IllegalEmployeeTypeChangeException.class, () -> employeeService.update(1, "b", "b", "1234", EmployeeType.WAITER));
    }

    @Test
    void update_whenCalledWithDuplicatePin_throwsException() {
        assertThrows(PinAlreadyExistsException.class, () -> employeeService.update(1, "c", "c", "5678", EmployeeType.CHEF));
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

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideTestsForValidUpdate() {
        return Stream.of(
                Arguments.of(2, "5678", EmployeeType.WAITER),
                Arguments.of(2, "5678", EmployeeType.BARTENDER),
                Arguments.of(2, "5678", EmployeeType.CHEF),
                Arguments.of(2, "9999", EmployeeType.BARTENDER),
                Arguments.of(1, "1234", EmployeeType.CHEF),
                Arguments.of(3, "4321", EmployeeType.WAITER)
        );
    }

    private Employee makeEmployee(String name, String surname, String pinCode, EmployeeType type, long salaryValue) {
        var employee = new Employee(name, surname, null, pinCode, type);
        var salary = new Salary(LocalDate.of(2021, 11, 30), null, BigDecimal.valueOf(salaryValue), employee);
        employee.addSalary(salary);
        return employee;
    }

}
