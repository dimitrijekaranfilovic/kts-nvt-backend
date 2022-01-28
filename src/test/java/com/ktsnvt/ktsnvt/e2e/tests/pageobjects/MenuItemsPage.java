package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

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

    @FindBy(css = "tbody tr")
    private List<WebElement> menuItemTableRows;

    @FindBy(css = "[name='menuItemName']")
    private List<WebElement> menuItemTableNames;

    @FindBy(css = "[name='menuItemDescription']")
    private List<WebElement> menuItemTableDescriptions;

    @FindBy(css = "[name='menuItemAllergies']")
    private List<WebElement> menuItemTableAllergies;

    @FindBy(css = "[name='menuItemPrice']")
    private List<WebElement> menuItemPrices;

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
        waitForElementToBeRefreshedAndVisible(driver, menuItemTableRows);
    }

    public void resetSearchForm() throws InterruptedException {
        click(resetButton);
        waitForElementToBeRefreshedAndVisible(driver, menuItemTableRows);
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


    public int countItems() {
        var elements = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOfAllElements(menuItemTableRows));
        return elements.size();
    }

    public boolean checkQuerySearchResults(String query) {
        var names = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOfAllElements(menuItemTableNames));
        var descriptions = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOfAllElements(menuItemTableDescriptions));
        var allergies = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOfAllElements(menuItemTableAllergies));
        for (int i = 0; i < menuItemTableNames.size(); ++i) {
            if (!names.get(i).getText().toLowerCase(Locale.ROOT).contains(query.trim().toLowerCase(Locale.ROOT))
                    && !descriptions.get(i).getText().toLowerCase(Locale.ROOT).contains(query.trim().toLowerCase(Locale.ROOT))
                    && !allergies.get(i).getText().toLowerCase(Locale.ROOT).contains(query.trim().toLowerCase(Locale.ROOT))
            ) {
                return false;
            }
        }
        return true;
    }

    public boolean checkPriceBound(Predicate<Double> comparator){
        var prices = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOfAllElements(menuItemPrices));
        return prices.stream().map(WebElement::getText).map(Double::parseDouble).allMatch(comparator);
    }

}
