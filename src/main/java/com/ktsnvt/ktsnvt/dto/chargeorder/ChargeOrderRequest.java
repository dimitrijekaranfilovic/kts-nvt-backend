package com.ktsnvt.ktsnvt.dto.chargeorder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class ChargeOrderRequest {

    @NotBlank(message = "Pin cannot be blank.")
    private String pin;
}
