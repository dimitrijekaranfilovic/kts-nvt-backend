package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.Authority;

import java.util.Optional;

public interface AuthorityService {
    Authority findByName(String name);
}
