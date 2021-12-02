package com.ktsnvt.ktsnvt.dto.updatepassword;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class UpdatePasswordRequest {

    @NotBlank(message = "Old password can't be blank.")
    private String oldPassword;

    @NotBlank(message = "New password can't be blank.")
    private String newPassword;

}
