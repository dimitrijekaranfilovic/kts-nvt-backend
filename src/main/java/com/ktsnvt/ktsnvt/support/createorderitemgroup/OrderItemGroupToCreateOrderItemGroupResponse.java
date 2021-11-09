package com.ktsnvt.ktsnvt.support.createorderitemgroup;

import com.ktsnvt.ktsnvt.dto.createorderitemgroup.CreateOrderItemGroupResponse;
import com.ktsnvt.ktsnvt.model.OrderItemGroup;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;


@Component
public class OrderItemGroupToCreateOrderItemGroupResponse extends AbstractConverter<OrderItemGroup, CreateOrderItemGroupResponse> {
    @Override
    public CreateOrderItemGroupResponse convert(@NonNull OrderItemGroup source) {
        return getModelMapper().map(source, CreateOrderItemGroupResponse.class);
    }
}
