package com.ktsnvt.ktsnvt.dto.deleteorderitemgroup;


import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeleteOrderItemGroupRequest {

    @NotBlank(message = "PIN cannot be blank.")
    private String pin;

}
