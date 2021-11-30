package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.AuthorityNotFoundException;
import com.ktsnvt.ktsnvt.service.impl.AuthorityServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthorityServiceTest {
    @Autowired
    private AuthorityServiceImpl authorityService;

    @Test
    void findByName_whenCalledWithValidName_isSuccess() {
        var waiterAuthorityDbId = 3;
        var authority = authorityService.findByName("WAITER");
        assertEquals(waiterAuthorityDbId, authority.getId());
    }

    @Test
    void findByName_whenCalledWithInvalidName_throwsException() {
        assertThrows(AuthorityNotFoundException.class, () -> authorityService.findByName("Something not found."));
    }
}
