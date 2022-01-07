package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ChangePasswordPage extends BasePage {
    @FindBy(css = "input[formcontrolname='oldPassword']")
    private WebElement oldPasswordInput;

    @FindBy(css = "input[formcontrolname='newPassword']")
    private WebElement newPasswordInput;

    @FindBy(css = "input[formcontrolname='confirmPassword']")
    private WebElement confirmPasswordInput;

    @FindBy(css = "button[id='changePassword']")
    private WebElement changePasswordButton;

    public ChangePasswordPage(WebDriver driver) {
        super(driver);
    }

    public void setOldPassword(String password) {
        sendKeys(oldPasswordInput, password);
    }

    public void setNewPassword(String password) {
        sendKeys(newPasswordInput, password);
    }

    public void setConfirmPassword(String password) {
        sendKeys(confirmPasswordInput, password);
    }

    public void clickChangePassword() {
        click(changePasswordButton);
    }
}
