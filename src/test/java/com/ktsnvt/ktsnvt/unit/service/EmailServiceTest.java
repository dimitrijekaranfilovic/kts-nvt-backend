package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@SpringBootTest
class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Test
    void sendSimpleMessage_calledWithValidData_isSuccess() {
        // GIVEN
        var paramTo = "param to";
        var paramSubject = "param subject";
        var paramText = "param text";
        var newMessage = new SimpleMailMessage();
        newMessage.setTo(paramTo);
        newMessage.setSubject(paramSubject);
        newMessage.setText(paramText);
        doNothing().when(javaMailSender).send(newMessage);

        // WHEN
        emailService.sendSimpleMessage(paramTo, paramSubject, paramText);

        // THEN
        verify(javaMailSender, times(1)).send(newMessage);
    }

    @Test
    void sendMonthlyFinancialReport_calledWithValidData_isSuccess() {
        // GIVEN
        var paramSuperUser = new SuperUser();
        paramSuperUser.setEmail("SU email");
        paramSuperUser.setName("SU name");
        paramSuperUser.setSurname("SU surname");
        var paramTotalSalaryExpense = new BigDecimal(42);
        var paramTotalOrderIncome = new BigDecimal("3.14");
        var paramTotalOrderCost = new BigDecimal(322);
        var subject = "Monthly financial report";
        var emailServiceSpy = spy(emailService);
        doNothing().when(emailServiceSpy).sendSimpleMessage(eq(paramSuperUser.getEmail()), eq(subject), anyString());

        // WHEN
        emailServiceSpy.sendMonthlyFinancialReport(paramSuperUser, paramTotalSalaryExpense,
                paramTotalOrderIncome, paramTotalOrderCost);

        // THEN
        verify(emailServiceSpy, times(1)).sendSimpleMessage(eq(paramSuperUser.getEmail()),
                eq(subject), anyString());
    }
}
