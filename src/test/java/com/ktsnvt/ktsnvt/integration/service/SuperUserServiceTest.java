package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.EmailAlreadyExistsException;
import com.ktsnvt.ktsnvt.exception.InvalidPasswordException;
import com.ktsnvt.ktsnvt.exception.ManagerNotFoundException;
import com.ktsnvt.ktsnvt.exception.SuperUserNotFoundException;
import com.ktsnvt.ktsnvt.model.Authority;
import com.ktsnvt.ktsnvt.model.SuperUser;
import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import com.ktsnvt.ktsnvt.service.impl.SuperUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Stream;

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

    @Test
    void readAll_whenCalledNormally_isSuccess() {
        var userStream = superUserService.readAll();
        assertEquals(3, userStream.count());
    }

    @ParameterizedTest
    @ValueSource(strings = {"email1@email.com", "email2@email.com"})
    void loadUserByUsername_whenCalledWithExistingUsername_isSuccess(String username) {
        var user = superUserService.loadUserByUsername(username);
        assertEquals(username, user.getUsername());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "pera@pera.com", "Email2@gmail.com"})
    void loadUserByUsername_whenCalledWithNonExistingUsername_throwsException(String username) {
        assertThrows(SuperUserNotFoundException.class, () -> superUserService.loadUserByUsername(username));
    }

    @ParameterizedTest
    @ValueSource(strings = {"email1@email.com", "email2@email.com"})
    void findByEmail_whenCalledWithExistingEmail_isSuccess(String email) {
        var user = superUserService.findByEmail(email);
        assertEquals(email, user.getEmail());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "pera@pera.com", "Email2@gmail.com"})
    void findByEmail_whenCalledWithNonExistingEmail_throwsException(String email) {
        assertThrows(SuperUserNotFoundException.class, () -> superUserService.findByEmail(email));
    }

    @ParameterizedTest
    @ValueSource(ints = {4})
    void readManagerForUpdate_whenCalledWithValidId_isSuccess(Integer id) {
        var manager = superUserService.readManagerForUpdate(id);
        assertEquals(id, manager.getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 8})
    void readManagerForUpdate_whenCalledWithInvalidId_throwsException(Integer id) {
        assertThrows(ManagerNotFoundException.class, () -> superUserService.readManagerForUpdate(id));
    }

    @ParameterizedTest
    @MethodSource("provideTestsForPaginatedRead")
    void read_whenCalledWithPagination_isSuccess(String query, BigDecimal salaryFrom, BigDecimal salaryTo, SuperUserType type, Pageable pageable, int expected) {
        var page = superUserService.read(query, salaryFrom, salaryTo, type, pageable);
        assertEquals(expected, page.getTotalElements());
    }

    @Test
    void deleteManager_whenCalledWithValidId_isSuccess() {
        assertDoesNotThrow(() -> superUserService.deleteManager(4));
    }

    @Test
    void deleteManager_whenCalledWithInvalidId_throwsException() {
        assertThrows(ManagerNotFoundException.class, () -> superUserService.deleteManager(5));
    }

    @Test
    void updatePassword_whenCalledWithValidData_isSuccess() {
        assertDoesNotThrow(() -> superUserService.updatePassword(4, "password", "something new"));
    }

    @Test
    void updatePassword_whenCalledWithInvalidOldPassword_throwsException() {
        assertThrows(InvalidPasswordException.class, () -> superUserService.updatePassword(4, "wrong", "something new"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"email1@email.com", "peraperic@gmail.com"})
    void update_whenCalledWithValidData_isSuccess(String email) {
        assertDoesNotThrow(() -> superUserService.update(4, "pera", "peric", email));
    }

    @Test
    void update_whenCalledWithDuplicateEmail_throwsException() {
        assertThrows(EmailAlreadyExistsException.class, () -> superUserService.update(4, "pera", "peric", "email2@email.com"));
    }

    @ParameterizedTest
    @EnumSource(SuperUserType.class)
    void create_whenCalledWithValidData_isSuccess(SuperUserType superUserType) {
        var superUser = new SuperUser("dusan", "dusan", null, "dusan@email.com", "password", superUserType);
        var createdUser = superUserService.create(superUser);
        assertTrue(createdUser.getId() > 0);
        assertEquals(superUserType.toString(), createdUser.getAuthority().getName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"email1@email.com", "email2@email.com"})
    void create_whenCalledWithDuplicateEmail_throwsException(String email) {
        var superUser = new SuperUser("dusan", "dusan", null, email, "password", SuperUserType.ADMIN);
        assertThrows(EmailAlreadyExistsException.class, () -> superUserService.create(superUser));
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> provideTestsForPaginatedRead() {
        var pageable = PageRequest.of(0, 10, Sort.unsorted());
        return Stream.of(
                Arguments.of("", null, null, null, pageable, 3),
                Arguments.of("iKO", null, null, null, pageable, 1),
                Arguments.of("iKO", null, BigDecimal.valueOf(500), null, pageable, 1),
                Arguments.of("iKO", null, null, SuperUserType.MANAGER, pageable, 0),
                Arguments.of("", BigDecimal.valueOf(200), BigDecimal.valueOf(600), null, pageable, 2)
        );
    }
}
