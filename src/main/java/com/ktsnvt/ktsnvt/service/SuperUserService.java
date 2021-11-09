package com.ktsnvt.ktsnvt.service;

import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface SuperUserService {
    SuperUser create(SuperUser superUser);

    Page<SuperUser> read(String query, BigDecimal salaryFrom, BigDecimal salaryTo, SuperUserType type, Pageable pageable);

    void deleteManager(Integer id);

    void updatePassword(Integer id, String oldPassword, String newPassword);
}
