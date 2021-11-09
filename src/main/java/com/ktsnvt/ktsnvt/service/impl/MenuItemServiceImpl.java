package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.model.MenuItem;
import com.ktsnvt.ktsnvt.repository.MenuItemRepository;
import com.ktsnvt.ktsnvt.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;

    @Autowired
    public MenuItemServiceImpl(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }


    @Override
    public Page<MenuItem> getMenuItems(String name, Pageable pageable) {
        return this.menuItemRepository.getMenuItems(name, pageable);
    }
}
