package com.ktsnvt.ktsnvt.e2e.tests;

import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.ChefAndBartenderPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.Navbar;
import com.ktsnvt.ktsnvt.e2e.tests.utilities.Utilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BartenderPageTest extends BaseE2ETest {

    @Test
    void bartenderTest_happyFlow() {
        var driver = initDriver();
        Navbar navbar = PageFactory.initElements(driver, Navbar.class);
        ChefAndBartenderPage chefPage = PageFactory.initElements(driver, ChefAndBartenderPage.class);

        navbar.navigateBartender();
        assertTrue(Utilities.checkUrl(driver, "/bartender"));

        assertTrue(chefPage.checkNewTableRows(2));
        assertTrue(chefPage.checkPreparingTableRows(1));

        chefPage.clickFinishOldestNew();
        chefPage.setPin("5678");
        chefPage.clickConfirmPin();

        assertTrue(chefPage.checkNewTableRows(1));
        assertTrue(chefPage.checkPreparingTableRows(1));

        chefPage.clickPrepareOldestNew();
        chefPage.setPin("5678");
        chefPage.clickConfirmPin();

        assertTrue(chefPage.checkNewTableRows(0));
        assertTrue(chefPage.checkPreparingTableRows(2));

        chefPage.clickFinishOldestPreparing();
        chefPage.setPin("5678");
        chefPage.clickConfirmPin();

        assertTrue(chefPage.checkNewTableRows(0));
        assertTrue(chefPage.checkPreparingTableRows(1));
        driver.close();
    }
}
