package com.ktsnvt.ktsnvt.e2e.tests;

import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.EmployeesPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.LoginPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.Navbar;
import com.ktsnvt.ktsnvt.e2e.tests.utilities.Utilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeesPageTest extends BaseE2ETest {

    @Test
    void employeePage_happyFlow() {
        var driver = initDriver();
        var navbar = PageFactory.initElements(driver, Navbar.class);
        var loginPage = PageFactory.initElements(driver, LoginPage.class);
        var employeesPage = PageFactory.initElements(driver, EmployeesPage.class);

        navbar.navigateLogin();
        loginPage.setEmail("email2@email.com");
        loginPage.setPassword("password");
        loginPage.clickLogin();

        assertTrue(Utilities.checkUrl(driver, "/employees"));

        var numOfItemsAndPages = employeesPage.getPaginationInformation();

        employeesPage.search("ice", 10d, 100d, "WAITER");
        assertTrue(employeesPage.checkSearchQueryResultsOnAllPages(
                "ice", v -> (v >= 10d && v <= 100d), "WAITER"));

        employeesPage.search("ice", 1000000d, 0d, "WAITER");
        assertTrue(employeesPage.checkEmptyResultsPage());

        employeesPage.search("all", 100d, 10000d, "WAITER");
        assertTrue(employeesPage.checkSearchQueryResultsOnAllPages(
                "all", v -> (v >= 100d && v <= 10000d), "WAITER"));

        employeesPage.search("all", 100d, 10000d, "WAITER");
        assertTrue(employeesPage.checkSearchQueryResultsOnAllPages(
                "all", v -> (v >= 100d && v <= 10000d), "WAITER"));

        employeesPage.search("all", 0d, 322d, "WAITER");
        assertTrue(employeesPage.checkSearchQueryResultsOnAllPages(
                "all", v -> (v >= 0d && v <= 322d), "WAITER"));

        employeesPage.search("all", 42d, 440d, "WAITER");
        assertTrue(employeesPage.checkSearchQueryResultsOnAllPages(
                "all", v -> (v >= 42d && v <= 440d), "WAITER"));

        employeesPage.resetSearchForm();

        assertTrue(employeesPage.checkPaginationInformationMatching(numOfItemsAndPages));

//        employeesPage.clickUpdateLastMenuItemPriceButton();
//        employeesPage.setUpdatePriceField(496d);
//        employeesPage.clickSaveChangesButton();
//        assertTrue(employeesPage.checkLastMenuItemPriceUpdated(496d));
//
//        var numOfItemsAndPagesBeforeDeletion = employeesPage.getPaginationInformation();
//        employeesPage.clickDeactivateLastMenuItem();
//        employeesPage.clickConfirmDeletion();
//        assertTrue(employeesPage.checkNumberOfItemsAfterDeactivation(numOfItemsAndPagesBeforeDeletion));

        driver.quit();
    }
}
