package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.repository.SuperUserRepository;
import com.ktsnvt.ktsnvt.service.SuperUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuperUserServiceImpl implements SuperUserService {
    private final SuperUserRepository superUserRepository;

    @Autowired
    public SuperUserServiceImpl(SuperUserRepository superUserRepository) {
        this.superUserRepository = superUserRepository;
    }

    @Override
    public SuperUser create(SuperUser superUser) {
        return superUserRepository.save(superUser);
    }
}
