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

    @FindBy(css = "button[element-group='updateMenuItemPriceBtn']")
    private List<WebElement> updateMenuPriceButton;

    @FindBy(css = "button[element-group='deactivateMenuItemBtn']")
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

    @FindBy(css = "[class='mat-paginator-range-label']")
    private WebElement paginationItemAndPageNumbers;

    @FindBy(css = "[formcontrolname='category']")
    private WebElement menuItemCategorySearchField;

    @FindBy(css = "[dataclass='searchOption']")
    private List<WebElement> menuItemCategorySearchOptions;

    @FindBy(css = "[name='menuItemCategory']")
    private List<WebElement> menuItemTableCategories;

    @FindBy(id = "loading-spinner")
    private WebElement loadingSpinner;


    public MenuItemsPage(WebDriver driver) {
        super(driver);
    }


    public Boolean checkMenuItemsRows(int numberOfElements) {
        var elements = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("tbody tr"), numberOfElements));
        return elements.size() == numberOfElements;
    }

    public void search(String query, Double priceLowerBound, Double priceUpperbound, String category) throws InterruptedException {
        waitForSpinnerToFinish();
        sendKeys(queryInput, query);
        sendKeys(priceLowerBoundInput, priceLowerBound.toString());
        sendKeys(priceUpperBoundInput, priceUpperbound.toString());
        selectCategoryOption(category);
        click(searchButton);
    }

    public void resetSearchForm() throws InterruptedException {
        waitForSpinnerToFinish();
        click(resetButton);
    }

    public void clickSaveChangesButton() {
        waitForSpinnerToFinish();
        click(menuItemUpdateSubmitButton);
    }

    public boolean checkLastMenuItemPriceUpdated(Double enteredPrice) {
        waitForSpinnerToFinish();
        var priceField = getLastTableRowField(4);
        return Double.parseDouble(priceField) == enteredPrice;
    }


    public void setUpdatePriceField(Double updatePrice) {
        waitForSpinnerToFinish();
        sendKeys(updateMenuItemPriceField,
                updatePrice.toString());
    }

    public void clickUpdateLastMenuItemPriceButton() {
        waitForSpinnerToFinish();
        performLastTableRowAction(0);
    }

    public void clickDeactivateLastMenuItem() {
        performLastTableRowAction(1);
    }

    public void clickConfirmDeletion() {
        click(yesButton);
    }

    public void waitForSpinnerToFinish() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.invisibilityOf(loadingSpinner));
    }

    public int countItems() {
        waitForSpinnerToFinish();
        var elements = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOfAllElements(menuItemTableRows));
        return elements.size();
    }

    public String getPaginationInformation() {
        waitForSpinnerToFinish();
        return new WebDriverWait(driver, 10)
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(paginationItemAndPageNumbers)))
                .getText();
    }

    public boolean checkPaginationInformationMatching(String previousPaginationInformation) {
        System.out.println(previousPaginationInformation);
        waitForSpinnerToFinish();
        return (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.textToBePresentInElement(paginationItemAndPageNumbers, previousPaginationInformation));

    }

    @Override
    public boolean checkSearchResults(String query, Predicate<Double> comparator, String categoryName) {
//        waitForElementToBeRefreshedAndVisible(driver, menuItemTableRows);
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
        waitForElementToBeRefreshedAndVisible(driver, menuItemTableRows);
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


}
