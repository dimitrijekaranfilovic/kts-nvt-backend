package com.ktsnvt.ktsnvt.controller;


import com.ktsnvt.ktsnvt.annotations.IsSuperUser;
import com.ktsnvt.ktsnvt.dto.createmenuitem.CreateMenuItemRequest;
import com.ktsnvt.ktsnvt.dto.createmenuitem.CreateMenuItemResponse;
import com.ktsnvt.ktsnvt.dto.displaypaginatedmenuitems.DisplayMenuItemResponse;
import com.ktsnvt.ktsnvt.dto.updatemenuitemprice.UpdateMenuItemPriceRequest;
import com.ktsnvt.ktsnvt.dto.updatemenuitemprice.UpdateMenuItemPriceResponse;
import com.ktsnvt.ktsnvt.model.MenuItem;
import com.ktsnvt.ktsnvt.service.MenuItemService;
import com.ktsnvt.ktsnvt.support.EntityConverter;
import com.ktsnvt.ktsnvt.support.displaypaginatedmenuitems.MenuItemToDisplayMenuItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;
    private final EntityConverter<MenuItem, DisplayMenuItemResponse> menuItemToDisplayMenuItemResponse;
    private final EntityConverter<MenuItem, CreateMenuItemResponse> menuItemToCreateMenuItemResponse;
    private final EntityConverter<MenuItem, UpdateMenuItemPriceResponse> menuItemToUpdateMenuItemPriceResponse;

    @Autowired
    public MenuItemController(MenuItemService menuItemService,
                              MenuItemToDisplayMenuItemResponse menuItemToDisplayMenuItemResponse,
                              EntityConverter<MenuItem, CreateMenuItemResponse> menuItemToCreateMenuItemResponse,
                              EntityConverter<MenuItem, UpdateMenuItemPriceResponse> menuItemToUpdateMenuItemPriceResponse) {
        this.menuItemService = menuItemService;
        this.menuItemToDisplayMenuItemResponse = menuItemToDisplayMenuItemResponse;
        this.menuItemToCreateMenuItemResponse = menuItemToCreateMenuItemResponse;
        this.menuItemToUpdateMenuItemPriceResponse = menuItemToUpdateMenuItemPriceResponse;
    }


    @GetMapping
    public Page<DisplayMenuItemResponse> getPaginatedMenuItems(@RequestParam("name") String name, @PageableDefault Pageable pageable) {
        return this.menuItemService.getMenuItems(name, pageable).map(menuItemToDisplayMenuItemResponse::convert);
    }

    @IsSuperUser
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateMenuItemResponse createMenuItem(@RequestBody @Valid CreateMenuItemRequest request) {
        return menuItemToCreateMenuItemResponse
                .convert(this.menuItemService.createMenuItem(request.getPrice(), request.getInventoryItemId()));
    }

    @IsSuperUser
    @PostMapping("/{id}/price")
    @ResponseStatus(HttpStatus.CREATED)
    public UpdateMenuItemPriceResponse updatePrice(@PathVariable Integer id,
                                                   @RequestBody @Valid UpdateMenuItemPriceRequest request) {
        return menuItemToUpdateMenuItemPriceResponse
                .convert(this.menuItemService.updateMenuItemPrice(request.getPrice(), id));
    }

    @IsSuperUser
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateMenuItem(@PathVariable Integer id) {
        menuItemService.deactivateMenuItem(id);
    }

}
