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
        assertTrue(menuItemsPage.checkPriceBound(v -> (v >= 10d)));
        assertTrue(menuItemsPage.checkPriceBound(v -> (v <= 100d)));

        menuItemsPage.search("some query that is not found in the results", 0d, 10000d);
        assertTrue(menuItemsPage.checkEmptyResultsPage());

        menuItemsPage.search("all", 100d, 10000d);
        assertTrue(menuItemsPage.checkQuerySearchResults("all"));
        assertTrue(menuItemsPage.checkPriceBound(v -> (v >= 100d)));
        assertTrue(menuItemsPage.checkPriceBound(v -> (v <= 10000d)));

        menuItemsPage.search("all", 100d, 10000d);
        assertTrue(menuItemsPage.checkQuerySearchResults("all"));
        assertTrue(menuItemsPage.checkPriceBound(v -> (v >= 100d)));
        assertTrue(menuItemsPage.checkPriceBound(v -> (v <= 10000d)));

        menuItemsPage.search("all", 0d, 322d);
        assertTrue(menuItemsPage.checkQuerySearchResults("all"));
        assertTrue(menuItemsPage.checkPriceBound(v -> (v >= 0d)));
        assertTrue(menuItemsPage.checkPriceBound(v -> (v <= 322d)));

        menuItemsPage.search("all", 42d, 440d);
        assertTrue(menuItemsPage.checkQuerySearchResults("all"));
        assertTrue(menuItemsPage.checkPriceBound(v -> (v >= 42d)));
        assertTrue(menuItemsPage.checkPriceBound(v -> (v <= 440d)));

//        driver.close();
    }

}
