package com.ktsnvt.ktsnvt.support.readsuperusers;

import com.ktsnvt.ktsnvt.dto.readsuperusers.ReadSuperUsersResponse;
import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SuperUserToReadSuperUsersResponse extends AbstractConverter<SuperUser, ReadSuperUsersResponse> {
    @Override
    public ReadSuperUsersResponse convert(@NonNull SuperUser source) {
        return getModelMapper().map(source, ReadSuperUsersResponse.class);
    }
}
