package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.*;
import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import com.ktsnvt.ktsnvt.repository.SuperUserRepository;
import com.ktsnvt.ktsnvt.service.AuthorityService;
import com.ktsnvt.ktsnvt.service.SalaryService;
import com.ktsnvt.ktsnvt.service.SuperUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Stream;

@Service
public class SuperUserServiceImpl extends TransactionalServiceBase implements SuperUserService, UserDetailsService {
    private final SuperUserRepository superUserRepository;

    private final AuthorityService authorityService;
    private final SalaryService salaryService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SuperUserServiceImpl(SuperUserRepository superUserRepository,
                                AuthorityService authorityService,
                                SalaryService salaryService,
                                PasswordEncoder passwordEncoder) {
        this.superUserRepository = superUserRepository;
        this.authorityService = authorityService;
        this.salaryService = salaryService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SuperUser create(SuperUser superUser) {
        var superUserWithSameEmail = superUserRepository.findByEmail(superUser.getEmail());
        if (superUserWithSameEmail.isPresent()) {
            throw new EmailAlreadyExistsException(superUser.getEmail());
        }
        superUser.setPassword(passwordEncoder.encode(superUser.getPassword()));
        var authority = authorityService.findByName(superUser.getType().toString());
        superUser.setAuthority(authority);
        return superUserRepository.save(superUser);
    }

    @Override
    public SuperUser read(Integer id) {
        return superUserRepository
                .findOneById(id)
                .orElseThrow(() -> new SuperUserNotFoundException(String.format("Super user with id: %d not found.", id)));
    }

    @Override
    public SuperUser findByEmail(String email) {
        return this.superUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new SuperUserNotFoundException(String.format("Superuser with email %s does not exist.", email)));
    }

    @Override
    public SuperUser readManagerForUpdate(Integer id) {
        return superUserRepository
                .getSuperUserForUpdate(id, SuperUserType.MANAGER)
                .orElseThrow(() -> new ManagerNotFoundException(String.format("Manager with id: %d not found.", id)));
    }

    @Override
    public Page<SuperUser> read(String query, BigDecimal salaryFrom, BigDecimal salaryTo, SuperUserType type, Pageable pageable) {
        return superUserRepository.findAll(query.trim().toLowerCase(), salaryFrom, salaryTo, type, pageable);
    }

    @Override
    public void deleteManager(Integer id) {
        var manager = readManagerForUpdate(id);
        salaryService.endActiveSalaryForUser(manager);
        manager.setIsActive(false);
    }

    @Override
    public void updatePassword(Integer id, String oldPassword, String newPassword) {
        var user = read(id);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidPasswordException("Incorrect old password provided.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
    }

    @Override
    public void update(Integer id, String name, String surname, String email) {
        var user = read(id);
        var superUserWithSameEmail = superUserRepository.findByEmail(email);
        superUserWithSameEmail.ifPresent(sameEmail -> {
            if (!sameEmail.getId().equals(user.getId())) {
                throw new EmailAlreadyExistsException(email);
            }
        });
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
    }

    @Override
    public Stream<SuperUser> readAll() {
        return superUserRepository.findAllByIsActiveTrue();
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.superUserRepository.findByEmail(s)
                .orElseThrow(() ->
                        new SuperUserNotFoundException(String.format("Superuser with email %s does not exist.", s)));
    }
}
