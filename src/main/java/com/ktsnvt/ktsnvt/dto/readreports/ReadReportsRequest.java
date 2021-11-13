package com.ktsnvt.ktsnvt.dto.readreports;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ReadReportsRequest {

    @NotNull(message = "From date must be provided")
    private LocalDate from;

    @NotNull(message = "To date must be provided.")
    private LocalDate to;

}
