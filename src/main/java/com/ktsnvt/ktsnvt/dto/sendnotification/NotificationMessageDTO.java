package com.ktsnvt.ktsnvt.dto.sendnotification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationMessageDTO {

    @NotNull
    private String message;

    @NotBlank
    private String fromId;
}
