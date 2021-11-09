package com.ktsnvt.ktsnvt.support.createrestauranttable;

import com.ktsnvt.ktsnvt.dto.createrestauranttable.CreateRestaurantTableResponse;
import com.ktsnvt.ktsnvt.model.RestaurantTable;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class RestaurantTableToCreateRestaurantTableResponse extends AbstractConverter<RestaurantTable, CreateRestaurantTableResponse> {
    @Override
    public CreateRestaurantTableResponse convert(@NonNull RestaurantTable response) {
        return getModelMapper().map(response, CreateRestaurantTableResponse.class);
    }
}
