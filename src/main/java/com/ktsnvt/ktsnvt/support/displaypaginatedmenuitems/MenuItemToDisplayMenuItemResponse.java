package com.ktsnvt.ktsnvt.support.displaypaginatedmenuitems;

import com.ktsnvt.ktsnvt.dto.displaypaginatedmenuitems.DisplayMenuItemResponse;
import com.ktsnvt.ktsnvt.model.MenuItem;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;


@Component
public class MenuItemToDisplayMenuItemResponse  extends AbstractConverter<MenuItem, DisplayMenuItemResponse> {
    @Override
    public DisplayMenuItemResponse convert(@NonNull MenuItem source) {
        return getModelMapper().map(source, DisplayMenuItemResponse.class);
    }
}
