package com.ktsnvt.ktsnvt.dto.readreports;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ReadReportsResponse {

    private List<LocalDate> labels;

    private List<BigDecimal> values;

}
