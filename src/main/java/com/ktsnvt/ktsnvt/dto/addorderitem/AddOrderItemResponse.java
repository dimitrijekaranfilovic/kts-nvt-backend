package com.ktsnvt.ktsnvt.dto.addorderitem;


import lombok.Data;

@Data
public class AddOrderItemResponse {
    private Integer id;
    private Integer amount;
    private Integer itemPrice;
    private String itemItemName;

}
