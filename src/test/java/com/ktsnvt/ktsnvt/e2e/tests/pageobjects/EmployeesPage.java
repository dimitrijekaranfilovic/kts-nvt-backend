package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

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
}
