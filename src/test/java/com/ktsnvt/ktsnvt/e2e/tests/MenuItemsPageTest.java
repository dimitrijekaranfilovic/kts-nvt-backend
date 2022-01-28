package com.ktsnvt.ktsnvt.e2e.tests;

import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.LoginPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.MenuItemsPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.Navbar;
import com.ktsnvt.ktsnvt.e2e.tests.utilities.Utilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MenuItemsPageTest extends BaseE2ETest {

    @Test
    void menuItemsHappyFlow() throws InterruptedException {
        var driver = initDriver();
        var navbar = PageFactory.initElements(driver, Navbar.class);
        var loginPage = PageFactory.initElements(driver, LoginPage.class);
        var menuItemsPage = PageFactory.initElements(driver, MenuItemsPage.class);


        navbar.navigateLogin();
        loginPage.setEmail("email2@email.com");
        loginPage.setPassword("password");
        loginPage.clickLogin();

        assertTrue(Utilities.checkUrl(driver, "/employees"));

        navbar.navigateMenuItems();
        assertTrue(Utilities.checkUrl(driver, "/menu-items"));

        var numItems = menuItemsPage.countItems();

        menuItemsPage.search("ice", 10d, 100d);
        assertTrue(menuItemsPage.checkQuerySearchResults("ice"));
        assertTrue(menuItemsPage.checkPriceUpperBound(100d));


//        driver.close();
    }

}
