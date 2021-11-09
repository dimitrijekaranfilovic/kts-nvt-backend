package com.ktsnvt.ktsnvt.dto.readsuperusers;

import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReadSuperUsersResponse {

    private String name;

    private String surname;

    private String email;

    private SuperUserType type;

    private BigDecimal currentSalary;
}
