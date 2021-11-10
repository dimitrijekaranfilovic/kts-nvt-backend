package com.ktsnvt.ktsnvt.dto.cancelorder;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CancelOrderRequest {

    @NotBlank(message = "Pin cannot be blank.")
    private String pin;

}
