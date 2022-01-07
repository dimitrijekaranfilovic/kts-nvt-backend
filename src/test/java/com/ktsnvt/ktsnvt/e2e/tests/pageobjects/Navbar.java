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

    @FindBy(css = "button[id='profileButton']")
    private WebElement profileButton;

    @FindBy(css = "button[id='logout']")
    private WebElement logoutButton;

    public Navbar(WebDriver driver) {
        super(driver);
    }

    public void navigateChef() {
        click(linkChef);
    }

    public void navigateBartender() {
        click(linkBartender);
    }

    public void navigateWaiter() {
        click(linkWaiter);
    }

    public void navigateLogin() {
        click(linkLogin);
    }

    public void navigateSectionsAdmin() {
        click(linkSections);
    }

    public void clickLogout() throws InterruptedException {
        click(profileButton);
        Thread.sleep(200);
        click(logoutButton);
    }
}
