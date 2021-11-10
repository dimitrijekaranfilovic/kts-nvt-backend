package com.ktsnvt.ktsnvt.controller;


import com.ktsnvt.ktsnvt.dto.createrestauranttable.CreateRestaurantTableRequest;
import com.ktsnvt.ktsnvt.dto.createrestauranttable.CreateRestaurantTableResponse;
import com.ktsnvt.ktsnvt.dto.createsection.CreateSectionRequest;
import com.ktsnvt.ktsnvt.dto.createsection.CreateSectionResponse;
import com.ktsnvt.ktsnvt.dto.movetable.MoveTableRequest;
import com.ktsnvt.ktsnvt.dto.readsection.ReadSectionResponse;
import com.ktsnvt.ktsnvt.dto.readsectiontablesresponse.ReadSectionTablesResponse;
import com.ktsnvt.ktsnvt.dto.updatesection.UpdateSectionRequest;
import com.ktsnvt.ktsnvt.model.RestaurantTable;
import com.ktsnvt.ktsnvt.model.Section;
import com.ktsnvt.ktsnvt.service.RestaurantTableService;
import com.ktsnvt.ktsnvt.service.SectionService;
import com.ktsnvt.ktsnvt.support.EntityConverter;
import com.ktsnvt.ktsnvt.support.createrestauranttable.CreateRestaurantTableRequestToRestaurantTable;
import com.ktsnvt.ktsnvt.support.createrestauranttable.RestaurantTableToCreateRestaurantTableResponse;
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
    private final CreateRestaurantTableRequestToRestaurantTable createRestaurantTableRequestToRestaurantTable;
    private final RestaurantTableToCreateRestaurantTableResponse restaurantTableToCreateRestaurantTableResponse;

    private final EntityConverter<CreateSectionRequest, Section> createSectionToSection;
    private final EntityConverter<Section, CreateSectionResponse> sectionToCreateSection;

    @Autowired
    public SectionController(SectionService sectionService,
                             RestaurantTableService restaurantTableService,
                             SectionToReadSectionResponse sectionToReadSectionResponse,
                             RestaurantTableToReadSectionTablesResponse restaurantTableToReadSectionTablesResponse,
                             CreateRestaurantTableRequestToRestaurantTable createRestaurantTableRequestToRestaurantTable,
                             RestaurantTableToCreateRestaurantTableResponse restaurantTableToCreateRestaurantTableResponse,
                             EntityConverter<CreateSectionRequest, Section> createSectionToSection,
                             EntityConverter<Section, CreateSectionResponse> sectionToCreateSection) {
        this.sectionService = sectionService;
        this.restaurantTableService = restaurantTableService;
        this.sectionToReadSectionResponse = sectionToReadSectionResponse;
        this.restaurantTableToReadSectionTablesResponse = restaurantTableToReadSectionTablesResponse;
        this.createRestaurantTableRequestToRestaurantTable = createRestaurantTableRequestToRestaurantTable;
        this.restaurantTableToCreateRestaurantTableResponse = restaurantTableToCreateRestaurantTableResponse;
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

    // PRE AUTHORIZE (ADMIN)
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateSection(@PathVariable Integer id, @RequestBody @Valid UpdateSectionRequest request) {
        sectionService.update(id, request.getName());
    }

    // PRE AUTHORIZE (ADMIN)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSection(@PathVariable Integer id) {
        sectionService.delete(id);
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

    @PostMapping(value = "{sectionId}/tables")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateRestaurantTableResponse createTable(@RequestBody @Valid CreateRestaurantTableRequest
                                                             request, @PathVariable Integer sectionId) {
        RestaurantTable newTable = createRestaurantTableRequestToRestaurantTable.convert(request);

        RestaurantTable createdTable = restaurantTableService.createRestaurantTable(newTable, sectionId);

        return restaurantTableToCreateRestaurantTableResponse.convert(createdTable);
    }

    @DeleteMapping(value = "tables/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTable(@PathVariable Integer id) {
        restaurantTableService.deleteRestaurantTable(id);
    }

    @PutMapping("/{sectionId}/tables/{tableId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTable(@PathVariable Integer sectionId, @PathVariable Integer tableId, @RequestBody @Valid MoveTableRequest request) {
        restaurantTableService.updateTablePosition(sectionId, tableId, request.getNewX(), request.getNewY());
    }

}
