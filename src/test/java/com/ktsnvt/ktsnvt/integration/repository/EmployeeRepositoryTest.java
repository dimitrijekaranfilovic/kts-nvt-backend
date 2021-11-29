package com.ktsnvt.ktsnvt.integration.repository;

import com.ktsnvt.ktsnvt.model.Authority;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestConstructor;

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setEverythingUp() {
        Authority a = new Authority("aaa");
        entityManager.persist(a);
        entityManager.persist(new Employee("aaa", "aaa", a, "5341", EmployeeType.WAITER));
        entityManager.persist(new Employee("aaa", "aaa", a, "5342", EmployeeType.WAITER));
    }

    @Test
    public void test1() {
        Assertions.assertEquals(true, employeeRepository.findByPin("5341").isPresent());
    }

    @Test
    public void test2() {
        Assertions.assertEquals(2, employeeRepository.findAll().size());
    }

    @Test
    public void test3() {
        Assertions.assertEquals(2, employeeRepository.findAll().size());
    }
}
