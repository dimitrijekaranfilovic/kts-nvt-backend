package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    @FindBy(css = "input[formcontrolname='email']")
    private WebElement emailInput;

    @FindBy(css = "input[formcontrolname='password']")
    private WebElement passwordInput;

    @FindBy(css = "button[color='primary']")
    private WebElement loginBtn;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void setEmail(String keys) {
        WebElement element = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(emailInput));
        element.clear();
        element.sendKeys(keys);
    }

    public void setPassword(String keys) {
        WebElement element = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(passwordInput));
        element.clear();
        element.sendKeys(keys);
    }

    public void clickLogin() {
        WebElement element = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(loginBtn));
        element.click();
    }
}
