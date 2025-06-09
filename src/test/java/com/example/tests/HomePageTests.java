package com.example.tests;

import com.example.pages.HomePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class HomePageTests extends BaseTests {

    private static final Logger logger = LoggerFactory.getLogger(HomePageTests.class);

    @Test
    public void isB2C2Displayed() throws InterruptedException {
        // ***** Page Instantiations *****
        HomePage homePage = new HomePage(driver, wait);



        // ***** Test Steps *****
        logger.info("🌐 Opening B2C2.com website.");
        homePage.goToB2C2HomePage();

        logger.info("Accept Cookie button");
        homePage.acceptCookieButton();

        logger.info("🌐 Checking if Home Page Displayed");
        homePage.isHomePageDisplayed();





    }
}
