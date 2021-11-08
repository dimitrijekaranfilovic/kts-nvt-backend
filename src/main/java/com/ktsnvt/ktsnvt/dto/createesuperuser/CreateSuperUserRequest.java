package com.ktsnvt.ktsnvt.dto.createesuperuser;

import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateSuperUserRequest {

    @NotBlank(message = "First name cannot be blank.")
    private String name;

    @NotBlank(message = "Last name cannot be blank.")
    private String surname;

    @Email(message = "Valid email must be provided.")
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    private String password;

    @NotNull(message = "Super user type is required.")
    private SuperUserType type;

}
