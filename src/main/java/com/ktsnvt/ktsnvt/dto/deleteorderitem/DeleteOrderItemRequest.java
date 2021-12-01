package com.ktsnvt.ktsnvt.dto.deleteorderitem;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteOrderItemRequest {

    @NotNull(message = "PIN cannot be null.")
    private String pin;
}
