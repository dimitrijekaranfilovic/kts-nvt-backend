package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.SuperUserNotFoundException;
import com.ktsnvt.ktsnvt.service.impl.SuperUserServiceImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SuperUserServiceTest {
    @Autowired
    private SuperUserServiceImpl superUserService;

    @ParameterizedTest
    @ValueSource(ints = {4, 5})
    void read_whenCalledWithValidId_isSuccess(Integer id) {
        var user = superUserService.read(id);
        assertEquals(id, user.getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 10, 15, 18})
    void read_whenCalledWithInvalidId_throwsException(Integer id) {
        assertThrows(SuperUserNotFoundException.class, () -> superUserService.read(id));
    }
}
