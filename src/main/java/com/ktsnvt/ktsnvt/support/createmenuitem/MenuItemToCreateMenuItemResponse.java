package com.ktsnvt.ktsnvt.support.createmenuitem;

import com.ktsnvt.ktsnvt.dto.createmenuitem.CreateMenuItemResponse;
import com.ktsnvt.ktsnvt.model.MenuItem;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class MenuItemToCreateMenuItemResponse extends AbstractConverter<MenuItem, CreateMenuItemResponse> {
    @Override
    public CreateMenuItemResponse convert(@NonNull MenuItem source) {
        return getModelMapper().map(source, CreateMenuItemResponse.class);
    }
}
