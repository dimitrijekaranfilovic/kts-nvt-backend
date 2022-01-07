package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BaseCRUDPage extends BasePage {

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
        return  (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//table[1]/tbody/tr)[last()]")));
    }
}
