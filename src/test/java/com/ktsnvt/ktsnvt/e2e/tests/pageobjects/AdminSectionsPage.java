package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AdminSectionsPage extends BasePage {

    @FindBy(css = "button[mattooltip='View seating layout']")
    public List<WebElement> layoutButtons;

    public AdminSectionsPage(WebDriver driver) {
        super(driver);
    }

    public void clickLayoutButton(Integer buttonIndex) {
        List<WebElement> elements = new WebDriverWait(driver, 0).until(ExpectedConditions.visibilityOfAllElements(layoutButtons));
        elements.get(buttonIndex).click();
    }
}
