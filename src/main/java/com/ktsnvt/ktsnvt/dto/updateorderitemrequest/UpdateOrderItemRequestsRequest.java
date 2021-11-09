package com.ktsnvt.ktsnvt.dto.updateorderitemrequest;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
public class UpdateOrderItemRequestsRequest {
    @NotNull(message = "Requested action cannot be null.")
    @Pattern(regexp = "PREPARE|FINISH", message = "Requested action can be PREPARE or FINISH.")
    private String action;

    @Positive(message = "Item id cannot be a negative value.")
    private Integer itemId;

    @NotNull(message = "Employee PIN cannot be null.")
    @Pattern(regexp = "[0-9]{4}", message = "Invalid PIN format.")
    private String employeePin;
}
