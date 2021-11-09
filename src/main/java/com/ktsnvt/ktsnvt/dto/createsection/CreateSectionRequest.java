package com.ktsnvt.ktsnvt.dto.createsection;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateSectionRequest {

    @NotBlank(message = "Section name cannot be blank.")
    private String name;

}
