package com.ktsnvt.ktsnvt.dto.sendorderitemgroup;


import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SendOrderItemGroupRequest {

    @NotBlank(message = "PIN cannot be blank.")
    private String pin;

}
