package com.ktsnvt.ktsnvt.dto.readsuperusers;

import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReadSuperUsersRequest {

    private String query = "";

    private BigDecimal salaryLowerBound = BigDecimal.valueOf(0);

    private BigDecimal salaryUpperBound = BigDecimal.valueOf(Long.MAX_VALUE);

    private SuperUserType type;
}
