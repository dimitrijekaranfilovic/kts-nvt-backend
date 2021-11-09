package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface SuperUserRepository extends JpaRepository<SuperUser, Integer> {

    Optional<SuperUser> findByEmail(String email);

    @Query("select u from SuperUser u where ((lower(u.name) like concat('%', :query, '%')) or (lower(u.surname) like concat('%', :query, '%')))" +
            " and u.currentSalary >= :salaryFrom and u.currentSalary <= :salaryTo and (:type is null or u.type = : type)")
    Page<SuperUser> findAll(String query, BigDecimal salaryFrom, BigDecimal salaryTo, SuperUserType type, Pageable pageable);
}
