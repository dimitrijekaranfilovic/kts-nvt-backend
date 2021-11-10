package com.ktsnvt.ktsnvt.dto.movetable;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MoveTableRequest {

    @NotNull(message = "New x coordinate must be provided.")
    private Integer newX;

    @NotNull(message = "New y coordinate must be provided.")
    private Integer newY;

}
