package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class OrderPage extends BasePage {

    @FindBy(id = "charge-order-btn")
    private WebElement chargeOrderButton;

    @FindBy(css = ".delete-group-btn")
    private List<WebElement> deleteGroupButtons;

    @FindBy(css = ".add-menu-item-btn")
    private List<WebElement> addMenuItemButtons;

    @FindBy(css = ".delete-item-btn")
    private List<WebElement> deleteOrderItemButtons;

    @FindBy(css = "div.mat-menu-content > button.add-to-new-order-group-btn")
    private WebElement addMenuItemToNewGroupButton;

    @FindBy(css = "input[formcontrolname='pin']")
    private WebElement chargeCancelPinInput;

    @FindBy(id = "confirmPin")
    private WebElement confirmChargeCancelPinButton;

    @FindBy(id = "new-group-pin")
    private WebElement newGroupPinInput;

    @FindBy(id = "new-group-amount")
    private WebElement newGroupAmountInput;

    @FindBy(id = "new-group-name")
    private WebElement newGroupNameInput;

    @FindBy(id = "confirm-new-group-btn")
    private WebElement confirmNewGroupButton;

    public OrderPage(WebDriver driver) {
        super(driver);
    }


    public void clickChargeButton() {
        click(chargeOrderButton);
    }


    public void clickDeleteGroupButton(int groupIndex) {
        var wait = new WebDriverWait(driver, 10);
        var elements = wait.until(ExpectedConditions.visibilityOfAllElements(deleteGroupButtons));
        var btn = wait.until(ExpectedConditions.elementToBeClickable(elements.get(groupIndex)));
        btn.click();
        ;
    }

    public void clickAddMenuItemButton(int menuItemIndex) {
        var wait = new WebDriverWait(driver, 10);
        var elements = wait.until(ExpectedConditions.visibilityOfAllElements(addMenuItemButtons));
        var btn = wait.until(ExpectedConditions.elementToBeClickable(elements.get(menuItemIndex)));
        btn.click();
    }

    public void clickDeleteOrderItemButton(int orderItemIndex) {
        var wait = new WebDriverWait(driver, 10);
        var elements = wait.until(ExpectedConditions.visibilityOfAllElements(deleteOrderItemButtons));
        var btn = wait.until(ExpectedConditions.elementToBeClickable(elements.get(orderItemIndex)));
        btn.click();
    }

    public void clickAddMenuItemToNewGroupButton() {
        click(addMenuItemToNewGroupButton);
    }

    public void enterChargeCancelPin(String pin) {
        sendKeys(chargeCancelPinInput, pin);
    }

    public void clickConfirmChargeCancelPin() {
        click(confirmChargeCancelPinButton);
    }

    public void enterNewGroupPin(String pin){
        sendKeys(newGroupPinInput, pin);
    }

    public void enterNewGroupAmount(String amount){
        sendKeys(newGroupAmountInput, amount);
    }

    public void enterNewGroupName(String groupName){
        sendKeys(newGroupNameInput, groupName);
    }

    public void clickConfirmNewGroupButton(){
        click(confirmNewGroupButton);
    }

    public boolean checkGroupsNumber(int groupNum){
        var wait = new WebDriverWait(driver, 10);
        var groups = wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".order-item-group"), groupNum));
        return groups.size() == groupNum;
    }

    public boolean checkOrderItemNumber(int itemNum){
        var wait = new WebDriverWait(driver, 10);
        var items = wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".order-group-item"), itemNum));
        return items.size() == itemNum;
    }

}
