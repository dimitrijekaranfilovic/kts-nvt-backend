package com.ktsnvt.ktsnvt.e2e.tests;

import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.AdminSectionsPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.LayoutPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.LoginPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.Navbar;
import com.ktsnvt.ktsnvt.e2e.tests.utilities.Utilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdminLayoutPageTest extends BaseE2ETest {

    @Test
    void adminLayoutPage_happyFlow() throws InterruptedException {
        var driver = initDriver();
        Navbar nav = PageFactory.initElements(driver, Navbar.class);
        LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
        AdminSectionsPage sectionsPage = PageFactory.initElements(driver, AdminSectionsPage.class);
        LayoutPage layoutPage = PageFactory.initElements(driver, LayoutPage.class);

        nav.navigateLogin();
        loginPage.setEmail("email2@email.com");
        loginPage.setPassword("password");
        loginPage.clickLogin();

        assertTrue(Utilities.checkUrl(driver, "/employees"));

        nav.navigateSectionsAdmin();

        assertTrue(Utilities.checkUrl(driver, "/sections/manage"));

        sectionsPage.clickLayoutButton(1);

        assertTrue(Utilities.checkUrl(driver, "/sections/2/layout"));

        layoutPage.insertNewTable("11", "10", "10");

        layoutPage.clickOnTable(10, 10);

        layoutPage.deleteSelectedTable();

        layoutPage.clickOnTable(10, 10);

        assertFalse(Utilities.checkIfButtonIsEnabled(driver, By.id("deleteTable")));

        driver.quit();
    }
}
