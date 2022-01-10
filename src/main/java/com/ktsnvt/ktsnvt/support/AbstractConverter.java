package com.ktsnvt.ktsnvt.support;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractConverter<F, T> implements EntityConverter<F, T> {

    private ModelMapper modelMapper;

    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    @Autowired
    public final void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

}
