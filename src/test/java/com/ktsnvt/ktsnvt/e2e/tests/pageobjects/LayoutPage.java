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

    @FindBy(id = "deleteTable")
    private WebElement deleteTableButton;

    @FindBy(id = "yes")
    private WebElement confirmButton;

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

    public void clickOnTable(Integer tableX, Integer tableY) {
        List<WebElement> elements = new WebDriverWait(driver, 10).until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("canvas"))));

        try {
            Point location = elements.get(0).getLocation();
            int x = location.getX();
            int y = location.getY();
            new Actions(driver).moveByOffset(x, y).moveByOffset(tableX, tableY).click().build().perform();
        } catch (StaleElementReferenceException e) {
            Point location = driver.findElements(By.cssSelector("canvas")).get(0).getLocation();
            int x = location.getX();
            int y = location.getY();
            new Actions(driver).moveByOffset(x, y).moveByOffset(tableX, tableY).click().build().perform();
        }

    }

    public void deleteSelectedTable() {
        click(deleteTableButton);
        click(confirmButton);
    }

    public void dragTable(int x, int y, int newX, int newY) {
        List<WebElement> elements = new WebDriverWait(driver, 10).until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("canvas"))));
        int canvasX, canvasY;
        try {
            Point location = elements.get(0).getLocation();
            canvasX = location.getX();
            canvasY = location.getY();
        } catch (StaleElementReferenceException e) {
            Point location = driver.findElements(By.cssSelector("canvas")).get(0).getLocation();
            canvasX = location.getX();
            canvasY = location.getY();
        }
        new Actions(driver)
                .moveByOffset(canvasX, canvasY)
                .moveByOffset(x, y)
                .clickAndHold()
                .moveByOffset(newX - x, newY - y)
                .release()
                .click()
                .build()
                .perform();
    }

    public boolean canDeleteTable() {
        return deleteTableButton.isEnabled();
    }
}
