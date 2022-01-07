package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UpdateProfilePage extends BasePage {
    @FindBy(css = "input[formcontrolname='name']")
    private WebElement firstNameInput;

    @FindBy(css = "input[formcontrolname='surname']")
    private WebElement lastNameInput;

    @FindBy(css = "button[id='updateProfile']")
    private WebElement updateProfileButton;

    public UpdateProfilePage(WebDriver driver) {
        super(driver);
    }

    public void setName(String name) {
        sendKeys(firstNameInput, name);
    }

    public void setSurname(String surname) {
        sendKeys(lastNameInput, surname);
    }

    public void clickUpdateProfile() {
        click(updateProfileButton);
    }
}
