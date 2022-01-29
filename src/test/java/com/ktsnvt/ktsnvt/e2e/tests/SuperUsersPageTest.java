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
class SuperUsersPageTest extends BaseE2ETest {

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

        var numOfItemsAndPages = superUsersPage.getPaginationInformation();

        superUsersPage.search("nikola", 10d, 560d, "ADMIN");
        assertTrue(superUsersPage.checkSearchQueryResultsOnAllPages(
                "nikola", v -> (v >= 10d && v <= 560d), "ADMIN"));

        superUsersPage.search("sn", 100d, 10000d, "MANAGER");
        assertTrue(superUsersPage.checkSearchQueryResultsOnAllPages(
                "sn", v -> (v >= 100d && v <= 10000d), "MANAGER"));

        superUsersPage.search("ice", 1000000d, 0d, "");
        assertTrue(superUsersPage.checkEmptyResultsPage());

        superUsersPage.resetSearchForm();

        assertTrue(superUsersPage.checkPaginationInformationMatching(numOfItemsAndPages));

        var paginationBeforeAdding = superUsersPage.getPaginationInformation();
        superUsersPage.clickCreateSuperUser();
        superUsersPage.setName("pera");
        superUsersPage.setSurname("peric");
        superUsersPage.setEmail("nepostojeci.email@gmail.com");
        superUsersPage.setPassword("password");
        superUsersPage.setConfirmPassword("password");
        superUsersPage.setSalary(300.0);
        superUsersPage.clickSaveButton();
        superUsersPage.goToLastPage();
        assertTrue(superUsersPage.checkCurrentTotalElements(paginationBeforeAdding, +1d));
        assertTrue(superUsersPage.checkLastSuperUserDetails("pera", "peric", "nepostojeci.email@gmail.com", "Manager", 300.0));

        var paginationBeforeSalaryUpdate = superUsersPage.getPaginationInformation();
        superUsersPage.goToLastPage();
        superUsersPage.clickUpdateLastSuperUserSalary();
        superUsersPage.setUpdateSalary(999.0);
        superUsersPage.clickSaveSalaryButton();
        superUsersPage.goToLastPage();
        assertTrue(superUsersPage.checkPaginationInformationMatching(paginationBeforeSalaryUpdate));
        assertTrue(superUsersPage.checkLastSuperUserDetails("pera", "peric", "nepostojeci.email@gmail.com", "Manager", 999.0));

        var paginationBeforeDelete = superUsersPage.getPaginationInformation();
        superUsersPage.goToLastPage();
        superUsersPage.clickDeleteLastSuperUser();
        superUsersPage.clickYesButton();
        assertTrue(superUsersPage.checkCurrentTotalElements(paginationBeforeDelete, -1d));

        driver.quit();
    }
}
