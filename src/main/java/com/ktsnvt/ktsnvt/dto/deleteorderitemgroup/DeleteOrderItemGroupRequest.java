package com.ktsnvt.ktsnvt.dto.deleteorderitemgroup;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DeleteOrderItemGroupRequest {

    @NotBlank(message = "PIN cannot be blank.")
    private String pin;

}
