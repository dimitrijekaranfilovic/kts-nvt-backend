package com.ktsnvt.ktsnvt.e2e.tests;

import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.LoginPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.Navbar;
import com.ktsnvt.ktsnvt.e2e.tests.utilities.Utilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginPageTest extends BaseE2ETest {

    @Test
    void loginPage_happyFlow() {
        var driver = initDriver();
        var loginPage = PageFactory.initElements(driver, LoginPage.class);
        var navbar = PageFactory.initElements(driver, Navbar.class);

        navbar.navigateLogin();
        loginPage.setEmail("email1@email.com");
        loginPage.setPassword("password");
        loginPage.clickLogin();

        assertTrue(Utilities.checkUrl(driver, "/employees"));

        navbar.clickLogout();

        assertTrue(Utilities.checkUrl(driver, "/auth/login"));
    }

}
