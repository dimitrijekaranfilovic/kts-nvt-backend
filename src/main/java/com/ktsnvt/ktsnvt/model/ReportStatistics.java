package com.ktsnvt.ktsnvt.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReportStatistics<TLabel, TValue> {

    private List<TLabel> labels = new ArrayList<>();

    private List<TValue> values = new ArrayList<>();

    public void addSample(TLabel label, TValue value) {
        labels.add(label);
        values.add(value);
    }

}
