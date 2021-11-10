package com.ktsnvt.ktsnvt.dto.updatesection;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateSectionRequest {

    @NotBlank(message = "Section name cannot be empty.")
    private String name;

}
