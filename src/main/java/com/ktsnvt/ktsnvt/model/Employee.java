package com.ktsnvt.ktsnvt.model;

import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee extends User {

    @Column(name = "pin", nullable = false)
    private Integer pin;

    @Column(name = "type", nullable = false)
    private EmployeeType type;
}
