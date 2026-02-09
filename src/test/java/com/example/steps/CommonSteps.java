package com.example.steps;

import com.example.context.ScenarioContext;
import com.example.hooks.Hooks;
import com.example.pages.AccountCreatedPage;
import com.example.pages.ContactUsPage;
import com.example.pages.HomePage;
import com.example.pages.LoginPage;
import com.example.pages.ProductsPage;
import com.example.pages.SignupPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class CommonSteps {

    @Given("I open the browser")
    public void iOpenTheBrowser() {
        Assert.assertNotNull(Hooks.driver, "Przeglądarka powinna być uruchomiona");
        ScenarioContext.put(ScenarioContext.HOME_PAGE, new HomePage(Hooks.driver));
    }

    @And("I navigate to the home page")
    public void iNavigateToTheHomePage() {
        // NOOP: strona główna jest otwierana w Hooks.setUp()
    }

    @Then("I should see the home page")
    public void iShouldSeeTheHomePage() {
        HomePage homePage = ScenarioContext.get(ScenarioContext.HOME_PAGE);
        Assert.assertTrue(homePage.isDisplayed(), "Home page powinna być widoczna");
    }

    @When("I click on {string} button")
    public void iClickOnButton(String buttonText) {
        if ("Signup / Login".equals(buttonText)) {
            HomePage homePage = ScenarioContext.get(ScenarioContext.HOME_PAGE);
            homePage.clickSignupLogin();
            ScenarioContext.put(ScenarioContext.LOGIN_PAGE, new LoginPage(Hooks.driver));
        } else if ("Contact Us".equals(buttonText)) {
            HomePage homePage = ScenarioContext.get(ScenarioContext.HOME_PAGE);
            homePage.clickContactUs();
            ScenarioContext.put(ScenarioContext.CONTACT_US_PAGE, new ContactUsPage(Hooks.driver));
        }
    }

    @Then("I should see {string} section")
    public void iShouldSeeSection(String sectionText) {
        LoginPage loginPage = ScenarioContext.get(ScenarioContext.LOGIN_PAGE);
        SignupPage signupPage = ScenarioContext.get(ScenarioContext.SIGNUP_PAGE);
        ContactUsPage contactUsPage = ScenarioContext.get(ScenarioContext.CONTACT_US_PAGE);

        if ("GET IN TOUCH".equals(sectionText) && contactUsPage != null) {
            Assert.assertTrue(
                    contactUsPage.isGetInTouchVisible(),
                    "\"GET IN TOUCH\" section powinna być widoczna"
            );
        } else if ("New User Signup!".equals(sectionText)) {
            Assert.assertTrue(
                    loginPage.isNewUserSignupSectionVisible(),
                    "\"New User Signup!\" section powinna być widoczna"
            );
        } else if ("Login to your account".equals(sectionText)) {
            Assert.assertTrue(
                    loginPage.isLoginToYourAccountSectionVisible(),
                    "\"Login to your account\" section powinna być widoczna"
            );
        } else if ("SEARCHED PRODUCTS".equals(sectionText)) {
            ProductsPage productsPage = ScenarioContext.get(ScenarioContext.PRODUCTS_PAGE);
            Assert.assertTrue(
                    productsPage != null && productsPage.isSearchedProductsVisible(),
                    "Sekcja \"Searched Products\" powinna być widoczna"
            );
        } else if ("ENTER ACCOUNT INFORMATION".equals(sectionText)) {
            String lastEnteredName = ScenarioContext.get(ScenarioContext.LAST_ENTERED_NAME);
            String lastEnteredEmail = ScenarioContext.get(ScenarioContext.LAST_ENTERED_EMAIL);
            int maxRetries = 3;
            for (int i = 0; i < maxRetries; i++) {
                if (signupPage != null && signupPage.isEnterAccountInformationSectionVisible()) {
                    break;
                }
                if (loginPage != null && loginPage.isEmailAlreadyExistsErrorVisible()) {
                    String uniqueEmail = generateUniqueEmail(lastEnteredEmail);
                    loginPage.enterNewUserNameAndEmail(lastEnteredName, uniqueEmail);
                    loginPage.clickSignupButton();
                    signupPage = new SignupPage(Hooks.driver);
                    ScenarioContext.put(ScenarioContext.SIGNUP_PAGE, signupPage);
                } else {
                    break;
                }
            }
            SignupPage finalSignupPage = ScenarioContext.get(ScenarioContext.SIGNUP_PAGE);
            Assert.assertTrue(
                    finalSignupPage != null && finalSignupPage.isEnterAccountInformationSectionVisible(),
                    "\"ENTER ACCOUNT INFORMATION\" section powinna być widoczna"
            );
        }
    }

    @And("I click the {string} button")
    public void iClickTheButton(String buttonText) {
        LoginPage loginPage = ScenarioContext.get(ScenarioContext.LOGIN_PAGE);
        SignupPage signupPage = ScenarioContext.get(ScenarioContext.SIGNUP_PAGE);
        AccountCreatedPage accountCreatedPage = ScenarioContext.get(ScenarioContext.ACCOUNT_CREATED_PAGE);
        ContactUsPage contactUsPage = ScenarioContext.get(ScenarioContext.CONTACT_US_PAGE);

        if ("Submit".equals(buttonText) && contactUsPage != null) {
            contactUsPage.clickSubmit();
        } else if ("OK".equals(buttonText) && contactUsPage != null) {
            contactUsPage.acceptAlertIfPresent();
        } else if ("Home".equals(buttonText) && contactUsPage != null) {
            contactUsPage.clickHome();
            ScenarioContext.put(ScenarioContext.HOME_PAGE, new HomePage(Hooks.driver));
        } else if ("Signup".equals(buttonText)) {
            loginPage.clickSignupButton();
            ScenarioContext.put(ScenarioContext.SIGNUP_PAGE, new SignupPage(Hooks.driver));
        } else if ("Create Account".equals(buttonText) && signupPage != null) {
            signupPage.clickCreateAccount();
        } else if ("Continue".equals(buttonText)) {
            if (accountCreatedPage != null && accountCreatedPage.isAccountCreatedMessageVisible()) {
                accountCreatedPage.clickContinue();
            } else if (loginPage != null && loginPage.isAccountDeletedMessageVisible()) {
                loginPage.clickContinueAfterDelete();
            }
        } else if ("Delete Account".equals(buttonText)) {
            loginPage.clickDeleteAccount();
        } else if ("Login".equals(buttonText) && loginPage != null) {
            loginPage.clickLoginButton();
        } else if ("Logout".equals(buttonText) && loginPage != null) {
            loginPage.clickLogout();
            ScenarioContext.put(ScenarioContext.LOGIN_PAGE, new LoginPage(Hooks.driver));
        }
    }

    @Then("I should be navigated to the login page")
    public void iShouldBeNavigatedToTheLoginPage() {
        LoginPage loginPage = new LoginPage(Hooks.driver);
        Assert.assertTrue(
                loginPage.isLoginToYourAccountSectionVisible(),
                "Użytkownik powinien być przekierowany na stronę logowania"
        );
    }

    @Then("I should see {string} message")
    public void iShouldSeeMessage(String messageText) {
        LoginPage loginPage = ScenarioContext.get(ScenarioContext.LOGIN_PAGE);
        AccountCreatedPage accountCreatedPage = ScenarioContext.get(ScenarioContext.ACCOUNT_CREATED_PAGE);
        ContactUsPage contactUsPage = ScenarioContext.get(ScenarioContext.CONTACT_US_PAGE);

        if ("Success! Your details have been submitted successfully.".equals(messageText) && contactUsPage != null) {
            Assert.assertTrue(
                    contactUsPage.isSuccessMessageVisible(),
                    "Komunikat sukcesu Contact Us powinien być widoczny"
            );
        } else if ("Your email or password is incorrect!".equals(messageText) && loginPage != null) {
            Assert.assertTrue(
                    loginPage.isLoginIncorrectErrorVisible(),
                    "Komunikat \"Your email or password is incorrect!\" powinien być widoczny"
            );
        } else if ("Email Address already exist!".equals(messageText) && loginPage != null) {
            Assert.assertTrue(
                    loginPage.isEmailAlreadyExistsErrorVisible(),
                    "Komunikat \"Email Address already exist!\" powinien być widoczny"
            );
        } else if ("ACCOUNT CREATED!".equals(messageText)) {
            if (accountCreatedPage == null) {
                accountCreatedPage = new AccountCreatedPage(Hooks.driver);
                ScenarioContext.put(ScenarioContext.ACCOUNT_CREATED_PAGE, accountCreatedPage);
            }
            Assert.assertTrue(
                    accountCreatedPage.isAccountCreatedMessageVisible(),
                    "ACCOUNT CREATED! powinno być widoczne"
            );
        } else if ("ACCOUNT DELETED!".equals(messageText)) {
            Assert.assertTrue(
                    loginPage.isAccountDeletedMessageVisible(),
                    "ACCOUNT DELETED! powinno być widoczne"
            );
        }
    }

    static String generateUniqueEmail(String baseEmail) {
        String localPart = baseEmail != null && baseEmail.contains("@")
                ? baseEmail.substring(0, baseEmail.indexOf("@"))
                : (baseEmail != null ? baseEmail : "");
        String domain = baseEmail != null && baseEmail.contains("@")
                ? baseEmail.substring(baseEmail.indexOf("@"))
                : "@example.com";
        return localPart + "+" + System.currentTimeMillis() + domain;
    }
}
