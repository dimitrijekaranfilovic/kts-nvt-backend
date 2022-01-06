package com.ktsnvt.ktsnvt.e2e.tests.utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Utilities {

    public static boolean checkUrl(WebDriver driver, String url) {
        return (new WebDriverWait(driver, 10).until(ExpectedConditions.urlToBe(url)));
    }

    public static boolean checkIfButtonIsEnabled(WebDriver driver, By selector) {
        return (new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(selector)).isEnabled());
    }
}
