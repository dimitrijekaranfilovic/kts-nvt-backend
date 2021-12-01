package com.ktsnvt.ktsnvt.dto.updateorderitem;


import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderItemRequest {

    @NotNull(message = "Amount cannot be null.")
    @Positive(message = "Amount cannot be equal to or lower than 0.")
    private Integer amount;

    @NotNull(message = "PIN cannot be null.")
    private String pin;

}
