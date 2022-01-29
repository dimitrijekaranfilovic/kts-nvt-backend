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

        employeesPage.search("jovan", 10d, 560d, "CHEF");
        assertTrue(employeesPage.checkSearchQueryResultsOnAllPages(
                "jovan", v -> (v >= 10d && v <= 560d), "CHEF"));

        employeesPage.search("et", 100d, 10000d, "BARTENDER");
        assertTrue(employeesPage.checkSearchQueryResultsOnAllPages(
                "et", v -> (v >= 100d && v <= 10000d), "BARTENDER"));

        employeesPage.search("ice", 1000000d, 0d, "");
        assertTrue(employeesPage.checkEmptyResultsPage());

        employeesPage.resetSearchForm();

        assertTrue(employeesPage.checkPaginationInformationMatching(numOfItemsAndPages));

        var paginationBeforeAdding = employeesPage.getPaginationInformation();
        employeesPage.clickCreateEmployee();
        employeesPage.setName("pera");
        employeesPage.setSurname("peric");
        employeesPage.setPin("9999");
        employeesPage.setSalary(300.0);
        employeesPage.clickSaveButton();
        employeesPage.goToLastPage();
        assertTrue(employeesPage.checkCurrentTotalElements(paginationBeforeAdding, +1d));
        assertTrue(employeesPage.checkLastEmployeeDetails("pera", "peric", "9999", 300.0));

        var paginationBeforeUpdate = employeesPage.getPaginationInformation();
        employeesPage.goToLastPage();
        employeesPage.clickUpdateLastEmployee();
        employeesPage.setName("updated");
        employeesPage.setSurname("employee");
        employeesPage.clickSaveButton();
        assertTrue(employeesPage.checkPaginationInformationMatching(paginationBeforeUpdate));
        assertTrue(employeesPage.checkLastEmployeeDetails("updated", "employee", "9999", 300.0));

        var paginationBeforeSalaryUpdate = employeesPage.getPaginationInformation();
        employeesPage.goToLastPage();
        employeesPage.clickUpdateLastEmployeeSalary();
        employeesPage.setUpdateSalary(999.0);
        employeesPage.clickUpdateSalary();
        employeesPage.goToLastPage();
        assertTrue(employeesPage.checkPaginationInformationMatching(paginationBeforeSalaryUpdate));
        assertTrue(employeesPage.checkLastEmployeeDetails("updated", "employee", "9999", 999.0));

        var paginationBeforeDelete = employeesPage.getPaginationInformation();
        employeesPage.goToLastPage();
        employeesPage.clickDeleteLastEmployee();
        employeesPage.clickConfirm();
        assertTrue(employeesPage.checkCurrentTotalElements(paginationBeforeDelete, -1d));

        driver.quit();
    }
}
