package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public class EmployeesPage extends BaseCRUDPage {

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

    @FindBy(css = "button[id='createEmployee']")
    private WebElement createEmployeeButton;

    @FindBy(css = "input[formcontrolname='name']")
    private WebElement nameInput;

    @FindBy(css = "input[formcontrolname='surname']")
    private WebElement surnameInput;

    @FindBy(css = "input[formcontrolname='pin']")
    private WebElement pinInput;

    @FindBy(css = "input[formcontrolname='salary']")
    private WebElement salaryInput;

    @FindBy(css = "button[id='saveEmployee']")
    private WebElement saveButton;

    @FindBy(css = "button[id='saveSalary']")
    private WebElement saveSalaryButton;

    @FindBy(css = "input[id='updateSalary']")
    private WebElement updateSalaryInput;

    @FindBy(css = "button[id='yes']")
    private WebElement yesButton;

    @FindBy(css = "[formcontrolname='type']")
    private WebElement employeeTypeSearchField;

    @FindBy(css = "[dataclass='searchOption']")
    private List<WebElement> employeeTypeSearchOptions;

    @FindBy(css = "tbody tr")
    private List<WebElement> employeeTableRows;

    @FindBy(css = "[element-group='employeeName']")
    private List<WebElement> employeeTableNames;

    @FindBy(css = "[element-group='employeeSurname']")
    private List<WebElement> employeeTableSurnames;

    @FindBy(css = "[element-group='employeePin']")
    private List<WebElement> employeeTablePins;

    @FindBy(css = "[element-group='employeeType']")
    private List<WebElement> employeeTableTypes;

    @FindBy(css = "[element-group='employeeCurrentSalary']")
    private List<WebElement> employeeTableCurrentSalary;

    public EmployeesPage(WebDriver driver) {
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

    public void selectCategoryOption(String categoryName) {
        waitForSpinnerToFinish();
        click(employeeTypeSearchField);
        var searchOptions = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(employeeTypeSearchOptions)));
        var option = searchOptions.stream()
                .filter(p -> p.getAttribute("value").contains(categoryName)).findFirst();
        if (option.isEmpty()) {
            option = employeeTypeSearchOptions.stream()
                    .filter(p -> p.getAttribute("value").isBlank()).findFirst();
        }
        option.ifPresent(this::click);
    }

    @Override
    public boolean checkSearchResults(String query, Predicate<Double> comparator, String category) {
        waitForSpinnerToFinish();
        var names = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(employeeTableNames)));
        var surnames = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(employeeTableSurnames)));
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
        waitForElementsToBeRefreshedAndVisible(driver, employeeTableRows);
        var prices = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(employeeTableCurrentSalary)));
        return prices.stream().parallel().map(WebElement::getText).map(Double::parseDouble).allMatch(comparator);
    }

    public boolean checkCategory(String categoryName) {
        waitForSpinnerToFinish();
        var categories = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(employeeTableTypes)));
        return categories.stream().parallel().map(WebElement::getText)
                .allMatch(p -> p.toLowerCase(Locale.ROOT).contains(categoryName.toLowerCase(Locale.ROOT)));
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

    public void clickCreateEmployee() {
        click(createEmployeeButton);
    }

    public void setName(String name) {
        sendKeys(nameInput, name);
    }

    public void setSurname(String surname) {
        sendKeys(surnameInput, surname);
    }

    public void setPin(String pin) {
        sendKeys(pinInput, pin);
    }

    public void setSalary(double salary) {
        sendKeys(salaryInput, String.valueOf(salary));
    }

    public void clickSaveButton() {
        click(saveButton);
    }

    public boolean checkLastEmployeeDetails(String name, String surname, String pin, double salary) {
        waitForSpinnerToFinish();
        var nameMatches = getLastTableRowField(0).equals(name);
        var surnameMatches = getLastTableRowField(1).equals(surname);
        var pinMatches = getLastTableRowField(2).equals(pin);
        var salaryMatches = Double.parseDouble(getLastTableRowField(4)) == salary;
        return nameMatches && surnameMatches && pinMatches && salaryMatches;
    }

    public void clickUpdateLastEmployee() {
        waitForSpinnerToFinish();
        performLastTableRowAction(0);
    }

    public void clickUpdateLastEmployeeSalary() {
        waitForSpinnerToFinish();
        performLastTableRowAction(1);
    }

    public void clickDeleteLastEmployee() {
        waitForSpinnerToFinish();
        performLastTableRowAction(2);
    }

    public void setUpdateSalary(double salary) {
        sendKeys(updateSalaryInput, String.valueOf(salary));
    }

    public void clickUpdateSalary() {
        click(saveSalaryButton);
    }

    public void clickConfirm() {
        click(yesButton);
    }
}
