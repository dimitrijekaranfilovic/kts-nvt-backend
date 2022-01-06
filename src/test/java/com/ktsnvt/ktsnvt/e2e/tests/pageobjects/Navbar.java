package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Navbar extends BasePage {

    @FindBy(css = "a[routerlink='/chef']")
    private WebElement linkChef;

    @FindBy(css = "a[routerlink='/bartender']")
    private WebElement linkBartender;

    @FindBy(css = "a[routerlink='/waiter']")
    private WebElement linkWaiter;

    @FindBy(css = "a[routerlink='/auth/login']")
    private WebElement linkLogin;

    @FindBy(css = "a[routerlink='/sections/manage']")
    private WebElement linkSections;

    public Navbar(WebDriver driver) {
        super(driver);
    }

    public void navigateChef() {
        (new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(linkChef))).click();
    }

    public void navigateBartender() {
        (new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(linkBartender))).click();
    }

    public void navigateWaiter() {
        (new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(linkWaiter))).click();
    }

    public void navigateLogin() {
        (new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(linkLogin))).click();
    }

    public void navigateSectionsAdmin() {
        (new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(linkSections))).click();
    }
}
