package com.ktsnvt.ktsnvt.dto.createsection;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateSectionRequest {

    @NotBlank(message = "Section name cannot be blank.")
    private String name;

}
