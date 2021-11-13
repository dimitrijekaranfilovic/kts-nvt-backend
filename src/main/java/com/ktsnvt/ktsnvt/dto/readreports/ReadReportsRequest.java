package com.ktsnvt.ktsnvt.dto.readreports;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReadReportsRequest {

    private LocalDateTime from;

    private LocalDateTime to;

}
