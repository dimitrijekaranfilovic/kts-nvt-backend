package com.ktsnvt.ktsnvt.dto.readmenuitems;

import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ReadMenuItemsRequest {

    private String query = "";

    @PositiveOrZero(message = "Lower bound for price must not be negative.")
    private BigDecimal priceLowerBound = BigDecimal.valueOf(0);

    @PositiveOrZero(message = "Upper bound for price must not be negative.")
    private BigDecimal priceUpperBound = BigDecimal.valueOf(Long.MAX_VALUE);

    private ItemCategory category;

}
