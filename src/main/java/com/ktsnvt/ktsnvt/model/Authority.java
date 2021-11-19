package com.ktsnvt.ktsnvt.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "authorities")
public class Authority extends BaseEntity implements GrantedAuthority {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public Authority() {
        super();
    }

    public Authority(String name) {
        this();
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}
