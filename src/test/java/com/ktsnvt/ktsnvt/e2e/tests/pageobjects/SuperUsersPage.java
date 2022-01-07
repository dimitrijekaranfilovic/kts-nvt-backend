package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SuperUsersPage extends BasePage {
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

    public SuperUsersPage(WebDriver driver) {
        super(driver);
    }

    public void search(String query, Double salaryLowerBound, Double salaryUpperBound) throws InterruptedException {
        sendKeys(queryInput, query);
        sendKeys(salaryLowerBoundInput, salaryLowerBound.toString());
        sendKeys(salaryUpperBoundInput, salaryUpperBound.toString());
        click(searchButton);
        Thread.sleep(200);
    }

    public boolean checkSuperUsersTableRows(int numberOfElements) {
        var elements = (new WebDriverWait(driver, 10)
                .until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("tbody tr"), numberOfElements)));
        return elements.size() == numberOfElements;
    }

    public void resetSearchForm() throws InterruptedException {
        click(resetButton);
        Thread.sleep(200);
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

    public String getLastSuperUserName() {
        var lastTableRow = getLastTableRow();
        var tds = lastTableRow.findElements(By.cssSelector("td"));
        return tds.get(0).getText();
    }

    public String getLastSuperUserSalary() {
        var lastTableRow = getLastTableRow();
        var tds = lastTableRow.findElements(By.cssSelector("td"));
        return tds.get(4).getText();
    }

    public void clickUpdateLastSuperUserSalary() {
        performLastSuperUserAction(0);
    }

    public void clickDeleteLastSuperUser() {
        performLastSuperUserAction(1);
    }

    private void performLastSuperUserAction(int index) {
        var lastTableRow = getLastTableRow();
        var actions = lastTableRow.findElements(By.cssSelector("button"));
        click(actions.get(index));
    }

    private WebElement getLastTableRow() {
        return  (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//table[1]/tbody/tr)[last()]")));
    }
}
