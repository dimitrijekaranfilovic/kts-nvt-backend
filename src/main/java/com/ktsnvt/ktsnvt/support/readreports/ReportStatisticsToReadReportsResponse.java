package com.ktsnvt.ktsnvt.support.readreports;

import com.ktsnvt.ktsnvt.dto.readreports.ReadReportsResponse;
import com.ktsnvt.ktsnvt.model.ReportStatistics;
import com.ktsnvt.ktsnvt.support.AbstractConverter;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class ReportStatisticsToReadReportsResponse extends AbstractConverter<ReportStatistics<LocalDate, BigDecimal>, ReadReportsResponse> {
    @Override
    public ReadReportsResponse convert(@NonNull ReportStatistics<LocalDate, BigDecimal> source) {
        return getModelMapper().map(source, ReadReportsResponse.class);
    }
}
