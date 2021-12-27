package com.ktsnvt.ktsnvt.support.readmenuitems;

import com.ktsnvt.ktsnvt.dto.readmenuitems.ReadMenuItemsResponse;
import com.ktsnvt.ktsnvt.model.MenuItem;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class MenuItemToReadMenuItemsResponse extends AbstractConverter<MenuItem, ReadMenuItemsResponse> {
    @Override
    public ReadMenuItemsResponse convert(@NonNull MenuItem source) {
        return getModelMapper().map(source, ReadMenuItemsResponse.class);
    }
}
