package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class SectionsManagePage extends BaseCRUDPage {
    @FindBy(css = "button[id='createSection']")
    private WebElement createSectionButton;

    @FindBy(css = "button[id='save']")
    private WebElement saveButton;

    @FindBy(css = "input[formcontrolname='name']")
    private WebElement nameInput;

    @FindBy(css = "button[id='yes']")
    private WebElement yesButton;

    @FindBy(css = "tbody tr")
    private List<WebElement> sectionTableRows;

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

    public int getNumberOfElements() {
        waitForSpinnerToFinish();
        waitForElementsToBeRefreshedAndVisible(driver, sectionTableRows);
        return sectionTableRows.size();
    }

    public String getLastSectionId() {
        waitForSpinnerToFinish();
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

    public boolean checkLastSectionName(String name) {
        return getLastTableRowField(1).equals(name);
    }
}
