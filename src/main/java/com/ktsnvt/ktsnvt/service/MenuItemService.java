package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MenuItemService {


    Page<MenuItem> getMenuItems(String name, Pageable pageable);
}
