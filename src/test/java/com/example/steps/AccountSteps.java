package com.example.steps;

import com.example.context.ScenarioContext;
import com.example.pages.LoginPage;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class AccountSteps {

    @Then("I should see that I am logged in")
    public void iShouldSeeThatIAmLoggedIn() {
        LoginPage loginPage = ScenarioContext.get(ScenarioContext.LOGIN_PAGE);
        Assert.assertTrue(
                loginPage.isLoggedInAsUser(),
                "Użytkownik powinien być zalogowany (Logged in as username)"
        );
    }
}
