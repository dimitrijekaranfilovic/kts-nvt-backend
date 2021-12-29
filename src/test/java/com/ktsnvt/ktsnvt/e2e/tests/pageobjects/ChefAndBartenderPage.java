package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class ChefAndBartenderPage {

    private WebDriver driver;

    @FindBy(css = "div[name='left'] tr.mat-row")
    private List<WebElement> newRequests;

    @FindBy(css = "div[name='right'] tr.mat-row")
    private List<WebElement> requestsInPreparation;

    @FindBy(css = "div[name='left'] button.mat-raised-button[color='primary']")
    private List<WebElement> prepareButtons;

    @FindBy(css = "div[name='left'] button.mat-raised-button[color='accent']")
    private List<WebElement> finishNewButtons;

    @FindBy(css = "div[name='right'] button.mat-raised-button[color='accent']")
    private List<WebElement> finishPreparingButtons;

    @FindBy(css = "input[formcontrolname='pin']")
    private WebElement pinInput;

    @FindBy(id = "confirmPin")
    private WebElement confirmPinButton;

    public ChefAndBartenderPage(WebDriver driver) {
        this.driver = driver;
    }

    public int getNewRequestCount() {
        List<WebElement> elements = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfAllElements(newRequests));
        return elements.size();
    }

    public int getRequestsInPreparationCount() {
        List<WebElement> elements = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfAllElements(requestsInPreparation));
        return elements.size();
    }

    public void clickFinishOldestNew() {
        List<WebElement> elements = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfAllElements(finishNewButtons));
        elements.get(0).click();
    }

    public void clickFinishOldestPreparing() {
        List<WebElement> elements = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfAllElements(finishPreparingButtons));
        elements.get(1).click();
    }

    public void clickPrepareOldestNew() {
        List<WebElement> elements = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfAllElements(prepareButtons));
        elements.get(1).click();
    }

    public void setPin(String keys) {
        WebElement element = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(pinInput));
        element.clear();
        element.sendKeys(keys);
    }

    public void clickConfirmPin() {
        WebElement element = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(confirmPinButton));
        element.click();
    }
}