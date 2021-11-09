package com.ktsnvt.ktsnvt.support.createrestauranttable;

import com.ktsnvt.ktsnvt.dto.createrestauranttable.CreateRestaurantTableRequest;
import com.ktsnvt.ktsnvt.model.RestaurantTable;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class CreateRestaurantTableRequestToRestaurantTable extends AbstractConverter<CreateRestaurantTableRequest, RestaurantTable> {
    @Override
    public RestaurantTable convert(@NonNull CreateRestaurantTableRequest request) {
        return getModelMapper().map(request, RestaurantTable.class);
    }
}
