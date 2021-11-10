package com.ktsnvt.ktsnvt.support.createorder;

import com.ktsnvt.ktsnvt.dto.createorder.CreateOrderResponse;
import com.ktsnvt.ktsnvt.model.Order;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class OrderToCreateOrderResponse extends AbstractConverter<Order, CreateOrderResponse> {
    @Override
    public CreateOrderResponse convert(@NonNull Order source) {
        return getModelMapper().map(source, CreateOrderResponse.class);
    }
}
