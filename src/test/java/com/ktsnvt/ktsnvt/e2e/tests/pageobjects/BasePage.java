package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
    protected final WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected void sendKeys(WebElement input, String keys) {
        var element = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(input));
        element.clear();
        element.sendKeys(keys);
    }

    protected void click(WebElement clickable) {
        var element = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(clickable));
        element.click();
    }
}
