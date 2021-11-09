package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.EmailAlreadyExistsException;
import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.repository.SuperUserRepository;
import com.ktsnvt.ktsnvt.service.AuthorityService;
import com.ktsnvt.ktsnvt.service.SuperUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SuperUserServiceImpl implements SuperUserService {
    private final SuperUserRepository superUserRepository;
    private final AuthorityService authorityService;

    @Autowired
    public SuperUserServiceImpl(SuperUserRepository superUserRepository, AuthorityService authorityService) {
        this.superUserRepository = superUserRepository;
        this.authorityService = authorityService;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SuperUser create(SuperUser superUser) {
        var superUserWithSameEmail = superUserRepository.findByEmail(superUser.getEmail());
        if (superUserWithSameEmail.isPresent()) {
            throw new EmailAlreadyExistsException(superUser.getEmail());
        }
        var authority = authorityService.findByName(superUser.getType().toString());
        superUser.setAuthority(authority);
        return superUserRepository.save(superUser);
    }
}
