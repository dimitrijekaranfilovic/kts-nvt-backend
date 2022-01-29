package com.ktsnvt.ktsnvt.e2e.tests;

import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.InventoryItemsPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.LoginPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.Navbar;
import com.ktsnvt.ktsnvt.e2e.tests.utilities.Utilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InventoryItemsPageTest extends BaseE2ETest {

    @Test
    void inventoryItems_happyFlow() throws InterruptedException {
        var driver = initDriver();
        var navbar = PageFactory.initElements(driver, Navbar.class);
        var loginPage = PageFactory.initElements(driver, LoginPage.class);
        var inventoryItemsPage = PageFactory.initElements(driver, InventoryItemsPage.class);

        navbar.navigateLogin();
        loginPage.setEmail("email2@email.com");
        loginPage.setPassword("password");
        loginPage.clickLogin();

        assertTrue(Utilities.checkUrl(driver, "/employees"));

        navbar.navigateInventoryItems();
        assertTrue(Utilities.checkUrl(driver, "/inventory-items"));

        inventoryItemsPage.search("ice", 10d, 100d, "FOOD");
        assertTrue(inventoryItemsPage.checkSearchQueryResultsOnAllPages(
                "ice", v -> (v >= 10d && v <= 100d), "FOOD"));

        inventoryItemsPage.search("nothing", 10000d, 0d, "");
        assertTrue(inventoryItemsPage.checkEmptyResultsPage());

        inventoryItemsPage.search("all", 100d, 10000d, "FOOD");
        assertTrue(inventoryItemsPage.checkSearchQueryResultsOnAllPages(
                "all", v -> (v >= 100d && v <= 10000d), "FOOD"));

        inventoryItemsPage.search("all", 0d, 322d, "DRINK");
        assertTrue(inventoryItemsPage.checkSearchQueryResultsOnAllPages(
                "all", v -> (v >= 0d && v <= 322d), "DRINK"));

        inventoryItemsPage.search("all", 42d, 440d, "FOOD");
        assertTrue(inventoryItemsPage.checkSearchQueryResultsOnAllPages(
                "all", v -> (v >= 42d && v <= 440d), "FOOD"));

        inventoryItemsPage.resetSearchForm();

        inventoryItemsPage.clickCreateInventoryItem();
        inventoryItemsPage.setUpdateNameField("New item");
        inventoryItemsPage.setUpdateDescriptionFieldField("New description");
        inventoryItemsPage.setUpdateBasePriceField(42d);
        inventoryItemsPage.setUpdateAllergiesField("New allergy");
        inventoryItemsPage.clickSubmitInventoryItemUpdate();
        Thread.sleep(1000);
        assertTrue(inventoryItemsPage.checkInventoryItemsRows(5));
        assertEquals("New item", inventoryItemsPage.getLastInventoryItemName());
        assertEquals("New description", inventoryItemsPage.getLastInventoryItemDescription());
        assertEquals("42", inventoryItemsPage.getLastInventoryItemBasePrice());
        assertEquals("New allergy", inventoryItemsPage.getLastInventoryItemAllergy());
        assertEquals("false", inventoryItemsPage.getLastInventoryItemInMenu());


        inventoryItemsPage.clickUpdateLastInventoryItem();
        inventoryItemsPage.setUpdateNameField("Updated name");
        inventoryItemsPage.setUpdateDescriptionFieldField("Updated description");
        inventoryItemsPage.setUpdateBasePriceField(496d);
        inventoryItemsPage.setUpdateAllergiesField("Updated allergy");
        inventoryItemsPage.clickSubmitInventoryItemUpdate();
        Thread.sleep(1000);
        assertEquals("Updated name", inventoryItemsPage.getLastInventoryItemName());
        assertEquals("Updated description", inventoryItemsPage.getLastInventoryItemDescription());
        assertEquals("496", inventoryItemsPage.getLastInventoryItemBasePrice());
        assertEquals("Updated allergy", inventoryItemsPage.getLastInventoryItemAllergy());
        assertEquals("false", inventoryItemsPage.getLastInventoryItemInMenu());

        inventoryItemsPage.clickAddToMenu();
        inventoryItemsPage.setNewMenuItemPrice(42d);
        inventoryItemsPage.clickAddMenuItemButton();
        Thread.sleep(1000);
        assertEquals("true", inventoryItemsPage.getLastInventoryItemInMenu());

        inventoryItemsPage.clickDeleteLastInventoryItem();
        inventoryItemsPage.clickConfirmDeletion();
        assertTrue(inventoryItemsPage.checkInventoryItemsRows(4));

        driver.quit();
    }


}
