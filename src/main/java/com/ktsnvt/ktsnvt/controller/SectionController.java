package com.ktsnvt.ktsnvt.controller;

import com.ktsnvt.ktsnvt.dto.readsection.ReadSectionResponse;
import com.ktsnvt.ktsnvt.dto.readsectiontablesresponse.ReadSectionTablesResponse;
import com.ktsnvt.ktsnvt.service.RestaurantTableService;
import com.ktsnvt.ktsnvt.service.SectionService;
import com.ktsnvt.ktsnvt.support.readsection.SectionToReadSectionResponse;
import com.ktsnvt.ktsnvt.support.readsectiontablesresponse.RestaurantTableToReadSectionTablesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/sections")
public class SectionController {
    private final SectionService sectionService;
    private final RestaurantTableService restaurantTableService;
    private final SectionToReadSectionResponse sectionToReadSectionResponse;
    private final RestaurantTableToReadSectionTablesResponse restaurantTableToReadSectionTablesResponse;

    @Autowired
    public SectionController(SectionService sectionService,
                             RestaurantTableService restaurantTableService,
                             SectionToReadSectionResponse sectionToReadSectionResponse,
                             RestaurantTableToReadSectionTablesResponse restaurantTableToReadSectionTablesResponse) {
        this.sectionService = sectionService;
        this.restaurantTableService = restaurantTableService;
        this.sectionToReadSectionResponse = sectionToReadSectionResponse;
        this.restaurantTableToReadSectionTablesResponse = restaurantTableToReadSectionTablesResponse;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ReadSectionResponse> getAllSections() {
        return sectionService.getAllSections().stream().map(s -> sectionToReadSectionResponse.convert(s)).collect(Collectors.toList());
    }

    @GetMapping(value = "{sectionId}/tables")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ReadSectionTablesResponse> getAllSectionTables(@PathVariable Integer sectionId) {
        return restaurantTableService.getAllTablesForSection(sectionId).stream().map(t -> restaurantTableToReadSectionTablesResponse.convert(t)).collect(Collectors.toList());
    }
}
