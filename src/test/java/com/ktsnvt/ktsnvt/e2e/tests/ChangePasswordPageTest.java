package com.ktsnvt.ktsnvt.e2e.tests;

import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.ChangePasswordPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.LoginPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.Navbar;
import com.ktsnvt.ktsnvt.e2e.tests.utilities.Utilities;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChangePasswordPageTest extends BaseE2ETest {

    @Test
    void changePassword_happyFlow() {
        var driver = initDriver();
        var loginPage = PageFactory.initElements(driver, LoginPage.class);
        var navbar = PageFactory.initElements(driver, Navbar.class);
        var changePasswordPage = PageFactory.initElements(driver, ChangePasswordPage.class);

        navbar.navigateLogin();
        loginPage.setEmail("email1@email.com");
        loginPage.setPassword("password");
        loginPage.clickLogin();

        assertTrue(Utilities.checkUrl(driver, "/employees"));

        navbar.clickChangePassword();

        assertTrue(Utilities.checkUrl(driver, "/super-users/password"));

        changePasswordPage.setOldPassword("password");
        changePasswordPage.setNewPassword("test123");
        changePasswordPage.setConfirmPassword("test123");
        changePasswordPage.clickChangePassword();

        assertTrue(Utilities.checkUrl(driver, "/employees"));

        navbar.clickLogout();

        loginPage.setEmail("email1@email.com");
        loginPage.setPassword("test123");
        loginPage.clickLogin();

        assertTrue(Utilities.checkUrl(driver, "/employees"));

        navbar.clickChangePassword();

        changePasswordPage.setOldPassword("test123");
        changePasswordPage.setNewPassword("password");
        changePasswordPage.setConfirmPassword("password");
        changePasswordPage.clickChangePassword();

        assertTrue(Utilities.checkUrl(driver, "/employees"));

        driver.quit();
    }

}
