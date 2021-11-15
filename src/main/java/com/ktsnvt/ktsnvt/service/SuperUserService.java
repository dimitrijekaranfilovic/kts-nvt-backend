package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.stream.Stream;

public interface SuperUserService {
    SuperUser create(SuperUser superUser);

    SuperUser read(Integer id);

    Page<SuperUser> read(String query, BigDecimal salaryFrom, BigDecimal salaryTo, SuperUserType type, Pageable pageable);

    void deleteManager(Integer id);

    void updatePassword(Integer id, String oldPassword, String newPassword);

    void update(Integer id, String name, String surname, String email);

    Stream<SuperUser> readAll();
}
