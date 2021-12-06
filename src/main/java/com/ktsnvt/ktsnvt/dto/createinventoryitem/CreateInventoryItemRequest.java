package com.ktsnvt.ktsnvt.dto.createinventoryitem;

import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInventoryItemRequest {

    @NotBlank(message = "Inventory item's name can't be blank.")
    private String name;

    @NotNull(message = "Inventory item's base price must be provided")
    @PositiveOrZero(message = "Inventory item's base price must not be negative.")
    private BigDecimal basePrice;

    private String description = "";

    private String image = "";

    private String allergies = "";

    @NotNull(message = "Inventory item's category is required.")
    private ItemCategory category;
}
