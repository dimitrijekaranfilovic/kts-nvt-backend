package com.ktsnvt.ktsnvt.controller;

import com.ktsnvt.ktsnvt.dto.createrestauranttable.CreateRestaurantTableRequest;
import com.ktsnvt.ktsnvt.dto.createrestauranttable.CreateRestaurantTableResponse;
import com.ktsnvt.ktsnvt.dto.readsection.ReadSectionResponse;
import com.ktsnvt.ktsnvt.dto.readsectiontablesresponse.ReadSectionTablesResponse;
import com.ktsnvt.ktsnvt.model.RestaurantTable;
import com.ktsnvt.ktsnvt.service.RestaurantTableService;
import com.ktsnvt.ktsnvt.service.SectionService;
import com.ktsnvt.ktsnvt.support.createrestauranttable.CreateRestaurantTableRequestToRestaurantTable;
import com.ktsnvt.ktsnvt.support.createrestauranttable.RestaurantTableToCreateRestaurantTableResponse;
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
    private final CreateRestaurantTableRequestToRestaurantTable createRestaurantTableRequestToRestaurantTable;
    private final RestaurantTableToCreateRestaurantTableResponse restaurantTableToCreateRestaurantTableResponse;

    @Autowired
    public SectionController(SectionService sectionService,
                             RestaurantTableService restaurantTableService,
                             SectionToReadSectionResponse sectionToReadSectionResponse,
                             RestaurantTableToReadSectionTablesResponse restaurantTableToReadSectionTablesResponse,
                             CreateRestaurantTableRequestToRestaurantTable createRestaurantTableRequestToRestaurantTable,
                             RestaurantTableToCreateRestaurantTableResponse restaurantTableToCreateRestaurantTableResponse) {
        this.sectionService = sectionService;
        this.restaurantTableService = restaurantTableService;
        this.sectionToReadSectionResponse = sectionToReadSectionResponse;
        this.restaurantTableToReadSectionTablesResponse = restaurantTableToReadSectionTablesResponse;
        this.createRestaurantTableRequestToRestaurantTable = createRestaurantTableRequestToRestaurantTable;
        this.restaurantTableToCreateRestaurantTableResponse = restaurantTableToCreateRestaurantTableResponse;
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

    @PostMapping(value = "table/{sectionId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateRestaurantTableResponse createTable(@RequestBody CreateRestaurantTableRequest request, @PathVariable Integer sectionId){
        RestaurantTable newTable = createRestaurantTableRequestToRestaurantTable.convert(request);

        RestaurantTable createdTable = restaurantTableService.createRestaurantTable(newTable, sectionId);

        return restaurantTableToCreateRestaurantTableResponse.convert(createdTable);
    }

    @DeleteMapping(value = "table/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTable(@PathVariable Integer id){
        restaurantTableService.deleteRestaurantTable(id);
    }
}
