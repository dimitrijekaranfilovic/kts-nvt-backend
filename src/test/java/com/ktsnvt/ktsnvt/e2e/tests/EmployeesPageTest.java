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
    void employeePage_happyFlow() throws InterruptedException {
        var driver = initDriver();
        var navbar = PageFactory.initElements(driver, Navbar.class);
        var loginPage = PageFactory.initElements(driver, LoginPage.class);
        var employeesPage = PageFactory.initElements(driver, EmployeesPage.class);

        navbar.navigateLogin();
        loginPage.setEmail("email2@email.com");
        loginPage.setPassword("password");
        loginPage.clickLogin();

        assertTrue(Utilities.checkUrl(driver, "/employees"));

        assertTrue(employeesPage.checkEmployeeTableRows(3));

        employeesPage.search("sv", 10.0, 1000.0);
        assertTrue(employeesPage.checkEmployeeTableRows(1));

        employeesPage.search("sv", 800.0, 1000.0);
        assertTrue(employeesPage.checkEmployeeTableRows(0));

        employeesPage.resetSearchForm();
        assertTrue(employeesPage.checkEmployeeTableRows(3));

        employeesPage.clickCreateEmployee();
        employeesPage.setName("pera");
        employeesPage.setSurname("peric");
        employeesPage.setPin("9999");
        employeesPage.setSalary(300.0);
        employeesPage.clickSaveButton();
        assertTrue(employeesPage.checkEmployeeTableRows(4));
        assertEquals("pera", employeesPage.getLastEmployeeName());
        assertEquals("300", employeesPage.getLastEmployeeSalary());

        employeesPage.clickUpdateLastEmployee();
        employeesPage.setName("NOVO IME");
        employeesPage.clickSaveButton();
        assertTrue(employeesPage.checkEmployeeTableRows(4));
        Thread.sleep(500);
        assertEquals("NOVO IME", employeesPage.getLastEmployeeName());

        employeesPage.clickUpdateLastEmployeeSalary();
        employeesPage.setUpdateSalary(420.0);
        employeesPage.clickSaveSalaryButton();
        assertTrue(employeesPage.checkEmployeeTableRows(4));
        Thread.sleep(500);
        assertEquals("420", employeesPage.getLastEmployeeSalary());

        employeesPage.clickDeleteLastEmployee();
        employeesPage.clickYesButton();
        assertTrue(employeesPage.checkEmployeeTableRows(3));

        driver.quit();
    }
}
