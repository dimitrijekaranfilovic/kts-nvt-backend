package com.ktsnvt.ktsnvt.e2e.tests;

import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.Navbar;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.WaiterPage;
import com.ktsnvt.ktsnvt.e2e.tests.utilities.Utilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WaiterPageTest extends BaseE2ETest {

    @Test
    void waiterPage_happyTest() throws InterruptedException {
        var driver = initDriver();
        Navbar navbar = PageFactory.initElements(driver, Navbar.class);
        WaiterPage waiterPage = PageFactory.initElements(driver, WaiterPage.class);

        navbar.navigateWaiter();

        assertTrue(Utilities.checkUrl(driver, "/waiter"));
        assertEquals(4, waiterPage.getTabNumber());
        assertEquals(2, waiterPage.getCanvasNumber()); // 2 canvases shown in component (1 for drawing second for dragging);

        waiterPage.clickOnTable(120, 120);
        assertTrue(Utilities.checkUrl(driver, "/order/1"));

        driver.quit();
    }
}
