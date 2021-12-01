package com.ktsnvt.ktsnvt.dto.createrestauranttable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data @AllArgsConstructor @NoArgsConstructor
public class CreateRestaurantTableRequest {
    @NotNull
    private Integer number;

    @NotNull
    private Integer x;

    @NotNull
    private Integer y;

    @NotNull
    private Integer r;
}
