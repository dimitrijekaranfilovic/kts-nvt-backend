package com.ktsnvt.ktsnvt.support.createsuperuser;

import com.ktsnvt.ktsnvt.dto.createesuperuser.CreateSuperUserRequest;
import com.ktsnvt.ktsnvt.model.Salary;
import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateSuperUserRequestToSuperUser extends AbstractConverter<CreateSuperUserRequest, SuperUser> {
    private final LocalDateTimeService localDateTimeService;

    @Autowired
    public CreateSuperUserRequestToSuperUser(LocalDateTimeService localDateTimeService) {
        this.localDateTimeService = localDateTimeService;
    }

    @Override
    public SuperUser convert(@NonNull CreateSuperUserRequest request) {
        var superUser = getModelMapper().map(request, SuperUser.class);
        superUser.addSalary(new Salary(
            localDateTimeService.currentDate(), null, request.getSalary(), superUser
        ));
        return superUser;
    }
}
