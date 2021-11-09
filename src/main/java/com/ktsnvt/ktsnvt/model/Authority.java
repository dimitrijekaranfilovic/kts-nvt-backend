package com.ktsnvt.ktsnvt.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "authorities")
@Where(clause = "is_active = true")
public class Authority extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public Authority() {
        super();
    }

    public Authority(String name) {
        this();
        this.name = name;
    }

}
