package com.ktsnvt.ktsnvt.e2e.tests;

import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.ChefAndBartenderPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.Navbar;
import com.ktsnvt.ktsnvt.e2e.tests.utilities.Utilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChefPageTest extends BaseE2ETest {

    @Test
    void chefPage_happyFlow() {
        var driver = initDriver();
        Navbar navbar = PageFactory.initElements(driver, Navbar.class);
        ChefAndBartenderPage chefPage = PageFactory.initElements(driver, ChefAndBartenderPage.class);

        navbar.navigateChef();
        assertTrue(Utilities.checkUrl(driver, "/chef"));

        int initialNewRequests = chefPage.getNewTableRows();
        int initialPreparingRequests = chefPage.getPreparingTableRows();

        chefPage.clickFinishOldestNew();
        chefPage.setPin("1234");
        chefPage.clickConfirmPin();

        assertTrue(chefPage.checkNewTableRows(initialNewRequests - 1));
        assertTrue(chefPage.checkPreparingTableRows(initialPreparingRequests));

        chefPage.clickPrepareOldestNew();
        chefPage.setPin("1234");
        chefPage.clickConfirmPin();

        assertTrue(chefPage.checkNewTableRows(initialNewRequests - 2));
        assertTrue(chefPage.checkPreparingTableRows(initialPreparingRequests + 1));

        chefPage.clickFinishOldestPreparing();
        chefPage.setPin("1234");
        chefPage.clickConfirmPin();

        assertTrue(chefPage.checkNewTableRows(initialNewRequests - 2));
        assertTrue(chefPage.checkPreparingTableRows(initialPreparingRequests));

        driver.quit();
    }
}
