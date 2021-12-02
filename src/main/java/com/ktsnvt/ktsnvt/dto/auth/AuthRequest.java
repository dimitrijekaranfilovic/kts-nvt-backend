package com.ktsnvt.ktsnvt.dto.auth;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthRequest {

    private String email;
    private String password;
}
