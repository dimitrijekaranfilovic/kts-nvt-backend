package com.ktsnvt.ktsnvt.e2e.tests;

import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.LoginPage;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.Navbar;
import com.ktsnvt.ktsnvt.e2e.tests.pageobjects.UpdateProfilePage;
import com.ktsnvt.ktsnvt.e2e.tests.utilities.Utilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UpdateProfilePageTest extends BaseE2ETest {

    @Test
    void updateProfile_happyFlow() {
        var driver = initDriver();
        var loginPage = PageFactory.initElements(driver, LoginPage.class);
        var navbar = PageFactory.initElements(driver, Navbar.class);
        var updateProfilePage = PageFactory.initElements(driver, UpdateProfilePage.class);

        navbar.navigateLogin();
        loginPage.setEmail("email1@email.com");
        loginPage.setPassword("password");
        loginPage.clickLogin();

        assertTrue(Utilities.checkUrl(driver, "/employees"));

        navbar.clickUpdateProfile();

        assertTrue(Utilities.checkUrl(driver, "/super-users/profile"));

        updateProfilePage.setName("pera");
        updateProfilePage.setSurname("peric");
        updateProfilePage.clickUpdateProfile();

        assertTrue(Utilities.checkUrl(driver, "/employees"));
        assertTrue(navbar.getProfileName().contains("pera peric"));

        driver.quit();
    }

}
