package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MenuItemsPage extends BaseCRUDPage {

    @FindBy(css = "input[formcontrolname='query']")
    private WebElement queryInput;

    @FindBy(css = "input[formcontrolname='priceLowerBound']")
    private WebElement priceLowerBoundInput;

    @FindBy(css = "input[formcontrolname='priceUpperBound']")
    private WebElement priceUpperBoundInput;

    @FindBy(css = "button[id='menuItemSubmitSearchBtn']")
    private WebElement searchButton;

    @FindBy(css = "button[id='menuItemResetSearchBtn']")
    private WebElement resetButton;

    @FindBy(css = "button[id='updateMenuItemPriceBtn']")
    private WebElement updateMenuPriceButton;

    @FindBy(css = "button[id='deactivateMenuItemBtn']")
    private WebElement deactivateMenuItemButton;

    @FindBy(css = "input[id='menuItemUpdatePriceField']")
    private WebElement updateMenuItemPriceField;

    @FindBy(css = "button[id='menuItemUpdateSubmitBtn']")
    private WebElement menuItemUpdateSubmitButton;

    @FindBy(css = "button[id='yes']")
    private WebElement yesButton;

    public MenuItemsPage(WebDriver driver) {
        super(driver);
    }


    public Boolean checkMenuItemsRows(int numberOfElements) {
        var elements = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("tbody tr"), numberOfElements));
        return elements.size() == numberOfElements;
    }

    public void search(String query, Double priceLowerBound, Double priceUpperbound) throws InterruptedException {
        sendKeys(queryInput, query);
        sendKeys(priceLowerBoundInput, priceLowerBound.toString());
        sendKeys(priceUpperBoundInput, priceUpperbound.toString());
        click(searchButton);
    }

    public void resetSearchForm() throws InterruptedException {
        click(resetButton);
    }

    public void setUpdatePriceField(Double updatePrice) {
        sendKeys(updateMenuItemPriceField,
                updatePrice.toString());
    }

    public void clickUpdateMenuItemPriceButton() {
        click(updateMenuPriceButton);
    }

    public void clickDeactivateLastMenuItem() {
        performLastTableRowAction(1);
    }

    public void clickConfirmDeletion() {
        click(yesButton);
    }


}
