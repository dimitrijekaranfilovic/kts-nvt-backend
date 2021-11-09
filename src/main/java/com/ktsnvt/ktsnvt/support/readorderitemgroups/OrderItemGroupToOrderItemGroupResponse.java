package com.ktsnvt.ktsnvt.support.readorderitemgroups;

import com.ktsnvt.ktsnvt.dto.readorderitemgroups.OrderItemGroupResponse;
import com.ktsnvt.ktsnvt.dto.readorderitemgroups.OrderItemResponse;
import com.ktsnvt.ktsnvt.model.OrderItemGroup;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class OrderItemGroupToOrderItemGroupResponse extends AbstractConverter<OrderItemGroup, OrderItemGroupResponse> {
    @Override
    public OrderItemGroupResponse convert(@NonNull OrderItemGroup source) {
        var orderItemGroupResponse = new OrderItemGroupResponse(source.getId(),
                source.getName(),
                source.getStatus());

        orderItemGroupResponse.setOrderItems(source.getOrderItems().stream().map(orderItem -> getModelMapper().map(orderItem, OrderItemResponse.class)).collect(Collectors.toList()));
        return orderItemGroupResponse;
    }
}
