package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    public EmployeesPage(WebDriver driver) {
        super(driver);
    }

    public void search(String query, Double salaryLowerBound, Double salaryUpperBound) throws InterruptedException {
        sendKeys(queryInput, query);
        sendKeys(salaryLowerBoundInput, salaryLowerBound.toString());
        sendKeys(salaryUpperBoundInput, salaryUpperBound.toString());
        click(searchButton);
        Thread.sleep(200);
    }

    public boolean checkEmployeeTableRows(int numberOfElements) {
        var elements = (new WebDriverWait(driver, 10)
                .until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("tbody tr"), numberOfElements)));
        return elements.size() == numberOfElements;
    }

    public void resetSearchForm() throws InterruptedException {
        click(resetButton);
        Thread.sleep(200);
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

    public String getLastEmployeeName() {
        return getLastTableRowField(0);
    }

    public String getLastEmployeeSalary() {
        return getLastTableRowField(4);
    }

    public void clickUpdateLastEmployee() {
        performLastTableRowAction(0);
    }

    public void clickUpdateLastEmployeeSalary() {
        performLastTableRowAction(1);
    }

    public void clickDeleteLastEmployee() {
        performLastTableRowAction(2);
    }
}
