package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.function.Predicate;

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

    @FindBy(css = "[formcontrolname='category']")
    private WebElement inventoryItemCategorySearchField;

    @FindBy(css = "[dataclass='searchOption']")
    private List<WebElement> inventoryItemCategorySearchOptions;

    @FindBy(css = "[element-group='inventoryItemName']")
    private List<WebElement> inventoryItemTableNames;

    @FindBy(css = "[element-group='inventoryItemDescription']")
    private List<WebElement> inventoryItemTableDescriptions;

    @FindBy(css = "[element-group='inventoryItemAllergies']")
    private List<WebElement> inventoryItemTableAllergies;

    @FindBy(css = "[element-group='inventoryItemPrice']")
    private List<WebElement> inventoryItemPrices;

    @FindBy(css = "[element-group='inventoryItemCategory']")
    private List<WebElement> inventoryItemTableCategories;

    @FindBy(css = "tbody tr")
    private List<WebElement> inventoryItemTableRows;

    public InventoryItemsPage(WebDriver driver) {
        super(driver);
    }

    public void search(String query, Double priceLowerBound, Double priceUpperbound, String category) {
        sendKeys(queryInput, query);
        sendKeys(basePriceLowerBoundInput, priceLowerBound.toString());
        sendKeys(basePriceUpperBoundInput, priceUpperbound.toString());
        selectCategoryOption(category);
        click(searchButton);
    }

    public void resetSearchForm() {
        click(resetButton);
    }

    public void clickCreateInventoryItem() {
        click(createInventoryItemButton);
    }

    public String setUpdateNameField(String updateName) {
        sendKeys(updateNameField, updateName);
        return updateName;
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

    public void clickUpdateLastInventoryItem() {
        performLastTableRowAction(0);
    }

    public void clickAddToMenu(String itemName) {
        goToFirstPage();
        waitForSpinnerToFinish();
        do {
            var row = findItemWithNameOnCurrentPage(itemName);
            if (row != null) {
                performRowAction(row, 1);
                break;
            }
        } while (goToNextPageIfPossible());
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

    public void clickDeleteInventoryItemByName(String itemName) {
        goToFirstPage();
        waitForSpinnerToFinish();
        do {
            var row = findItemWithNameOnCurrentPage(itemName);
            if (row != null) {
                performRowAction(row, 2);
                break;
            }
        } while (goToNextPageIfPossible());
    }

    public void clickConfirmDeletion() {
        click(yesButton);
    }

    public void selectCategoryOption(String categoryName) {
        waitForSpinnerToFinish();
        click(inventoryItemCategorySearchField);
        var searchOptions = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(inventoryItemCategorySearchOptions)));
        var option = searchOptions.stream()
                .filter(p -> p.getAttribute("value").contains(categoryName)).findFirst();
        if (option.isEmpty()) {
            option = inventoryItemCategorySearchOptions.stream()
                    .filter(p -> p.getAttribute("value").isBlank()).findFirst();
        }
        option.ifPresent(this::click);
    }

    @Override
    public boolean checkSearchResults(String query, Predicate<Double> comparator, String categoryName) {
        waitForSpinnerToFinish();
        var names = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(inventoryItemTableNames)));
        var descriptions = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(inventoryItemTableDescriptions)));
        var allergies = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(inventoryItemTableAllergies)));
        for (int i = 0; i < inventoryItemTableNames.size(); ++i) {
            if (!names.get(i).getText().toLowerCase(Locale.ROOT).contains(query.trim().toLowerCase(Locale.ROOT))
                    && !descriptions.get(i).getText().toLowerCase(Locale.ROOT).contains(query.trim().toLowerCase(Locale.ROOT))
                    && !allergies.get(i).getText().toLowerCase(Locale.ROOT).contains(query.trim().toLowerCase(Locale.ROOT))
            ) {
                return false;
            }
        }
        return checkPriceBound(comparator) && checkCategory(categoryName);
    }

    public boolean checkCategory(String categoryName) {
        waitForSpinnerToFinish();
        var categories = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(inventoryItemTableCategories)));
        return categories.stream().parallel().map(WebElement::getText)
                .allMatch(p -> p.toLowerCase(Locale.ROOT).contains(categoryName.toLowerCase(Locale.ROOT)));
    }

    public boolean checkPriceBound(Predicate<Double> comparator) {
        waitForSpinnerToFinish();
        waitForElementsToBeRefreshedAndVisible(driver, inventoryItemTableRows);
        var prices = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(inventoryItemPrices)));
        return prices.stream().parallel().map(WebElement::getText).map(Double::parseDouble).allMatch(comparator);
    }

    public boolean checkItemFields(String itemName, String itemDescription, Double price, String itemAllergy, String inMenu) {
        goToFirstPage();
        waitForSpinnerToFinish();
        do {
            var row = findItemWithNameOnCurrentPage(itemName);
            if (row != null) {
                return checkItemFields(row, itemName, itemDescription, price, itemAllergy, inMenu);
            }
        } while (goToNextPageIfPossible());
        return false;
    }

    private WebElement findItemWithNameOnCurrentPage(String itemName) {
        waitForSpinnerToFinish();
        var rows = waitForElementsToBeRefreshedAndVisible(driver, inventoryItemTableRows);
        for (var row :
                rows) {
            var name = row.findElement(By.cssSelector("[element-group='inventoryItemName']"));
            var rowName = name.getText();
            if (itemName.equals(rowName)) {
                return row;
            }
        }
        return null;
    }

    private boolean checkItemFields(WebElement inventoryItem, String itemName, String itemDescription,
                                    Double price, String itemAllergy, String inMenu) {
        waitForSpinnerToFinish();
        if (inventoryItem == null) {
            return false;
        }
        var currentName = inventoryItem
                .findElement(By.cssSelector("[element-group='inventoryItemName']")).getText();
        var currentDescription = inventoryItem
                .findElement(By.cssSelector("[element-group='inventoryItemDescription']")).getText();
        var currentPrice = inventoryItem
                .findElement(By.cssSelector("[element-group='inventoryItemPrice']")).getText();
        var currentAllergy = inventoryItem
                .findElement(By.cssSelector("[element-group='inventoryItemAllergies']")).getText();
        var currentIsInMenu = inventoryItem
                .findElement(By.cssSelector("[element-group='inventoryItemIsInMenu']")).getText();
        return currentName.equals(itemName)
                && currentDescription.equals(itemDescription)
                && Double.parseDouble(currentPrice) == price
                && currentAllergy.equals(itemAllergy)
                && currentIsInMenu.equals(inMenu);

    }

    public void clickUpdateInventoryItem(String createdName) {
        goToFirstPage();
        waitForSpinnerToFinish();
        do {
            var row = findItemWithNameOnCurrentPage(createdName);
            if (row != null) {
                performRowAction(row, 0);
                break;
            }
        } while (goToNextPageIfPossible());
    }

    public String findUniqueInventoryItemName(String proposedName) {
        goToFirstPage();
        waitForSpinnerToFinish();
        var allExistingNames = new HashSet<String>();
        do {
            var allNamesOnCurrentPage = findAllNamesOnCurrentPage();
            allExistingNames.addAll(allNamesOnCurrentPage);
        } while (goToNextPageIfPossible());

        while (allExistingNames.contains(proposedName)) {
            proposedName = UUID.randomUUID().toString();
        }
        return proposedName;
    }

    private Set<String> findAllNamesOnCurrentPage() {
        waitForSpinnerToFinish();
        var retVal = new HashSet<String>();
        var rows = waitForElementsToBeRefreshedAndVisible(driver, inventoryItemTableRows);
        for (var row :
                rows) {
            var name = row.findElement(By.cssSelector("[element-group='inventoryItemName']"));
            var rowName = name.getText();
            retVal.add(rowName);
        }
        return retVal;
    }
}
