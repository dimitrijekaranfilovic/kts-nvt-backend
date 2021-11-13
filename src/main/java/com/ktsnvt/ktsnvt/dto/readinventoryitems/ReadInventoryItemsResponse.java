package com.ktsnvt.ktsnvt.dto.readinventoryitems;

import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReadInventoryItemsResponse {

    private Integer id;

    private String name;

    private String description;

    private BigDecimal currentPrice;

    private String allergies;

    private ItemCategory category;

    private Boolean isInMenu;

    private String image;
}
