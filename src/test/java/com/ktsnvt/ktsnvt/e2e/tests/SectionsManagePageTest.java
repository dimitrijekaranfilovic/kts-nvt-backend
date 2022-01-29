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

        var sectionsBeforeInsert = sectionsManagePage.getNumberOfElements();
        sectionsManagePage.clickCreateSection();
        sectionsManagePage.setName("Nova Sekcija");
        sectionsManagePage.clickSaveButton();
        assertEquals(sectionsBeforeInsert + 1, sectionsManagePage.getNumberOfElements());
        assertTrue(sectionsManagePage.checkLastSectionName("Nova Sekcija"));

        var sectionsBeforeUpdate = sectionsManagePage.getNumberOfElements();
        sectionsManagePage.clickUpdateLastSection();
        sectionsManagePage.setName("Izmenjena sekcija");
        sectionsManagePage.clickSaveButton();
        assertEquals(sectionsBeforeUpdate, sectionsManagePage.getNumberOfElements());
        assertTrue(sectionsManagePage.checkLastSectionName("Izmenjena sekcija"));

        var id = sectionsManagePage.getLastSectionId();
        sectionsManagePage.clickViewSeatingLayoutLastSection();
        Utilities.checkUrl(driver, String.format("/sections/%s/layout", id));
        navbar.navigateSectionsAdmin();
        assertTrue(Utilities.checkUrl(driver, "/sections/manage"));

        var sectionsBeforeDelete = sectionsManagePage.getNumberOfElements();
        sectionsManagePage.clickDeleteLastSection();
        sectionsManagePage.clickYesButton();
        assertEquals(sectionsBeforeDelete - 1, sectionsManagePage.getNumberOfElements());

        driver.quit();
    }
}
