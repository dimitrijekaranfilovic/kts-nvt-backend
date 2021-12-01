package com.ktsnvt.ktsnvt.dto.createorderitemgroup;


import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderItemGroupRequest {

    @NotBlank(message = "Group name cannot be blank.")
    private String groupName;

    @NotBlank(message = "PIN cannot be blank.")
    private String pin;

}
