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

        assertTrue(chefPage.checkNewTableRows(3));
        assertTrue(chefPage.checkPreparingTableRows(1));

        chefPage.clickFinishOldestNew();
        chefPage.setPin("1234");
        chefPage.clickConfirmPin();

        assertTrue(chefPage.checkNewTableRows(2));
        assertTrue(chefPage.checkPreparingTableRows(1));

        chefPage.clickPrepareOldestNew();
        chefPage.setPin("1234");
        chefPage.clickConfirmPin();

        assertTrue(chefPage.checkNewTableRows(1));
        assertTrue(chefPage.checkPreparingTableRows(2));

        chefPage.clickFinishOldestPreparing();
        chefPage.setPin("1234");
        chefPage.clickConfirmPin();

        assertTrue(chefPage.checkNewTableRows(1));
        assertTrue(chefPage.checkPreparingTableRows(1));
        driver.close();
    }
}
