package com.ktsnvt.ktsnvt.controller;

import com.ktsnvt.ktsnvt.annotations.IsSuperUser;
import com.ktsnvt.ktsnvt.dto.createinventoryitem.CreateInventoryItemRequest;
import com.ktsnvt.ktsnvt.dto.createinventoryitem.CreateInventoryItemResponse;
import com.ktsnvt.ktsnvt.dto.readinventoryitems.ReadInventoryItemsRequest;
import com.ktsnvt.ktsnvt.dto.readinventoryitems.ReadInventoryItemsResponse;
import com.ktsnvt.ktsnvt.dto.updateinventoryitem.UpdateInventoryItemRequest;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.service.InventoryItemService;
import com.ktsnvt.ktsnvt.support.EntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/inventory-items")
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    private final EntityConverter<CreateInventoryItemRequest, InventoryItem> createInventoryItemRequestToInventoryItem;

    private final EntityConverter<InventoryItem, CreateInventoryItemResponse> inventoryItemToCreateInventoryItemResponse;

    private final EntityConverter<InventoryItem, ReadInventoryItemsResponse> inventoryItemToReadInventoryItemsResponse;

    @Autowired
    public InventoryItemController(InventoryItemService inventoryItemService,
                                   EntityConverter<CreateInventoryItemRequest,
                                           InventoryItem> createInventoryItemRequestToInventoryItem,
                                   EntityConverter<InventoryItem,
                                           CreateInventoryItemResponse> inventoryItemToCreateInventoryItemResponse,
                                   EntityConverter<InventoryItem,
                                           ReadInventoryItemsResponse> inventoryItemToReadInventoryItemsResponse) {
        this.inventoryItemService = inventoryItemService;
        this.createInventoryItemRequestToInventoryItem = createInventoryItemRequestToInventoryItem;
        this.inventoryItemToCreateInventoryItemResponse = inventoryItemToCreateInventoryItemResponse;
        this.inventoryItemToReadInventoryItemsResponse = inventoryItemToReadInventoryItemsResponse;
    }

    @IsSuperUser
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateInventoryItemResponse createInventoryItem(@RequestBody @Valid CreateInventoryItemRequest request) {
        var inventoryItem = createInventoryItemRequestToInventoryItem.convert(request);
        var result = inventoryItemService.createInventoryItem(inventoryItem);
        return inventoryItemToCreateInventoryItemResponse.convert(result);
    }

    @IsSuperUser
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ReadInventoryItemsResponse> readInventoryItems(@Valid ReadInventoryItemsRequest request,
                                                               @PageableDefault Pageable pageable) {
        var page = inventoryItemService.read(request.getQuery(), request.getBasePriceLowerBound(),
                request.getBasePriceUpperBound(), request.getCategory(), pageable);
        return page.map(inventoryItemToReadInventoryItemsResponse::convert);
    }

    @IsSuperUser
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInventoryItem(@PathVariable Integer id) {
        inventoryItemService.delete(id);
    }

    @IsSuperUser
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @RequestBody @Valid UpdateInventoryItemRequest request) {
        inventoryItemService.update(id, request.getName(), request.getDescription(), request.getAllergies(),
                request.getImage(), request.getCategory(), request.getBasePrice());
    }


}
