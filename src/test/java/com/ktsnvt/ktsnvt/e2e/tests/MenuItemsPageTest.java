package com.ktsnvt.ktsnvt.e2e.tests;

import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.InventoryItemsPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.LoginPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.MenuItemsPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.Navbar;
import com.ktsnvt.ktsnvt.e2e.tests.utilities.Utilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MenuItemsPageTest extends BaseE2ETest {

    @Test
    void menuItemsHappyFlow() {
        var driver = initDriver();
        var navbar = PageFactory.initElements(driver, Navbar.class);
        var loginPage = PageFactory.initElements(driver, LoginPage.class);
        var menuItemsPage = PageFactory.initElements(driver, MenuItemsPage.class);
        var inventoryItemsPage = PageFactory.initElements(driver, InventoryItemsPage.class);

        navbar.navigateLogin();
        loginPage.setEmail("email2@email.com");
        loginPage.setPassword("password");
        loginPage.clickLogin();

        assertTrue(Utilities.checkUrl(driver, "/employees"));

        navbar.navigateMenuItems();
        assertTrue(Utilities.checkUrl(driver, "/menu-items"));

        var numOfItemsAndPages = menuItemsPage.getPaginationInformation();

        menuItemsPage.search("ice", 10d, 100d, "FOOD");
        assertTrue(menuItemsPage.checkSearchQueryResultsOnAllPages(
                "ice", v -> (v >= 10d && v <= 100d), "FOOD"));

        menuItemsPage.search("ice", 1000000d, 0d, "");
        assertTrue(menuItemsPage.checkEmptyResultsPage());

        menuItemsPage.search("all", 100d, 10000d, "DRINK");
        assertTrue(menuItemsPage.checkSearchQueryResultsOnAllPages(
                "all", v -> (v >= 100d && v <= 10000d), "DRINK"));

        menuItemsPage.search("all", 100d, 10000d, "FOOD");
        assertTrue(menuItemsPage.checkSearchQueryResultsOnAllPages(
                "all", v -> (v >= 100d && v <= 10000d), "FOOD"));

        menuItemsPage.search("all", 0d, 322d, "DRINK");
        assertTrue(menuItemsPage.checkSearchQueryResultsOnAllPages(
                "all", v -> (v >= 0d && v <= 322d), "DRINK"));

        menuItemsPage.search("all", 42d, 440d, "FOOD");
        assertTrue(menuItemsPage.checkSearchQueryResultsOnAllPages(
                "all", v -> (v >= 42d && v <= 440d), "FOOD"));

        menuItemsPage.resetSearchForm();

        assertTrue(menuItemsPage.checkPaginationInformationMatching(numOfItemsAndPages));

        navbar.navigateInventoryItems();
        assertTrue(Utilities.checkUrl(driver, "/inventory-items"));

        var proposedCreateName = inventoryItemsPage
                .findUniqueInventoryItemName("Ice cream");
        inventoryItemsPage.clickCreateInventoryItem();
        var createdName = inventoryItemsPage.setUpdateNameField(proposedCreateName);
        inventoryItemsPage.setUpdateDescriptionFieldField("New description");
        inventoryItemsPage.setUpdateBasePriceField(42d);
        inventoryItemsPage.setUpdateAllergiesField("New allergy");
        inventoryItemsPage.clickSubmitInventoryItemUpdate();
        assertTrue(inventoryItemsPage.checkItemFields(createdName,
                "New description", 42d, "New allergy", "false"));

        inventoryItemsPage.clickAddToMenu(createdName);
        inventoryItemsPage.setNewMenuItemPrice(42d);
        inventoryItemsPage.clickAddMenuItemButton();
        assertTrue(inventoryItemsPage.checkItemFields(createdName,
                "New description", 42d, "New allergy", "true"));

        navbar.navigateMenuItems();
        assertTrue(Utilities.checkUrl(driver, "/menu-items"));

        menuItemsPage.clickUpdateMenuItem(createdName);
        var updatedPrice = menuItemsPage.setUpdatePriceField(496d);
        menuItemsPage.clickSaveChangesButton();
        assertTrue(menuItemsPage.checkMenuItemFields(createdName,
                "New description", updatedPrice, "New allergy"));

        var numOfItemsAndPagesBeforeDeletion = menuItemsPage.getPaginationInformation();
        menuItemsPage.clickDeleteMenuItemByName(createdName);
        menuItemsPage.clickConfirmDeletion();
        assertTrue(menuItemsPage.checkNumberOfItemsAfterDeactivation(numOfItemsAndPagesBeforeDeletion));

        navbar.navigateInventoryItems();
        assertTrue(Utilities.checkUrl(driver, "/inventory-items"));
        assertTrue(inventoryItemsPage.checkItemFields(createdName,
                "New description", 42d, "New allergy", "false"));

        var numOfItemsAndPagesOnInventoryItemsBeforeDeletion = inventoryItemsPage.getPaginationInformation();
        inventoryItemsPage.clickDeleteInventoryItemByName(createdName);
        inventoryItemsPage.clickConfirmDeletion();
        assertFalse(inventoryItemsPage.checkItemFields(createdName,
                "New description", 42d, "New allergy", "false"));
        assertTrue(inventoryItemsPage.checkNumberOfItemsAfterDeactivation(
                numOfItemsAndPagesOnInventoryItemsBeforeDeletion));

        driver.quit();
    }

}
