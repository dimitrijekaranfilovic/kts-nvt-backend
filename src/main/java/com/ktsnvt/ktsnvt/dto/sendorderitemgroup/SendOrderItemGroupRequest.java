package com.ktsnvt.ktsnvt.dto.sendorderitemgroup;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SendOrderItemGroupRequest {

    @NotBlank(message = "PIN cannot be blank.")
    private String pin;

}
