package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class WaiterPage {

    private WebDriver driver;

    @FindBy(css = "div[role='tab']")
    private List<WebElement> tabs;

    @FindBy(css = "canvas")
    private List<WebElement> canvases;

    public WaiterPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickTab(int tabIndex) {
        List<WebElement> elements = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfAllElements(tabs));
        elements.get(tabIndex).click();
    }

    public int getTabNumber() {
        List<WebElement> elements = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfAllElements(tabs));
        return elements.size();
    }

    public int getCanvasNumber() {
        List<WebElement> elements = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfAllElements(canvases));
        return elements.size();
    }

    public void clickOnTable(Integer tableX, Integer tableY) throws InterruptedException {
        List<WebElement> elements = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("canvas")));

        Thread.sleep(200); // wait for animation to finish

        Point location = elements.get(0).getLocation();
        int x = location.getX();
        int y = location.getY();

        new Actions(driver).moveByOffset(x, y).moveByOffset(tableX - 20, tableY - 20).click().build().perform();
    }
}
