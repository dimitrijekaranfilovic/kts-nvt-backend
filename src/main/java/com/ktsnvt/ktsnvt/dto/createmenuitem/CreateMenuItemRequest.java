package com.ktsnvt.ktsnvt.dto.createmenuitem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMenuItemRequest {

    @NotNull(message = "Inventory item id must be provided")
    private Integer inventoryItemId;

    @NotNull(message = "Menu item's price must be provided")
    @PositiveOrZero(message = "Menu item's price must be a positive number or zero.")
    private BigDecimal price;
}
