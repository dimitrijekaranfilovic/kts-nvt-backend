package com.ktsnvt.ktsnvt.support.addorderitem;


import com.ktsnvt.ktsnvt.dto.addorderitem.AddOrderItemResponse;
import com.ktsnvt.ktsnvt.model.OrderItem;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class OrderItemToAddOrderItemResponse extends AbstractConverter<OrderItem, AddOrderItemResponse> {
    @Override
    public AddOrderItemResponse convert(@NonNull OrderItem source) {
        return getModelMapper().map(source, AddOrderItemResponse.class);
    }
}
