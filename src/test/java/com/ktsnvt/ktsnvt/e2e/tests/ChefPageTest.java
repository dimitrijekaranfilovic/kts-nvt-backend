package com.ktsnvt.ktsnvt.e2e.tests;

import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.ChefAndBartenderPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.Navbar;
import com.ktsnvt.ktsnvt.e2e.tests.utilities.Utilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChefPageTest {

    @Test
    public void chefPage_happyFlow() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        Navbar navbar = PageFactory.initElements(driver, Navbar.class);
        ChefAndBartenderPage chefPage = PageFactory.initElements(driver, ChefAndBartenderPage.class);

        driver.manage().window().maximize();
        driver.get("http://localhost:4200/");

        navbar.navigateChef();
        assertTrue(Utilities.checkUrl(driver, "http://localhost:4200/chef"));

        assertEquals(3, chefPage.getNewRequestCount());
        assertEquals(1, chefPage.getRequestsInPreparationCount());

        chefPage.clickFinishOldestNew();
        chefPage.setPin("1234");
        chefPage.clickConfirmPin();

        Thread.sleep(1000);
        assertEquals(2, chefPage.getNewRequestCount());
        assertEquals(1, chefPage.getRequestsInPreparationCount());

        chefPage.clickPrepareOldestNew();
        chefPage.setPin("1234");
        chefPage.clickConfirmPin();

        Thread.sleep(1000);
        assertEquals(1, chefPage.getNewRequestCount());
        assertEquals(2, chefPage.getRequestsInPreparationCount());

        chefPage.clickFinishOldestPreparing();
        chefPage.setPin("1234");
        chefPage.clickConfirmPin();

        Thread.sleep(1000);
        assertEquals(1, chefPage.getNewRequestCount());
        assertEquals(1, chefPage.getRequestsInPreparationCount());
        driver.close();
    }
}
