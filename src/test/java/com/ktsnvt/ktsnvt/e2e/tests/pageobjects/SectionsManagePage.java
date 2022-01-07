package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SectionsManagePage extends BaseCRUDPage {
    @FindBy(css = "button[id='createSection']")
    private WebElement createSectionButton;

    @FindBy(css = "button[id='save']")
    private WebElement saveButton;

    @FindBy(css = "input[formcontrolname='name']")
    private WebElement nameInput;

    @FindBy(css = "button[id='yes']")
    private WebElement yesButton;

    public SectionsManagePage(WebDriver driver) {
        super(driver);
    }

    public void clickCreateSection() {
        click(createSectionButton);
    }

    public void clickSaveButton() {
        click(saveButton);
    }

    public void setName(String name) {
        sendKeys(nameInput, name);
    }

    public void clickYesButton() {
        click(yesButton);
    }

    public boolean checkSectionTableRows(int numberOfElements) {
        var elements = (new WebDriverWait(driver, 10)
                .until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("tbody tr"), numberOfElements)));
        return elements.size() == numberOfElements;
    }

    public String getLastSectionName() {
        return getLastTableRowField(1);
    }

    public String getLastSectionId() {
        return getLastTableRowField(0);
    }

    public void clickUpdateLastSection() {
        performLastTableRowAction(0);
    }

    public void clickViewSeatingLayoutLastSection() {
        performLastTableRowAction(1);
    }

    public void clickDeleteLastSection() {
        performLastTableRowAction(2);
    }
}
