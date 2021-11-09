package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary, Integer> {

    @Query("select s from Salary s where s.user.id = :id and s.endDate is null")
    Optional<Salary> findActiveForUser(Integer id);
}
