package com.ktsnvt.ktsnvt.dto.createorder;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data @Builder
public class CreateOrderRequest {

    @NotBlank(message = "Pin cannot be empty.")
    private String pin;

    @NotNull(message = "Table id must be provided")
    private Integer tableId;

}
