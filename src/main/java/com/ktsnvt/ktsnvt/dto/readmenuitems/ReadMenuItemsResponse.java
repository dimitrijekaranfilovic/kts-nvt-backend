package com.ktsnvt.ktsnvt.dto.readmenuitems;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReadMenuItemsResponse {
    private Integer id;
    private BigDecimal price;
    private String itemName;
    private String itemAllergies;
    private String itemDescription;
    private String itemCategory;
}
