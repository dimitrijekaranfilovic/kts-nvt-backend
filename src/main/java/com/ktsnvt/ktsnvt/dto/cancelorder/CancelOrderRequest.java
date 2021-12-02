package com.ktsnvt.ktsnvt.dto.cancelorder;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class CancelOrderRequest {

    @NotBlank(message = "Pin cannot be blank.")
    private String pin;

}
