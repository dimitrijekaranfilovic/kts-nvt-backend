package com.ktsnvt.ktsnvt.dto.readreports;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class ReadReportsRequest {

    @NotBlank(message = "From date must be provided")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String from;

    @NotBlank(message = "To date must be provided.")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String to;

    public LocalDate getFromLocalDate() {
        return LocalDate.parse(from, DateTimeFormatter.ISO_DATE);
    }

    public LocalDate getToLocalDate() {
        return LocalDate.parse(to, DateTimeFormatter.ISO_DATE);
    }
}
