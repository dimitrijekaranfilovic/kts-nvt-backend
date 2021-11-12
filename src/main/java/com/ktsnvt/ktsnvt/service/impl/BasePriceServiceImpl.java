package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.repository.BasePriceRepository;
import com.ktsnvt.ktsnvt.service.BasePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasePriceServiceImpl implements BasePriceService {

    private final BasePriceRepository basePriceRepository;

    @Autowired
    public BasePriceServiceImpl(BasePriceRepository basePriceRepository) {
        this.basePriceRepository = basePriceRepository;
    }
}
