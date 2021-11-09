package com.ktsnvt.ktsnvt.controller;


import com.ktsnvt.ktsnvt.dto.displaypaginatedmenuitems.DisplayMenuItemResponse;
import com.ktsnvt.ktsnvt.service.MenuItemService;
import com.ktsnvt.ktsnvt.support.displaypaginatedmenuitems.MenuItemToDisplayMenuItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;
    private final MenuItemToDisplayMenuItemResponse menuItemToDisplayMenuItemResponse;

    @Autowired
    public MenuItemController(MenuItemService menuItemService, MenuItemToDisplayMenuItemResponse menuItemToDisplayMenuItemResponse) {
        this.menuItemService = menuItemService;
        this.menuItemToDisplayMenuItemResponse = menuItemToDisplayMenuItemResponse;
    }


    @GetMapping
    public Page<DisplayMenuItemResponse> getPaginatedMenuItems(@RequestParam("name") String name, @PageableDefault Pageable pageable){
        return this.menuItemService.getMenuItems(name, pageable).map(menuItemToDisplayMenuItemResponse::convert);
    }

}
