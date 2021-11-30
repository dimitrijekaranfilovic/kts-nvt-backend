package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.exception.EmailAlreadyExistsException;
import com.ktsnvt.ktsnvt.model.Authority;
import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import com.ktsnvt.ktsnvt.repository.SuperUserRepository;
import com.ktsnvt.ktsnvt.service.AuthorityService;
import com.ktsnvt.ktsnvt.service.SalaryService;
import com.ktsnvt.ktsnvt.service.impl.SuperUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SuperUserServiceTest {
    @Mock
    private SuperUserRepository superUserRepository;
    @Mock
    private AuthorityService authorityService;
    @Mock
    private SalaryService salaryService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private SuperUserServiceImpl superUserService;

    @BeforeEach
    void setupPasswordEncoder() {
        doAnswer(returnsFirstArg()).when(passwordEncoder).encode(anyString());
    }

    @Test
    void create_whenCalledWithValidData_isSuccess() {
        // GIVEN
        var encodedPassword = "test123";
        var superUser = new SuperUser("name", "surname", null, "name.surname@gmail.com", "test123", SuperUserType.MANAGER);
        var managerAuthority = new Authority("MANAGER");
        doReturn(Optional.empty()).when(superUserRepository).findByEmail(superUser.getEmail());
        doReturn(managerAuthority).when(authorityService).findByName(managerAuthority.getName());
        doReturn(superUser).when(superUserRepository).save(superUser);

        // WHEN
        superUserService.create(superUser);

        // THEN
        assertEquals(managerAuthority, superUser.getAuthority());
        assertEquals(encodedPassword, superUser.getPassword());
        verify(authorityService, times(1)).findByName(managerAuthority.getName());
        verify(superUserRepository, times(1)).save(superUser);
        verify(passwordEncoder, times(1)).encode(superUser.getPassword());
    }

    @Test
    void create_whenCalledWithDuplicateEmail_throwsException() {
        // GIVEN
        var superUser = new SuperUser("name", "surname", null, "name.surname@gmail.com", "test123", SuperUserType.MANAGER);
        var managerAuthority = new Authority("MANAGER");
        doReturn(Optional.of(new SuperUser())).when(superUserRepository).findByEmail(superUser.getEmail());
        doReturn(managerAuthority).when(authorityService).findByName(managerAuthority.getName());
        doReturn(superUser).when(superUserRepository).save(superUser);

        // WHEN
        assertThrows(EmailAlreadyExistsException.class, () -> superUserService.create(superUser));

        // THEN
        assertNull(superUser.getAuthority());
        verifyNoInteractions(authorityService);
        verifyNoInteractions(passwordEncoder);
    }
}
