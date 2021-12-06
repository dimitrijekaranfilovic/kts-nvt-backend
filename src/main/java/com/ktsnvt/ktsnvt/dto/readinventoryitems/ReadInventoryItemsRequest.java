package com.ktsnvt.ktsnvt.dto.readinventoryitems;

import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadInventoryItemsRequest {

    private String query = "";

    @PositiveOrZero(message = "Lower bound for base price must not be negative.")
    private BigDecimal basePriceLowerBound = BigDecimal.valueOf(0);

    @PositiveOrZero(message = "Upper bound for base price must not be negative.")
    private BigDecimal basePriceUpperBound = BigDecimal.valueOf(Long.MAX_VALUE);

    private ItemCategory category;

}
