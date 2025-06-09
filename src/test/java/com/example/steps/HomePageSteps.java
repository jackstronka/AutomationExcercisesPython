package com.example.steps;

import com.example.hooks.Hooks;
import com.example.pages.HomePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.testng.Assert.assertEquals;

public class HomePageSteps {

    private final HomePage homePage;
    private static final Logger logger = LoggerFactory.getLogger(HomePageSteps.class);

    public HomePageSteps() {
        this.homePage = new HomePage(Hooks.getDriver(), Hooks.getWait());
    }

    @Given("the user opens the homepage")
    public void openHomePage() {
        logger.info("🌐 Opening the B2C2 homepage");
        homePage.goToB2C2HomePage();
        homePage.acceptCookieButton();
    }

    @Then("the page title should be {string}")
    public void verifyPageTitle(String expectedTitle) {
        logger.info("🔍 Verifying page title");
        String actualTitle = Hooks.getDriver().getTitle();
        assertEquals(actualTitle, expectedTitle, "❌ Page title does not match expected.");
        logger.info("✅ Page title verified: {}", actualTitle);
    }
}
