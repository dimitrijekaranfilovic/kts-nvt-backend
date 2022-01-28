package com.ktsnvt.ktsnvt.e2e.tests;

import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.LoginPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.Navbar;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.SectionsManagePage;
import com.ktsnvt.ktsnvt.e2e.tests.utilities.Utilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SectionsManagePageTest extends BaseE2ETest {

    @Test
    void sectionsManagePageTest_happyFlow() throws InterruptedException {
        var driver = initDriver();
        var navbar = PageFactory.initElements(driver, Navbar.class);
        var loginPage = PageFactory.initElements(driver, LoginPage.class);
        var sectionsManagePage = PageFactory.initElements(driver, SectionsManagePage.class);

        navbar.navigateLogin();
        loginPage.setEmail("email2@email.com");
        loginPage.setPassword("password");
        loginPage.clickLogin();
        assertTrue(Utilities.checkUrl(driver, "/employees"));

        navbar.navigateSectionsAdmin();
        assertTrue(Utilities.checkUrl(driver, "/sections/manage"));
        assertTrue(sectionsManagePage.checkSectionTableRows(4));

        sectionsManagePage.clickCreateSection();
        sectionsManagePage.setName("Nova Sekcija");
        sectionsManagePage.clickSaveButton();
        assertTrue(sectionsManagePage.checkSectionTableRows(5));
        assertEquals("Nova Sekcija", sectionsManagePage.getLastSectionName());

        sectionsManagePage.clickUpdateLastSection();
        sectionsManagePage.setName("Izmenjena sekcija");
        sectionsManagePage.clickSaveButton();
        Thread.sleep(500);
        assertTrue(sectionsManagePage.checkSectionTableRows(5));
        assertEquals("Izmenjena sekcija", sectionsManagePage.getLastSectionName());

        var id = sectionsManagePage.getLastSectionId();
        sectionsManagePage.clickViewSeatingLayoutLastSection();
        Utilities.checkUrl(driver, String.format("/sections/%s/layout", id));
        navbar.navigateSectionsAdmin();
        assertTrue(Utilities.checkUrl(driver, "/sections/manage"));

        sectionsManagePage.clickDeleteLastSection();
        sectionsManagePage.clickYesButton();
        assertTrue(sectionsManagePage.checkSectionTableRows(4));

        driver.quit();
    }
}
