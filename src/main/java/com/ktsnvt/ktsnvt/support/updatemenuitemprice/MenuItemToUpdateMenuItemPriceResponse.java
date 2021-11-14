package com.ktsnvt.ktsnvt.support.updatemenuitemprice;

import com.ktsnvt.ktsnvt.dto.updatemenuitemprice.UpdateMenuItemPriceResponse;
import com.ktsnvt.ktsnvt.model.MenuItem;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class MenuItemToUpdateMenuItemPriceResponse extends AbstractConverter<MenuItem, UpdateMenuItemPriceResponse> {

    @Override
    public UpdateMenuItemPriceResponse convert(@NonNull MenuItem source) {
        return getModelMapper().map(source, UpdateMenuItemPriceResponse.class);
    }
}
