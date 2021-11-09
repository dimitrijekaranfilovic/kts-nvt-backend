package com.ktsnvt.ktsnvt.controller;

import com.ktsnvt.ktsnvt.dto.createsection.CreateSectionRequest;
import com.ktsnvt.ktsnvt.dto.createsection.CreateSectionResponse;
import com.ktsnvt.ktsnvt.dto.readsection.ReadSectionResponse;
import com.ktsnvt.ktsnvt.dto.readsectiontablesresponse.ReadSectionTablesResponse;
import com.ktsnvt.ktsnvt.model.Section;
import com.ktsnvt.ktsnvt.service.RestaurantTableService;
import com.ktsnvt.ktsnvt.service.SectionService;
import com.ktsnvt.ktsnvt.support.EntityConverter;
import com.ktsnvt.ktsnvt.support.readsection.SectionToReadSectionResponse;
import com.ktsnvt.ktsnvt.support.readsectiontablesresponse.RestaurantTableToReadSectionTablesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/sections")
public class SectionController {
    private final SectionService sectionService;
    private final RestaurantTableService restaurantTableService;

    private final SectionToReadSectionResponse sectionToReadSectionResponse;
    private final RestaurantTableToReadSectionTablesResponse restaurantTableToReadSectionTablesResponse;

    private final EntityConverter<CreateSectionRequest, Section> createSectionToSection;
    private final EntityConverter<Section, CreateSectionResponse> sectionToCreateSection;

    @Autowired
    public SectionController(SectionService sectionService,
                             RestaurantTableService restaurantTableService,
                             SectionToReadSectionResponse sectionToReadSectionResponse,
                             RestaurantTableToReadSectionTablesResponse restaurantTableToReadSectionTablesResponse,
                             EntityConverter<CreateSectionRequest, Section> createSectionToSection,
                             EntityConverter<Section, CreateSectionResponse> sectionToCreateSection) {
        this.sectionService = sectionService;
        this.restaurantTableService = restaurantTableService;
        this.sectionToReadSectionResponse = sectionToReadSectionResponse;
        this.restaurantTableToReadSectionTablesResponse = restaurantTableToReadSectionTablesResponse;
        this.createSectionToSection = createSectionToSection;
        this.sectionToCreateSection = sectionToCreateSection;
    }

    // PRE AUTHORIZE (ADMIN)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateSectionResponse createSection(@RequestBody @Valid CreateSectionRequest request) {
        var section = createSectionToSection.convert(request);
        var result = sectionService.create(section);
        return sectionToCreateSection.convert(result);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ReadSectionResponse> getAllSections() {
        return sectionService.getAllSections().stream().map(sectionToReadSectionResponse::convert).collect(Collectors.toList());
    }

    @GetMapping(value = "{sectionId}/tables")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ReadSectionTablesResponse> getAllSectionTables(@PathVariable Integer sectionId) {
        return restaurantTableService.getAllTablesForSection(sectionId).stream().map(restaurantTableToReadSectionTablesResponse::convert).collect(Collectors.toList());
    }
}
