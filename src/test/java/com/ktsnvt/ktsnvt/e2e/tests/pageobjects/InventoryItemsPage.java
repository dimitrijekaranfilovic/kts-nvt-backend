package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InventoryItemsPage extends BaseCRUDPage {

    @FindBy(css = "input[formcontrolname='query']")
    private WebElement queryInput;

    @FindBy(css = "input[formcontrolname='basePriceLowerBound']")
    private WebElement basePriceLowerBoundInput;

    @FindBy(css = "input[formcontrolname='basePriceUpperBound']")
    private WebElement basePriceUpperBoundInput;

    @FindBy(css = "button[id='search']")
    private WebElement searchButton;

    @FindBy(css = "button[id='reset']")
    private WebElement resetButton;

    @FindBy(css = "button[id='createInventoryItem']")
    private WebElement createInventoryItemButton;

    @FindBy(css = "input[id='updateNameField']")
    private WebElement updateNameField;

    @FindBy(css = "input[id='updateDescriptionField']")
    private WebElement updateDescriptionField;

    @FindBy(css = "input[id='updateBasePriceField']")
    private WebElement updateBasePriceField;

    @FindBy(css = "input[id='updateAllergiesField']")
    private WebElement updateAllergiesField;

    @FindBy(css = "button[id='submitInventoryItemUpdate']")
    private WebElement submitInventoryItemUpdate;

    @FindBy(css = "input[id='newMenuItemPrice']")
    private WebElement newMenuItemPriceField;

    @FindBy(css = "button[id='submitAddingMenuItem']")
    private WebElement submitAddingMenuItem;

    @FindBy(css = "button[id='yes']")
    private WebElement yesButton;


    public InventoryItemsPage(WebDriver driver) {
        super(driver);
    }

    public Boolean checkInventoryItemsRows(int numberOfElements) {
        var elements = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("tbody tr"), numberOfElements));
        return elements.size() == numberOfElements;
    }

    public void search(String query, Double priceLowerBound, Double priceUpperbound) throws InterruptedException {
        sendKeys(queryInput, query);
        sendKeys(basePriceLowerBoundInput, priceLowerBound.toString());
        sendKeys(basePriceUpperBoundInput, priceUpperbound.toString());
        click(searchButton);
        Thread.sleep(300);
    }

    public void resetSearchForm() throws InterruptedException {
        click(resetButton);
        Thread.sleep(200);
    }

    public void clickCreateInventoryItem() {
        click(createInventoryItemButton);
    }

    public void setUpdateNameField(String updateName) {
        sendKeys(updateNameField, updateName);
    }

    public void setUpdateDescriptionFieldField(String updateDescription) {
        sendKeys(updateDescriptionField,
                updateDescription);
    }

    public void setUpdateBasePriceField(Double updateBasePrice) {
        sendKeys(updateBasePriceField,
                updateBasePrice.toString());
    }

    public void setUpdateAllergiesField(String updateAllergies) {
        sendKeys(updateAllergiesField, updateAllergies);
    }

    public void clickSubmitInventoryItemUpdate() {
        click(submitInventoryItemUpdate);
    }

    public String getLastInventoryItemName() {
        return getLastTableRowField(0);
    }

    public String getLastInventoryItemDescription() {
        return getLastTableRowField(1);
    }

    public String getLastInventoryItemBasePrice() {
        return getLastTableRowField(4);

    }

    public String getLastInventoryItemAllergy() {
        return getLastTableRowField(2);
    }

    public String getLastInventoryItemCategory() {
        return getLastTableRowField(3);
    }

    public void clickUpdateLastInventoryItem() {
        performLastTableRowAction(0);
    }

    public void clickAddToMenu() {
        performLastTableRowAction(1);
    }

    public void setNewMenuItemPrice(Double newMenuItemPrice) {
        sendKeys(newMenuItemPriceField, newMenuItemPrice.toString());
    }

    public void clickAddMenuItemButton() {
        click(submitAddingMenuItem);
    }

    public String getLastInventoryItemInMenu() {
        return getLastTableRowField(5);
    }

    public void clickDeleteLastInventoryItem() {
        performLastTableRowAction(2);
    }

    public void clickConfirmDeletion() {
        click(yesButton);
    }
}
