package com.ktsnvt.ktsnvt.model;

import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "super_users")
public class SuperUser extends User {

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "type", nullable = false)
    private SuperUserType type;

    public SuperUser(String name, String surname, Authority authority, String emailAddress, String passwordString, SuperUserType superUserType){
        super(name, surname, authority, new HashSet<>());
        password = passwordString;
        email = emailAddress;
        type = superUserType;
    }
}
