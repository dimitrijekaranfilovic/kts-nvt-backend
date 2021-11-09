package com.ktsnvt.ktsnvt.support.updatesalary;

import com.ktsnvt.ktsnvt.dto.updatesalary.UpdateSalaryRequest;
import com.ktsnvt.ktsnvt.model.Salary;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateSalaryRequestToSalary extends AbstractConverter<UpdateSalaryRequest, Salary> {
    private final LocalDateTimeService localDateTimeService;

    @Autowired
    public UpdateSalaryRequestToSalary(LocalDateTimeService localDateTimeService) {
        this.localDateTimeService = localDateTimeService;
    }

    @Override
    public Salary convert(@NonNull UpdateSalaryRequest source) {
        return new Salary(localDateTimeService.currentDate(), null, source.getAmount(), null);
    }
}
