package com.ktsnvt.ktsnvt.dto.displaypaginatedmenuitems;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class DisplayMenuItemResponse {

    private Integer id;
    private BigDecimal price;
    private String itemImage;
    private String itemName;
    private String itemAllergies;
    private String itemDescription;
    private String itemCategory;
}
