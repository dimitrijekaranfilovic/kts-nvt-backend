package com.ktsnvt.ktsnvt.support.readFoodAndDrinkRequest;

import com.ktsnvt.ktsnvt.dto.readFoodAndDrinkRequests.ReadFoodAndDrinkRequestResponse;
import com.ktsnvt.ktsnvt.model.OrderItem;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class OrderItemToReadFoodAndDrinkRequest extends AbstractConverter<OrderItem, ReadFoodAndDrinkRequestResponse> {

    @Override
    public ReadFoodAndDrinkRequestResponse convert(@NonNull OrderItem response) {
        var foodAndDrinkRequest = getModelMapper().map(response, ReadFoodAndDrinkRequestResponse.class);
        foodAndDrinkRequest.setItem(response.getItem().getItem().getName());
        return foodAndDrinkRequest;
    }
}
