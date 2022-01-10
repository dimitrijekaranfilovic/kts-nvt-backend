package com.ktsnvt.ktsnvt.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReportStatistics<L, V> {

    private List<L> labels = new ArrayList<>();

    private List<V> values = new ArrayList<>();

    public void addSample(L label, V value) {
        labels.add(label);
        values.add(value);
    }

}
