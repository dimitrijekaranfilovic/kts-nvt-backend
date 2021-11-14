package com.ktsnvt.ktsnvt.dto.chargeorder;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class ChargeOrderRequest {

    @NotBlank(message = "Pin cannot be blank.")
    private String pin;
}
