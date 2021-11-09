package com.ktsnvt.ktsnvt.dto.updatepassword;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdatePasswordRequest {

    @NotBlank(message = "Old password can't be blank.")
    private String oldPassword;

    @NotBlank(message = "New password can't be blank.")
    private String newPassword;

}
