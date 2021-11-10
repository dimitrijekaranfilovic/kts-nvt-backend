package com.ktsnvt.ktsnvt.dto.createorderitemgroup;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateOrderItemGroupRequest {

    @NotBlank(message = "Group name cannot be blank.")
    private String groupName;

    @NotBlank(message = "PIN cannot be blank.")
    private String pin;

}
