package com.ktsnvt.ktsnvt.support;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractConverter<TFrom, TTo> implements EntityConverter<TFrom, TTo> {

    private ModelMapper modelMapper;

    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    @Autowired
    public final void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

}
