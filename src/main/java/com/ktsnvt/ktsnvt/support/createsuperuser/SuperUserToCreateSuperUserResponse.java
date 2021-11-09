package com.ktsnvt.ktsnvt.support.createsuperuser;

import com.ktsnvt.ktsnvt.dto.createesuperuser.CreateSuperUserResponse;
import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SuperUserToCreateSuperUserResponse extends AbstractConverter<SuperUser, CreateSuperUserResponse> {
    @Override
    public CreateSuperUserResponse convert(@NonNull SuperUser source) {
        return getModelMapper().map(source, CreateSuperUserResponse.class);
    }
}
