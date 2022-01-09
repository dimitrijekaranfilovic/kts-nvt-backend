package com.ktsnvt.ktsnvt.e2e.tests;

import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.LoginPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.Navbar;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.SuperUsersPage;
import com.ktsnvt.ktsnvt.e2e.tests.utilities.Utilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SuperUsersPageTest extends BaseE2ETest {

    @Test
    void superUsersPage_happyFlow() throws InterruptedException {
        var driver = initDriver();
        var navbar = PageFactory.initElements(driver, Navbar.class);
        var loginPage = PageFactory.initElements(driver, LoginPage.class);
        var superUsersPage = PageFactory.initElements(driver, SuperUsersPage.class);

        navbar.navigateLogin();
        loginPage.setEmail("email2@email.com");
        loginPage.setPassword("password");
        loginPage.clickLogin();

        assertTrue(Utilities.checkUrl(driver, "/employees"));

        navbar.navigateSuperUser();
        assertTrue(Utilities.checkUrl(driver, "/super-users"));

        assertTrue(superUsersPage.checkSuperUsersTableRows(2));

        superUsersPage.search("ni", 10.0, 1000.0);
        assertTrue(superUsersPage.checkSuperUsersTableRows(1));

        superUsersPage.search("ni", 800.0, 1000.0);
        assertTrue(superUsersPage.checkSuperUsersTableRows(0));

        superUsersPage.resetSearchForm();
        assertTrue(superUsersPage.checkSuperUsersTableRows(2));

        superUsersPage.clickCreateSuperUser();
        superUsersPage.setName("pera");
        superUsersPage.setSurname("peric");
        superUsersPage.setEmail("peraperic@pera.com");
        superUsersPage.setPassword("password");
        superUsersPage.setConfirmPassword("password");
        superUsersPage.setSalary(300.0);
        superUsersPage.clickSaveButton();
        assertTrue(superUsersPage.checkSuperUsersTableRows(3));
        assertEquals("pera", superUsersPage.getLastSuperUserName());
        assertEquals("300", superUsersPage.getLastSuperUserSalary());

        superUsersPage.clickUpdateLastSuperUserSalary();
        superUsersPage.setUpdateSalary(420.0);
        superUsersPage.clickSaveSalaryButton();
        assertTrue(superUsersPage.checkSuperUsersTableRows(3));
        Thread.sleep(500);
        assertEquals("420", superUsersPage.getLastSuperUserSalary());

        superUsersPage.clickDeleteLastSuperUser();
        superUsersPage.clickYesButton();
        assertTrue(superUsersPage.checkSuperUsersTableRows(2));

        driver.quit();
    }
}
