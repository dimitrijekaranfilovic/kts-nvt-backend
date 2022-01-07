package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    @FindBy(css = "input[formcontrolname='email']")
    private WebElement emailInput;

    @FindBy(css = "input[formcontrolname='password']")
    private WebElement passwordInput;

    @FindBy(css = "button[id='signIn']")
    private WebElement loginBtn;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void setEmail(String email) {
        sendKeys(emailInput, email);
    }

    public void setPassword(String keys) {
        sendKeys(passwordInput, keys);
    }

    public void clickLogin() {
        click(loginBtn);
    }
}
