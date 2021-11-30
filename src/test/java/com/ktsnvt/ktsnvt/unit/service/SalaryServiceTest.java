package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.Salary;
import com.ktsnvt.ktsnvt.repository.SalaryRepository;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.service.SalaryService;
import com.ktsnvt.ktsnvt.service.UserService;
import com.ktsnvt.ktsnvt.service.impl.SalaryServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class SalaryServiceTest {
    @Mock
    private SalaryRepository salaryRepository;
    @Mock
    private UserService userService;
    @Mock
    private LocalDateTimeService localDateTimeService;
    @InjectMocks
    private SalaryServiceImpl salaryService;

    @Test
    void endActiveSalaryForUser_calledWithUserWithActiveSalary_isSuccess() {
        // GIVEN
        var user = new Employee();
        user.setId(999);
        var currentSalary = new Salary(LocalDate.of(2021, 11, 14), null, BigDecimal.valueOf(123), user);
        user.addSalary(currentSalary);
        var endDate = LocalDate.of(2021, 11, 29);
        doReturn(endDate).when(localDateTimeService).currentDate();
        doReturn(Optional.of(currentSalary)).when(salaryRepository).findActiveForUser(user.getId());

        // WHEN
        salaryService.endActiveSalaryForUser(user);

        // THEN
        assertEquals(endDate, currentSalary.getEndDate());
        verify(salaryRepository, times(1)).findActiveForUser(user.getId());
    }

    @Test
    void endActiveSalaryForUser_calledWithUserWithNoActiveSalary_isSuccess() {
        // GIVEN
        var user = new Employee();
        user.setId(999);
        doReturn(Optional.empty()).when(salaryRepository).findActiveForUser(user.getId());

        // WHEN
        salaryService.endActiveSalaryForUser(user);

        // THEN
        verify(salaryRepository, times(1)).findActiveForUser(user.getId());
    }

    @Test
    void updateUserSalary_calledWithValidData_isSuccess() {
        // GIVEN
        var userForSalaryUpdate = new Employee();
        userForSalaryUpdate.setId(999);
        var currentSalary = new Salary(LocalDate.of(2021, 11, 14), null, BigDecimal.valueOf(123), userForSalaryUpdate);
        userForSalaryUpdate.addSalary(currentSalary);
        var newSalary = new Salary(LocalDate.of(2021, 11, 29), null, BigDecimal.valueOf(456), userForSalaryUpdate);
        SalaryService salaryServiceSpy = spy(salaryService);
        doReturn(userForSalaryUpdate).when(userService).readForSalaryUpdate(userForSalaryUpdate.getId());
        doNothing().when(salaryServiceSpy).endActiveSalaryForUser(userForSalaryUpdate);

        // WHEN
        salaryServiceSpy.updateUserSalary(userForSalaryUpdate.getId(), newSalary);

        // THEN
        assertEquals(2, userForSalaryUpdate.getSalaries().size());
        assertEquals(newSalary.getAmount(), userForSalaryUpdate.getCurrentSalary());
        verify(salaryServiceSpy, times(1)).endActiveSalaryForUser(userForSalaryUpdate);
        verify(userService, times(1)).readForSalaryUpdate(userForSalaryUpdate.getId());
    }

}
