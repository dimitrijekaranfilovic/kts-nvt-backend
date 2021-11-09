package com.ktsnvt.ktsnvt.dto.updatesuperuser;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UpdateSuperUserRequest {

    @NotBlank(message = "First name cannot be blank.")
    private String name;

    @NotBlank(message = "Last name cannot be blank.")
    private String surname;

    @Email(message = "Valid email must be provided.")
    private String email;

}
