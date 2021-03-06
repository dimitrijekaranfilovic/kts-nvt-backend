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

    @FindBy(xpath = "//button[@ng-reflect-message='First page']")
    protected WebElement firstPageButton;

    @FindBy(id = "suchEmpty")
    protected WebElement suchEmpty;

    @FindBy(css = "[class='mat-paginator-range-label']")
    protected WebElement paginationItemAndPageNumbers;

    @FindBy(id = "loading-spinner")
    protected WebElement loadingSpinner;

    public BaseCRUDPage(WebDriver driver) {
        super(driver);
    }

    public String getPaginationInformation() {
        waitForSpinnerToFinish();
        return new WebDriverWait(driver, 10)
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(paginationItemAndPageNumbers)))
                .getText();
    }

    public boolean checkPaginationInformationMatching(String previousPaginationInformation) {
        waitForSpinnerToFinish();
        return (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.textToBePresentInElement(paginationItemAndPageNumbers, previousPaginationInformation));

    }

    public void waitForSpinnerToFinish() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.invisibilityOf(loadingSpinner));
    }

    protected void performLastTableRowAction(int index) {
        var lastTableRow = getLastTableRow();
        var actions = lastTableRow.findElements(By.cssSelector("button"));
        click(actions.get(index));
    }

    protected void performRowAction(WebElement row, int index) {
        var actions = row.findElements(By.cssSelector("button"));
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
    
    public void clickFirstPageButton() {
        click(firstPageButton);
    }

    public void clickLastPageButton() {
        click(lastPageButton);
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
        } while (goToNextPageIfPossible());
        return true;
    }

    public void goToLastPage() {
        waitForSpinnerToFinish();
        if (lastPageButton.getAttribute("ng-reflect-disabled").equals("false")) {
            clickLastPageButton();
        }
    }

    public void goToFirstPage() {
        waitForSpinnerToFinish();
        if (firstPageButton.getAttribute("ng-reflect-disabled").equals("false")) {
            clickFirstPageButton();
        }
    }

    public boolean checkEmptyResultsPage() {
        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(suchEmpty)));
        return true;
    }

    public boolean goToNextPageIfPossible() {
        if (nextPageButton.getAttribute("ng-reflect-disabled").equals("false")) {
            clickNextPageButton();
            return true;
        }
        return false;
    }

    public boolean checkNumberOfItemsAfterDeactivation(String numOfItemsAndPagesBeforeDeletion) {
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
        return currentTotalItems == previousTotalItems - 1d;
    }

}
