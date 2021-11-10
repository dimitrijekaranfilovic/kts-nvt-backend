package com.ktsnvt.ktsnvt.dto.deleteorderitem;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DeleteOrderItemRequest {

    @NotBlank(message = "PIN cannot be blank.")
    private String pin;

}
