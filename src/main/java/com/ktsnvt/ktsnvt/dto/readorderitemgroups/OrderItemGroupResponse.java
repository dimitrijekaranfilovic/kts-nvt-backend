package com.ktsnvt.ktsnvt.dto.readorderitemgroups;


import com.ktsnvt.ktsnvt.model.enums.OrderItemGroupStatus;
import lombok.Data;

import java.util.List;

@Data
public class OrderItemGroupResponse {

    private Integer id;
    private String name;
    private OrderItemGroupStatus status;
    private List<OrderItemResponse> orderItems;

    public OrderItemGroupResponse(Integer id, String name, OrderItemGroupStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

}
