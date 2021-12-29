package com.ktsnvt.ktsnvt.e2e.tests;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExampleTest {

    @Test
    void nekaTestMetoda(){
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.get("https://gigatron.rs/");

        WebElement el1 = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[placeholder='Unesite pojam za pretragu']")));
        el1.clear();
        el1.sendKeys("laptop");

        WebElement el2 = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.cssSelector(".search-icon")));
        el2.click();


        List<WebElement> els = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("figure.default")));

        els.get(0).click();

        WebElement el3 = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.buy")));
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("arguments[0].click()", el3);

        WebElement el4 = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.white")));
        el4.click();

        driver.navigate().back();
        els = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("figure.default")));
        els.get(1).click();

        el3 = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.buy")));
        jse.executeScript("arguments[0].click()", el3);

        el4 = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.white")));
        el4.click();

        driver.navigate().back();
        els = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("figure.default")));
        els.get(2).click();

        el3 = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.buy")));
        jse.executeScript("arguments[0].click()", el3);

        el4 = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.white")));
        el4.click();

        WebElement el5 = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.cssSelector("span.cart")));
        el5.click();

        WebElement el6 = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.cart-reg")));
        el6.click();

        List<WebElement> elss = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.cart-item-container")));
        driver.quit();
    }

}
