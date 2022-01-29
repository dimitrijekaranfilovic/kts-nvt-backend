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

    @FindBy(css = "input[id='menuItemUpdatePriceField']")
    private WebElement updateMenuItemPriceField;

    @FindBy(css = "button[id='menuItemUpdateSubmitBtn']")
    private WebElement menuItemUpdateSubmitButton;

    @FindBy(css = "button[id='yes']")
    private WebElement yesButton;

    @FindBy(css = "tbody tr")
    private List<WebElement> menuItemTableRows;

    @FindBy(css = "[element-group='menuItemName']")
    private List<WebElement> menuItemTableNames;

    @FindBy(css = "[element-group='menuItemDescription']")
    private List<WebElement> menuItemTableDescriptions;

    @FindBy(css = "[element-group='menuItemAllergies']")
    private List<WebElement> menuItemTableAllergies;

    @FindBy(css = "[element-group='menuItemPrice']")
    private List<WebElement> menuItemPrices;

    @FindBy(css = "[formcontrolname='category']")
    private WebElement menuItemCategorySearchField;

    @FindBy(css = "[dataclass='searchOption']")
    private List<WebElement> menuItemCategorySearchOptions;

    @FindBy(css = "[element-group='menuItemCategory']")
    private List<WebElement> menuItemTableCategories;

    public MenuItemsPage(WebDriver driver) {
        super(driver);
    }

    public void search(String query, Double priceLowerBound, Double priceUpperbound, String category) {
        waitForSpinnerToFinish();
        sendKeys(queryInput, query);
        sendKeys(priceLowerBoundInput, priceLowerBound.toString());
        sendKeys(priceUpperBoundInput, priceUpperbound.toString());
        selectCategoryOption(category);
        click(searchButton);
    }

    public void resetSearchForm() {
        waitForSpinnerToFinish();
        click(resetButton);
    }

    public void clickSaveChangesButton() {
        click(menuItemUpdateSubmitButton);
    }

    public boolean checkLastMenuItemPriceUpdated(Double enteredPrice) {
        waitForSpinnerToFinish();
        var priceField = getLastTableRowField(4);
        return Double.parseDouble(priceField) == enteredPrice;
    }

    public double setUpdatePriceField(Double updatePrice) {
        var priceField = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOf(updateMenuItemPriceField));
        sendKeys(priceField,
                updatePrice.toString());
        return updatePrice;
    }

    public void clickUpdateLastMenuItemPriceButton() {
        waitForSpinnerToFinish();
        performLastTableRowAction(0);
    }

    public void clickDeactivateLastMenuItem() {
        waitForSpinnerToFinish();
        performLastTableRowAction(1);
    }

    public void clickConfirmDeletion() {
        click(yesButton);
    }

    @Override
    public boolean checkSearchResults(String query, Predicate<Double> comparator, String categoryName) {
        waitForSpinnerToFinish();
        var names = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(menuItemTableNames)));
        var descriptions = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(menuItemTableDescriptions)));
        var allergies = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(menuItemTableAllergies)));
        for (int i = 0; i < menuItemTableNames.size(); ++i) {
            if (!names.get(i).getText().toLowerCase(Locale.ROOT).contains(query.trim().toLowerCase(Locale.ROOT))
                    && !descriptions.get(i).getText().toLowerCase(Locale.ROOT).contains(query.trim().toLowerCase(Locale.ROOT))
                    && !allergies.get(i).getText().toLowerCase(Locale.ROOT).contains(query.trim().toLowerCase(Locale.ROOT))
            ) {
                return false;
            }
        }
        return checkPriceBound(comparator) && checkCategory(categoryName);
    }

    public boolean checkPriceBound(Predicate<Double> comparator) {
        waitForSpinnerToFinish();
        waitForElementsToBeRefreshedAndVisible(driver, menuItemTableRows);
        var prices = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(menuItemPrices)));
        return prices.stream().parallel().map(WebElement::getText).map(Double::parseDouble).allMatch(comparator);
    }

    public void selectCategoryOption(String categoryName) {
        waitForSpinnerToFinish();
        click(menuItemCategorySearchField);
        var searchOptions = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(menuItemCategorySearchOptions)));
        var option = searchOptions.stream()
                .filter(p -> p.getAttribute("value").contains(categoryName)).findFirst();
        if (option.isEmpty()) {
            option = menuItemCategorySearchOptions.stream()
                    .filter(p -> p.getAttribute("value").isBlank()).findFirst();
        }
        option.ifPresent(this::click);
    }

    public boolean checkCategory(String categoryName) {
        waitForSpinnerToFinish();
        var categories = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(menuItemTableCategories)));
        return categories.stream().parallel().map(WebElement::getText)
                .allMatch(p -> p.toLowerCase(Locale.ROOT).contains(categoryName.toLowerCase(Locale.ROOT)));
    }

    public void clickUpdateMenuItem(String createdName) {
        waitForSpinnerToFinish();
        do {
            var row = findItemWithNameOnCurrentPage(createdName);
            if (row != null) {
                performRowAction(row, 0);
                break;
            }
        } while (goToNextPageIfPossible());
    }

    private WebElement findItemWithNameOnCurrentPage(String itemName) {
        waitForSpinnerToFinish();
        var rows = waitForElementsToBeRefreshedAndVisible(driver, menuItemTableRows);
        for (var row :
                rows) {
            var name = row.findElement(By.cssSelector("[element-group='menuItemName']"));
            var rowName = name.getText();
            if (itemName.equals(rowName)) {
                return row;
            }
        }
        return null;
    }

    public void clickDeleteMenuItemByName(String itemName) {
        waitForSpinnerToFinish();
        do {
            var row = findItemWithNameOnCurrentPage(itemName);
            if (row != null) {
                performRowAction(row, 1);
                break;
            }
        } while (goToNextPageIfPossible());
    }


    public boolean checkMenuItemFields(String itemName, String itemDescription, Double price, String itemAllergy) {
        waitForSpinnerToFinish();
        do {
            var row = findItemWithNameOnCurrentPage(itemName);
            if (row != null) {
                return checkMenuItemFields(row, itemName, itemDescription, price, itemAllergy);
            }
        } while (goToNextPageIfPossible());
        return false;
    }

    private boolean checkMenuItemFields(WebElement inventoryItem, String itemName, String itemDescription,
                                        Double price, String itemAllergy) {
        waitForSpinnerToFinish();
        if (inventoryItem == null) {
            return false;
        }
        var currentName = inventoryItem
                .findElement(By.cssSelector("[element-group='menuItemName']")).getText();
        var currentDescription = inventoryItem
                .findElement(By.cssSelector("[element-group='menuItemDescription']")).getText();
        var currentPrice = inventoryItem
                .findElement(By.cssSelector("[element-group='menuItemPrice']")).getText();
        var currentAllergy = inventoryItem
                .findElement(By.cssSelector("[element-group='menuItemAllergies']")).getText();
        return currentName.equals(itemName)
                && currentDescription.equals(itemDescription)
                && Double.parseDouble(currentPrice) == price
                && currentAllergy.equals(itemAllergy);

    }
}
