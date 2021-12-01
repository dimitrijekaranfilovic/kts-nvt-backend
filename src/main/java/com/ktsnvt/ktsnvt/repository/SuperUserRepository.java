package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

public interface SuperUserRepository extends JpaRepository<SuperUser, Integer> {

    @Query("select u from SuperUser u where u.id = :id and u.isActive = true")
    Optional<SuperUser> findOneById(Integer id);

    @Query("select u from SuperUser u where u.email = :email and u.isActive = true")
    Optional<SuperUser> findByEmail(String email);

    @Query("select u from SuperUser u where ((lower(u.name) like concat('%', :query, '%')) or (lower(u.surname) like concat('%', :query, '%')))" +
            " and (:salaryFrom is null or u.currentSalary >= :salaryFrom) and (:salaryTo is null or u.currentSalary <= :salaryTo) " +
            " and (:type is null or u.type = : type) and u.isActive = true")
    Page<SuperUser> findAll(String query, BigDecimal salaryFrom, BigDecimal salaryTo, SuperUserType type, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from SuperUser u where u.id = :id and u.type = :type and u.isActive = true")
    Optional<SuperUser> getSuperUserForUpdate(Integer id, SuperUserType type);

    Stream<SuperUser> findAllByIsActiveTrue();
}
