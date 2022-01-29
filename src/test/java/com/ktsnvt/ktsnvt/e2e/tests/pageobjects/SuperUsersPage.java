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

public class SuperUsersPage extends BaseCRUDPage {
    @FindBy(css = "input[formcontrolname='query']")
    private WebElement queryInput;

    @FindBy(css = "input[formcontrolname='salaryLowerBound']")
    private WebElement salaryLowerBoundInput;

    @FindBy(css = "input[formcontrolname='salaryUpperBound']")
    private WebElement salaryUpperBoundInput;

    @FindBy(css = "mat-select[formcontrolname='type']")
    private WebElement typeSelect;

    @FindBy(css = "button[id='search']")
    private WebElement searchButton;

    @FindBy(css = "button[id='reset']")
    private WebElement resetButton;

    @FindBy(css = "button[id='createSuperUser']")
    private WebElement createSuperUserButton;

    @FindBy(css = "input[formcontrolname='name']")
    private WebElement nameInput;

    @FindBy(css = "input[formcontrolname='surname']")
    private WebElement surnameInput;

    @FindBy(css = "input[formcontrolname='email']")
    private WebElement emailInput;

    @FindBy(css = "input[formcontrolname='password'")
    private WebElement passwordInput;

    @FindBy(css = "input[formcontrolname='confirmPassword']")
    private WebElement confirmPasswordInput;

    @FindBy(css = "input[formcontrolname='salary']")
    private WebElement salaryInput;

    @FindBy(css = "button[id='saveSuperUser']")
    private WebElement saveButton;

    @FindBy(css = "button[id='saveSalary']")
    private WebElement saveSalaryButton;

    @FindBy(css = "input[id='updateSalary']")
    private WebElement updateSalaryInput;

    @FindBy(css = "button[id='yes']")
    private WebElement yesButton;

    @FindBy(css = "[formcontrolname='type']")
    private WebElement superUserTypeSearchField;

    @FindBy(css = "[dataclass='searchOption']")
    private List<WebElement> superUserTypeSearchOptions;

    @FindBy(css = "tbody tr")
    private List<WebElement> superUserTableRows;

    @FindBy(css = "[element-group='superUserName']")
    private List<WebElement> superUserTableNames;

    @FindBy(css = "[element-group='superUserSurname']")
    private List<WebElement> superUserTableSurnames;

    @FindBy(css = "[element-group='superUserPin']")
    private List<WebElement> superUserTablePins;

    @FindBy(css = "[element-group='superUserType']")
    private List<WebElement> superUserTableType;

    @FindBy(css = "[element-group='superUserCurrentSalary']")
    private List<WebElement> superUserTableCurrentSalary;

    public SuperUsersPage(WebDriver driver) {
        super(driver);
    }

    public void search(String query, Double salaryLowerBound, Double salaryUpperBound, String category) {
        waitForSpinnerToFinish();
        sendKeys(queryInput, query);
        sendKeys(salaryLowerBoundInput, salaryLowerBound.toString());
        sendKeys(salaryUpperBoundInput, salaryUpperBound.toString());
        selectCategoryOption(category);
        click(searchButton);
    }

    public void resetSearchForm() {
        waitForSpinnerToFinish();
        click(resetButton);
    }

    @Override
    public boolean checkSearchResults(String query, Predicate<Double> comparator, String category) {
        waitForSpinnerToFinish();
        var names = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(superUserTableNames)));
        var surnames = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(superUserTableSurnames)));
        for (int i = 0; i < names.size(); ++i) {
            if (!names.get(i).getText().toLowerCase(Locale.ROOT).contains(query.trim().toLowerCase(Locale.ROOT))
                    && !surnames.get(i).getText().toLowerCase(Locale.ROOT).contains(query.trim().toLowerCase(Locale.ROOT))
            ) {
                return false;
            }
        }
        return checkPriceBound(comparator) && checkCategory(category);
    }

    public boolean checkPriceBound(Predicate<Double> comparator) {
        waitForSpinnerToFinish();
        waitForElementsToBeRefreshedAndVisible(driver, superUserTableRows);
        var prices = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(superUserTableCurrentSalary)));
        return prices.stream().parallel().map(WebElement::getText).map(Double::parseDouble).allMatch(comparator);
    }

    public boolean checkCategory(String categoryName) {
        waitForSpinnerToFinish();
        var categories = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(superUserTableType)));
        return categories.stream().parallel().map(WebElement::getText)
                .allMatch(p -> p.toLowerCase(Locale.ROOT).contains(categoryName.toLowerCase(Locale.ROOT)));
    }

    public void clickCreateSuperUser() {
        click(createSuperUserButton);
    }

    public void setName(String name) {
        sendKeys(nameInput, name);
    }

    public void setSurname(String surname) {
        sendKeys(surnameInput, surname);
    }

    public void setEmail(String email) {
        sendKeys(emailInput, email);
    }

    public void setPassword(String password) {
        sendKeys(passwordInput, password);
    }

    public void setConfirmPassword(String password) {
        sendKeys(confirmPasswordInput, password);
    }

    public void setSalary(Double salary) {
        sendKeys(salaryInput, salary.toString());
    }

    public void clickSaveButton() {
        click(saveButton);
    }

    public void clickSaveSalaryButton() {
        click(saveSalaryButton);
    }

    public void setUpdateSalary(Double salary) {
        sendKeys(updateSalaryInput, salary.toString());
    }

    public void clickYesButton() {
        click(yesButton);
    }

    public boolean checkLastSuperUserDetails(String name, String surname, String email, String type, Double salary) {
        waitForSpinnerToFinish();
        var nameMatches = getLastTableRowField(0).equals(name);
        var surnameMatches = getLastTableRowField(1).equals(surname);
        var emailMatches = getLastTableRowField(2).equals(email);
        var typeMatches = getLastTableRowField(3).equals(type);
        var salaryMatches = Double.parseDouble(getLastTableRowField(4)) == salary;
        return nameMatches && surnameMatches && emailMatches && typeMatches && salaryMatches;
    }

    public void clickUpdateLastSuperUserSalary() {
        waitForSpinnerToFinish();
        performLastTableRowAction(0);
    }

    public void clickDeleteLastSuperUser() {
        waitForSpinnerToFinish();
        performLastTableRowAction(1);
    }

    public void selectCategoryOption(String categoryName) {
        waitForSpinnerToFinish();
        click(superUserTypeSearchField);
        var searchOptions = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(superUserTypeSearchOptions)));
        var option = searchOptions.stream()
                .filter(p -> p.getAttribute("value").contains(categoryName)).findFirst();
        if (option.isEmpty()) {
            option = superUserTypeSearchOptions.stream()
                    .filter(p -> p.getAttribute("value").isBlank()).findFirst();
        }
        option.ifPresent(this::click);
    }

    public boolean checkCurrentTotalElements(String numOfItemsAndPagesBeforeDeletion, double correction) {
        waitForSpinnerToFinish();
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.not(
                        ExpectedConditions.textToBePresentInElement(
                                paginationItemAndPageNumbers, numOfItemsAndPagesBeforeDeletion)));
        var currentPageInfo = getPaginationInformation();
        var previousTotalItems = Double.parseDouble(numOfItemsAndPagesBeforeDeletion
                .substring(numOfItemsAndPagesBeforeDeletion.length() - 2));
        var currentTotalItems = Double.parseDouble(currentPageInfo
                .substring(currentPageInfo.length() - 2));
        return currentTotalItems == previousTotalItems + correction;
    }
}
