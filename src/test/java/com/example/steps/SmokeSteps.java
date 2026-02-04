package com.example.steps;

import com.example.hooks.Hooks;
import com.example.pages.HomePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class SmokeSteps {

    @Given("user opens web page")
    public void userOpensApplication() {
        // Driver and baseUrl are initialized in Hooks
        Assert.assertNotNull(
                Hooks.driver,
                "Web Page is opening"
        );
    }

    @Then("home page should be displayed")
    public void homePageShouldBeReady() {
        HomePage homePage = new HomePage(Hooks.driver);

        Assert.assertTrue(
                homePage.isDisplayed(),
                "Home page should be displayed"
        );
    }
}
