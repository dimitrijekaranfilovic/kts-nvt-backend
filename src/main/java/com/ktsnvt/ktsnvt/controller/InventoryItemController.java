package com.ktsnvt.ktsnvt.controller;

import com.ktsnvt.ktsnvt.dto.createinventoryitem.CreateInventoryItemRequest;
import com.ktsnvt.ktsnvt.dto.createinventoryitem.CreateInventoryItemResponse;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.service.InventoryItemService;
import com.ktsnvt.ktsnvt.support.EntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/inventory-items")
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    private final EntityConverter<CreateInventoryItemRequest, InventoryItem> createInventoryItemRequestToInventoryItem;

    private final EntityConverter<InventoryItem, CreateInventoryItemResponse> inventoryItemToCreateInventoryItemResponse;

    @Autowired
    public InventoryItemController(InventoryItemService inventoryItemService,
                                   EntityConverter<CreateInventoryItemRequest,
                                           InventoryItem> createInventoryItemRequestToInventoryItem,
                                   EntityConverter<InventoryItem,
                                           CreateInventoryItemResponse> inventoryItemToCreateInventoryItemResponse) {
        this.inventoryItemService = inventoryItemService;
        this.createInventoryItemRequestToInventoryItem = createInventoryItemRequestToInventoryItem;
        this.inventoryItemToCreateInventoryItemResponse = inventoryItemToCreateInventoryItemResponse;
    }

    // PRE AUTHORIZE (ADMIN, MANAGER)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateInventoryItemResponse createInventoryItem(@RequestBody @Valid CreateInventoryItemRequest request) {
        var inventoryItem = createInventoryItemRequestToInventoryItem.convert(request);
        var result = inventoryItemService.createInventoryItem(inventoryItem);
        return inventoryItemToCreateInventoryItemResponse.convert(result);
    }

}
