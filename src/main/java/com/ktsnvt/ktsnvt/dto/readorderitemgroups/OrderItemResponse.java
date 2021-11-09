package com.ktsnvt.ktsnvt.dto.readorderitemgroups;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {

    private Integer id;
    private Integer amount;
    private BigDecimal itemPrice;
    private String itemItemName;

}
