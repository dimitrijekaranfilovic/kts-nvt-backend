package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.AuthorityNotFoundException;
import com.ktsnvt.ktsnvt.model.Authority;
import com.ktsnvt.ktsnvt.repository.AuthorityRepository;
import com.ktsnvt.ktsnvt.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl extends TransactionalServiceBase implements AuthorityService {
    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Authority findByName(String name) {
        return authorityRepository
                .findByName(name)
                .orElseThrow(() -> new AuthorityNotFoundException("Cannot find authority with name: " + name));
    }
}
