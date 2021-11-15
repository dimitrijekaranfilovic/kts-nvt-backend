package com.ktsnvt.ktsnvt.repository;

import com.ktsnvt.ktsnvt.model.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary, Integer> {

    @Query("select s from Salary s where s.user.id = :id and s.endDate is null and s.isActive = true")
    Optional<Salary> findActiveForUser(Integer id);

    @Query("select sum(s.amount) from Salary s where s.startDate <= :date and (s.endDate is null or s.endDate > :date) and s.isActive = true")
    Optional<BigDecimal> readExpensesForDate(LocalDate date);
}
