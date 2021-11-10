package com.ktsnvt.ktsnvt.dto.createrestauranttable;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateRestaurantTableRequest {
    @NotBlank
    private Integer number;

    @NotNull
    private Integer x;

    @NotNull
    private Integer y;

    @NotNull
    private Integer r;
}
