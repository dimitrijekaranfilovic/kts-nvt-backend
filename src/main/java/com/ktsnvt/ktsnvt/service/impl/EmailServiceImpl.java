package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendSimpleMessage(String to, String subject, String text) {
        var message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    @Async
    public void sendMonthlyFinancialReport(SuperUser user, BigDecimal totalSalaryExpense,
                                           BigDecimal totalOrderIncome, BigDecimal totalOrderCost) {
        var to = user.getEmail();
        var subject = "Monthly financial report";
        var sb = new StringBuilder();
        sb.append("Hello ");
        sb.append(user.getName());
        sb.append(" ");
        sb.append(user.getSurname()).append(",\n");
        sb.append("\nHere is your monthly financial report\n");
        sb.append("\nTotal salary expenses: ").append(totalSalaryExpense).append("\n");
        sb.append("\nTotal orders income: ").append(totalOrderIncome).append("\n");
        sb.append("\nTotal orders cost: ").append(totalOrderCost).append("\n");
        var totalProfit = totalOrderIncome.subtract(totalOrderCost).subtract(totalSalaryExpense);
        if (totalProfit.compareTo(BigDecimal.ZERO) >= 0) {
            sb.append("\nTotal profit: ");
        } else {
            sb.append("\nTotal loss: ");
        }
        sb.append(totalProfit).append("\n");

        sendSimpleMessage(to, subject, sb.toString());
    }
}
