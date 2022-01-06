package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class LayoutPage extends BasePage {

    @FindBy(id = "addTable")
    private WebElement addNewButton;

    @FindBy(css = "button[type='submit']")
    private WebElement saveButton;

    @FindBy(css = "input[formcontrolname='number']")
    private WebElement tableNumberInput;

    @FindBy(css = "input[formcontrolname='x']")
    private WebElement tableXInput;

    @FindBy(css = "input[formcontrolname='y']")
    private WebElement tableYInput;

    private Integer lastX;
    private Integer lastY;

    public LayoutPage(WebDriver driver) {
        super(driver);
    }

    public void insertNewTable(String number, String x, String y) {
        WebElement addTableBtnElement = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(addNewButton));
        addTableBtnElement.click();
        WebElement numberInputElement = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(tableNumberInput));
        WebElement xInputElement = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(tableXInput));
        WebElement yInputElement = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(tableYInput));
        WebElement confirmBtnElement = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(saveButton));
        numberInputElement.clear();
        xInputElement.clear();
        yInputElement.clear();
        numberInputElement.sendKeys(number);
        xInputElement.sendKeys(x);
        yInputElement.sendKeys(y);
        confirmBtnElement.click();
    }

    public void clickOnTable(Integer tableX, Integer tableY) throws InterruptedException {
        List<WebElement> elements = new WebDriverWait(driver, 10).until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("canvas"))));

        Thread.sleep(200); // wait for animation to finish

        try {
            Point location = elements.get(0).getLocation();
            int x = location.getX();
            int y = location.getY();
            lastX = x;
            lastY = y;

            new Actions(driver).moveByOffset(x, y).moveByOffset(tableX, tableY).click().build().perform();
        } catch (StaleElementReferenceException e) {
            Point location = driver.findElements(By.cssSelector("canvas")).get(0).getLocation();
            int x = location.getX();
            int y = location.getY();
            new Actions(driver).moveByOffset(x, y).moveByOffset(10, 10).click().build().perform();
        }

    }

    public void deleteSelectedTable() {
        WebElement deleteTableBtnElement = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.id("deleteTable")));
        deleteTableBtnElement.click();
        WebElement confirmDeleteBtnElement = new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[mat-dialog-close='true']")));
        confirmDeleteBtnElement.click();
    }
}
