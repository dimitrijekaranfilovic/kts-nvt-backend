package com.ktsnvt.ktsnvt.e2e.tests;

import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.ChefAndBartenderPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.Navbar;
import com.ktsnvt.ktsnvt.e2e.tests.utilities.Utilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BartenderPageTest extends BaseE2ETest {

    @Test
    void bartenderTest_happyFlow() {
        var driver = initDriver();
        Navbar navbar = PageFactory.initElements(driver, Navbar.class);
        ChefAndBartenderPage bartenderPage = PageFactory.initElements(driver, ChefAndBartenderPage.class);

        navbar.navigateBartender();
        assertTrue(Utilities.checkUrl(driver, "/bartender"));

        int initialNewRequests = bartenderPage.getNewTableRows();
        int initialPreparingRequests = bartenderPage.getPreparingTableRows();

        bartenderPage.clickFinishOldestNew();
        bartenderPage.setPin("5678");
        bartenderPage.clickConfirmPin();

        assertTrue(bartenderPage.checkNewTableRows(initialNewRequests - 1));
        assertTrue(bartenderPage.checkPreparingTableRows(initialPreparingRequests));

        bartenderPage.clickPrepareOldestNew();
        bartenderPage.setPin("5678");
        bartenderPage.clickConfirmPin();

        assertTrue(bartenderPage.checkNewTableRows(initialNewRequests - 2));
        assertTrue(bartenderPage.checkPreparingTableRows(initialPreparingRequests + 1));

        bartenderPage.clickFinishOldestPreparing();
        bartenderPage.setPin("5678");
        bartenderPage.clickConfirmPin();

        assertTrue(bartenderPage.checkNewTableRows(initialNewRequests - 2));
        assertTrue(bartenderPage.checkPreparingTableRows(initialPreparingRequests));
        driver.close();
    }
}
