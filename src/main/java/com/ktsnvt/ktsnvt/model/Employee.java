package com.ktsnvt.ktsnvt.model;

import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "employees")
@Where(clause = "is_active = true")
public class Employee extends User {

    @Column(name = "pin", nullable = false)
    private String pin;

    @Column(name = "type", nullable = false)
    private EmployeeType type;

    public Employee() {
        super();
    }

    public Employee(String name, String surname, Authority authority, String pinCode, EmployeeType employeeType) {
        super(name, surname, authority);
        pin = pinCode;
        type = employeeType;
    }

}
