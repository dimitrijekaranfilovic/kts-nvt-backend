package com.ktsnvt.ktsnvt.e2e.tests;

import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.OrderPage;
import com.ktsnvt.ktsnvt.e2e.tests.utilities.Utilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderPageTest extends BaseE2ETest{

    @Test
    void orderPage_happyFlow(){
        WebDriver driver = initDriver();
        OrderPage orderPage = PageFactory.initElements(driver, OrderPage.class);

        driver.get(Utilities.baseUrl + "/order/3");

        int initialGroups = orderPage.countGroups();
        int initialItems = orderPage.countItems();

        orderPage.clickAddMenuItemButton(0);
        orderPage.clickAddMenuItemToNewGroupButton();
        orderPage.enterNewGroupPin("4321");
        orderPage.enterNewGroupAmount("1");
        orderPage.enterNewGroupName("some group");
        orderPage.clickConfirmNewGroupButton();

        Assertions.assertTrue(orderPage.checkGroupsNumber(initialGroups + 1));
        Assertions.assertTrue(orderPage.checkOrderItemNumber(initialItems + 1));

        orderPage.clickDeleteOrderItemButton(0);
        orderPage.enterChargeCancelPin("4321");
        orderPage.clickConfirmChargeCancelPin();

        Assertions.assertTrue(orderPage.checkOrderItemNumber(initialItems));

        orderPage.clickDeleteGroupButton(1);
        orderPage.enterChargeCancelPin("4321");
        orderPage.clickConfirmChargeCancelPin();

        Assertions.assertTrue(orderPage.checkGroupsNumber(initialGroups));

        orderPage.clickChargeButton();
        orderPage.enterChargeCancelPin("4321");
        orderPage.clickConfirmChargeCancelPin();

        Assertions.assertTrue(Utilities.checkUrl(driver, "/waiter"));

        driver.close();
        driver.quit();

    }
}
