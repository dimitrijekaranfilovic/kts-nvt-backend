package com.ktsnvt.ktsnvt.dto.createesuperuser;

import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class CreateSuperUserRequest {

    @NotBlank(message = "First name cannot be blank.")
    private String name;

    @NotBlank(message = "Last name cannot be blank.")
    private String surname;

    @Email(message = "Valid email must be provided.")
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    private String password;

    @NotNull(message = "Salary must be provided.")
    @PositiveOrZero(message = "Salary must not be negative.")
    private BigDecimal salary;

    @NotNull(message = "Super user type is required.")
    private SuperUserType type;

}
