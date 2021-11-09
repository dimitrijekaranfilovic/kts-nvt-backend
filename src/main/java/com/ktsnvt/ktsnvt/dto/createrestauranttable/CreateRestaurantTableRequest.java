package com.ktsnvt.ktsnvt.dto.createrestauranttable;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateRestaurantTableRequest {
    @NotNull
    private Integer x;

    @NotNull
    private Integer y;

    @NotNull
    private Integer r;
}
