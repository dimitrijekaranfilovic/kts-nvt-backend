package com.ktsnvt.ktsnvt.dto.addorderitem;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddOrderItemRequest {
    @NotNull(message = "Order item group id cannot be null.")
    private Integer orderItemGroupId;

    @NotNull(message = "Menu item id cannot be null.")
    private Integer menuItemId;

    @NotNull(message = "Amount cannot be null.")
    @Positive(message = "Amount must be a positive number.")
    private Integer amount;

    @NotBlank(message = "PIN cannot be blank.")
    private String pin;


}
