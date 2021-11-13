package com.ktsnvt.ktsnvt.dto.deleteorderitem;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteOrderItemRequest {

    @NotNull(message = "PIN cannot be null.")
    private String pin;
}
