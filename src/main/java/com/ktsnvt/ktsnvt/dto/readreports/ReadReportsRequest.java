package com.ktsnvt.ktsnvt.dto.readreports;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class ReadReportsRequest {

    @NotNull(message = "From date must be provided")
    private String from;

    @NotNull(message = "To date must be provided.")
    private String to;

    public LocalDate getFromLocalDate() {
        if (from == null) {
            return null;
        }
        return LocalDate.parse(from, DateTimeFormatter.ISO_DATE);
    }

    public LocalDate getToLocalDate() {
        if (to == null) {
            return null;
        }
        return LocalDate.parse(to, DateTimeFormatter.ISO_DATE);
    }

}
