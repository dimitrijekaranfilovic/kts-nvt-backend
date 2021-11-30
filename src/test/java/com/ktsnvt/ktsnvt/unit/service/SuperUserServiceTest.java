package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.exception.EmailAlreadyExistsException;
import com.ktsnvt.ktsnvt.exception.InvalidPasswordException;
import com.ktsnvt.ktsnvt.model.Authority;
import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import com.ktsnvt.ktsnvt.repository.SuperUserRepository;
import com.ktsnvt.ktsnvt.service.AuthorityService;
import com.ktsnvt.ktsnvt.service.SalaryService;
import com.ktsnvt.ktsnvt.service.SuperUserService;
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

    @Test
    void deleteManager_whenCalledWithValidId_isSuccess() {
        // GIVEN
        var manager = new SuperUser("name", "surname", new Authority("MANAGER"), "name.surname@gmail.com", "test123", SuperUserType.MANAGER);
        var managerId = 999;
        manager.setId(managerId);
        SuperUserService superUserServiceSpy = spy(superUserService);
        doNothing().when(salaryService).endActiveSalaryForUser(manager);
        doReturn(manager).when(superUserServiceSpy).readManagerForUpdate(manager.getId());

        // WHEN
        superUserServiceSpy.deleteManager(managerId);

        // THEN
        assertFalse(manager.getIsActive());
        verify(salaryService, times(1)).endActiveSalaryForUser(manager);
        verifyNoInteractions(superUserRepository);
    }

    @Test
    void updatePassword_whenCalledWithValidOldAndNewPasswords_isSuccess() {
        // GIVEN
        var oldPassword = "test123";
        var newPassword = "pera123";
        var superUser = new SuperUser("name", "surname", new Authority("MANAGER"), "name.surname@gmail.com", oldPassword, SuperUserType.MANAGER);
        var superUserId = 999;
        superUser.setId(superUserId);
        SuperUserService superUserServiceSpy = spy(superUserService);
        doReturn(superUser).when(superUserServiceSpy).read(superUser.getId());

        // WHEN
        superUserServiceSpy.updatePassword(superUserId, oldPassword, newPassword);

        // THEN
        assertEquals(newPassword, superUser.getPassword());
        verify(passwordEncoder, times(1)).encode(oldPassword);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verifyNoInteractions(superUserRepository);
    }

    @Test
    void updatePassword_whenCalledWithNotMatchingPasswords_throwsException() {
        // GIVEN
        var oldPasswordMistake = "ndgksdjgskdg";
        var oldPassword = "test123";
        var newPassword = "pera123";
        var superUser = new SuperUser("name", "surname", new Authority("MANAGER"), "name.surname@gmail.com", oldPassword, SuperUserType.MANAGER);
        var superUserId = 999;
        superUser.setId(superUserId);
        SuperUserService superUserServiceSpy = spy(superUserService);
        doReturn(superUser).when(superUserServiceSpy).read(superUser.getId());

        // WHEN
        assertThrows(InvalidPasswordException.class, () -> superUserServiceSpy.updatePassword(superUserId, oldPasswordMistake, newPassword));

        // THEN
        assertEquals(oldPassword, superUser.getPassword());
        verify(passwordEncoder, times(1)).encode(oldPasswordMistake);
        verifyNoInteractions(superUserRepository);
    }

    @Test
    void update_whenCalledWithValidEmailChange_isSuccess() {
        // GIVEN
        var newEMail = "test@gmail.com";
        var superUser = new SuperUser("name", "surname", new Authority("MANAGER"), "name.surname@gmail.com", "test123", SuperUserType.MANAGER);
        var superUserId = 999;
        superUser.setId(superUserId);
        SuperUserService superUserServiceSpy = spy(superUserService);
        doReturn(superUser).when(superUserServiceSpy).read(superUser.getId());
        doReturn(Optional.empty()).when(superUserRepository).findByEmail(newEMail);

        // WHEN
        superUserServiceSpy.update(superUserId, "pera", "peric", newEMail);

        // THEN
        assertEquals(newEMail, superUser.getEmail());
        assertEquals("pera", superUser.getName());
        assertEquals("peric", superUser.getSurname());
    }

    @Test
    void update_whenCalledWithNoEmailChange_isSuccess() {
        // GIVEN
        var superUser = new SuperUser("name", "surname", new Authority("MANAGER"), "name.surname@gmail.com", "test123", SuperUserType.MANAGER);
        var superUserId = 999;
        superUser.setId(superUserId);
        SuperUserService superUserServiceSpy = spy(superUserService);
        doReturn(superUser).when(superUserServiceSpy).read(superUser.getId());
        doReturn(Optional.of(superUser)).when(superUserRepository).findByEmail(superUser.getEmail());

        // WHEN
        superUserServiceSpy.update(superUserId, "pera", "peric", superUser.getEmail());

        // THEN
        assertEquals("pera", superUser.getName());
        assertEquals("peric", superUser.getSurname());
    }

    @Test
    void update_whenCalledWithDuplicateEmail_throwsException() {
        // GIVEN
        var newEmail = "pera@gmail.com";
        var superUser = new SuperUser("name", "surname", new Authority("MANAGER"), "name.surname@gmail.com", "test123", SuperUserType.MANAGER);
        var superUserId = 999;
        superUser.setId(superUserId);
        var userWithSameEmail = new SuperUser();
        userWithSameEmail.setId(888);
        SuperUserService superUserServiceSpy = spy(superUserService);
        doReturn(superUser).when(superUserServiceSpy).read(superUser.getId());
        doReturn(Optional.of(userWithSameEmail)).when(superUserRepository).findByEmail(newEmail);

        // WHEN
        assertThrows(EmailAlreadyExistsException.class, () -> superUserServiceSpy.update(superUserId, "pera", "peric", newEmail));

        // THEN
        assertNotEquals(newEmail, superUser.getEmail());
        assertNotEquals("pera", superUser.getName());
        assertNotEquals("peric", superUser.getSurname());
    }
}
