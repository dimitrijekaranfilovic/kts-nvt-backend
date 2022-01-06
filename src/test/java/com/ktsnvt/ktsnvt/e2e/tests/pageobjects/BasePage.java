package com.ktsnvt.ktsnvt.e2e.tests.pageobjects;

import org.openqa.selenium.WebDriver;

public class BasePage {

    protected WebDriver driver;

    public BasePage() {}

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }
}
