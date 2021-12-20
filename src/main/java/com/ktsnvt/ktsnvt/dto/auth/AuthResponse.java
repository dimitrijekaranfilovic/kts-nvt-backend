package com.ktsnvt.ktsnvt.dto.auth;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthResponse {

    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String jwt;
    List<String> authorities;
}
