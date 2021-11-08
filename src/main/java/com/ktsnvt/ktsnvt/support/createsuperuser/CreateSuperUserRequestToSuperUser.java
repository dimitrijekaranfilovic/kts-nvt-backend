package com.ktsnvt.ktsnvt.support.createsuperuser;

import com.ktsnvt.ktsnvt.dto.createesuperuser.CreateSuperUserRequest;
import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CreateSuperUserRequestToSuperUser extends AbstractConverter<CreateSuperUserRequest, SuperUser> {
    @Override
    public SuperUser convert(@NonNull CreateSuperUserRequest request) {
        return getModelMapper().map(request, SuperUser.class);
    }
}
