package com.example.steps;

import com.example.context.ScenarioContext;
import com.example.hooks.Hooks;
import com.example.pages.AccountCreatedPage;
import com.example.pages.HomePage;
import com.example.pages.LoginPage;
import com.example.pages.SignupPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class LoginSteps {

    @Given("I have a registered user ready for login")
    public void iHaveARegisteredUserReadyForLogin(DataTable dataTable) {
        var row = dataTable.asMaps(String.class, String.class).get(0);
        String name = row.get("name");
        String password = row.get("password");
        String title = row.get("title");
        String day = row.get("day");
        String month = row.get("month");
        String year = row.get("year");
        String firstName = row.get("firstName");
        String lastName = row.get("lastName");
        String address = row.get("address");
        String country = row.get("country");
        String state = row.get("state");
        String city = row.get("city");
        String zipcode = row.get("zipcode");
        String mobile = row.get("mobile");

        ScenarioContext.put(ScenarioContext.REGISTERED_PASSWORD, password);

        HomePage homePage = ScenarioContext.get(ScenarioContext.HOME_PAGE);
        homePage.clickSignupLogin();
        LoginPage loginPage = new LoginPage(Hooks.driver);
        ScenarioContext.put(ScenarioContext.LOGIN_PAGE, loginPage);

        String registeredEmail = CommonSteps.generateUniqueEmail(
                name.toLowerCase().replace(" ", "") + "@example.com"
        );
        ScenarioContext.put(ScenarioContext.REGISTERED_EMAIL, registeredEmail);
        loginPage.enterNewUserNameAndEmail(name, registeredEmail);
        loginPage.clickSignupButton();

        SignupPage signupPage = new SignupPage(Hooks.driver);
        ScenarioContext.put(ScenarioContext.SIGNUP_PAGE, signupPage);
        signupPage.fillAccountInformation(password, title, day, month, year);
        signupPage.subscribeToNewsletter();
        signupPage.agreeToSpecialOffers();
        signupPage.fillAddressDetails(firstName, lastName, address, country, state, city, zipcode, mobile);
        signupPage.clickCreateAccount();

        AccountCreatedPage accountCreatedPage = new AccountCreatedPage(Hooks.driver);
        ScenarioContext.put(ScenarioContext.ACCOUNT_CREATED_PAGE, accountCreatedPage);
        accountCreatedPage.clickContinue();

        loginPage = new LoginPage(Hooks.driver);
        ScenarioContext.put(ScenarioContext.LOGIN_PAGE, loginPage);
        loginPage.clickLogout();
    }

    @When("I enter email and password from the registered user")
    public void iEnterEmailAndPasswordFromRegisteredUser() {
        String email = ScenarioContext.get(ScenarioContext.REGISTERED_EMAIL);
        String password = ScenarioContext.get(ScenarioContext.REGISTERED_PASSWORD);
        LoginPage loginPage = ScenarioContext.get(ScenarioContext.LOGIN_PAGE);
        loginPage.enterLoginCredentials(email, password);
    }

    @When("I enter incorrect email address and password")
    public void iEnterIncorrectEmailAddressAndPassword() {
        LoginPage loginPage = ScenarioContext.get(ScenarioContext.LOGIN_PAGE);
        loginPage.enterLoginCredentials("wrong@example.com", "WrongPassword123!");
    }
}
