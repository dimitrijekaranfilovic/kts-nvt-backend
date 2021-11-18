package com.ktsnvt.ktsnvt.model;

import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "super_users")
public class SuperUser extends User implements UserDetails {

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "type", nullable = false)
    private SuperUserType type;

    public SuperUser() {
        super();
    }

    public SuperUser(String name, String surname, Authority authority, String emailAddress, String passwordString, SuperUserType superUserType) {
        super(name, surname, authority);
        password = passwordString;
        email = emailAddress;
        type = superUserType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var l = new ArrayList<Authority>();
        l.add(this.getAuthority());
        return l;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.getIsActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.getIsActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.getIsActive();
    }

    @Override
    public boolean isEnabled() {
        return this.getIsActive();
    }
}
