package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.function.Predicate;

public abstract class BaseCRUDPage extends BasePage {

    @FindBy(xpath = "//button[@ng-reflect-message='Next page']")
    protected WebElement nextPageButton;

    @FindBy(xpath = "//button[@ng-reflect-message='Last page']")
    protected WebElement lastPageButton;

    @FindBy(id = "suchEmpty")
    protected WebElement suchEmpty;

    public BaseCRUDPage(WebDriver driver) {
        super(driver);
    }

    protected void performLastTableRowAction(int index) {
        var lastTableRow = getLastTableRow();
        var actions = lastTableRow.findElements(By.cssSelector("button"));
        click(actions.get(index));
    }

    protected String getLastTableRowField(int index) {
        var lastTableRow = getLastTableRow();
        var tds = lastTableRow.findElements(By.cssSelector("td"));
        return tds.get(index).getText();
    }

    protected WebElement getLastTableRow() {
        return (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(
                        ExpectedConditions.visibilityOfElementLocated(By.xpath("(//table[1]/tbody/tr)[last()]"))));
    }

    public void clickNextPageButton() {
        click(nextPageButton);
    }

    // Override this method
    public boolean checkSearchResults(String query, Predicate<Double> comparator, String category) {
        return false;
    }

    public boolean checkSearchQueryResultsOnAllPages(String query, Predicate<Double> comparator, String category) {
        do {
            var satisfied = checkSearchResults(query, comparator, category);
            if (!satisfied) {
                return false;
            }
            if (nextPageButton.getAttribute("ng-reflect-disabled").equals("false")) {
                clickNextPageButton();
            }
        } while (nextPageButton.getAttribute("ng-reflect-disabled").equals("false"));
        return true;
    }

    public boolean checkEmptyResultsPage() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(suchEmpty)));
        return true;
    }

}
