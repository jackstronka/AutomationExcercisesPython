package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for /login page – contains login form and new user registration section.
 */
public class LoginPage extends BasePage {

    // ===== Selectors for "New User Signup!" section (data-qa from HTML) =====
    private final By newUserNameInput = By.cssSelector("[data-qa='signup-name']");
    private final By newUserEmailInput = By.cssSelector("[data-qa='signup-email']");
    private final By signupButton = By.cssSelector("[data-qa='signup-button']");
    private final By newUserSignupSection = By.xpath("//*[contains(text(),'New User Signup!')]");

    // Message when email already exists: <p style="color: red;">Email Address already exist!</p>
    private final By emailAlreadyExistsError = By.xpath("//p[contains(text(),'Email Address already exist!')]");

    // Message on incorrect login: "Your email or password is incorrect!"
    private final By loginIncorrectError = By.xpath("//p[contains(text(),'Your email or password is incorrect!')]");

    // ===== Selectors for "Login to your account" section (data-qa from HTML) =====
    private final By loginToYourAccountSection = By.xpath("//*[contains(text(),'Login to your account')]");
    private final By loginEmailInput = By.cssSelector("[data-qa='login-email']");
    private final By loginPasswordInput = By.cssSelector("[data-qa='login-password']");
    private final By loginButton = By.cssSelector("[data-qa='login-button']");

    // Selector for "Logged in as <user>" info in header
    private final By loggedInAsLabel =
            By.xpath("//*[contains(.,'Logged in as') or contains(.,'LOGGED IN AS')]");

    // Account-related button selectors (href works on /payment_done etc.)
    private final By deleteAccountButton =
            By.cssSelector("a[href='/delete_account']");

    private final By accountDeletedMessage =
            By.xpath("//*[contains(.,'Account Deleted') or contains(.,'ACCOUNT DELETED')]");

    private final By continueButtonAfterDelete =
            By.xpath("//a[contains(.,'Continue') or contains(.,'CONTINUE')]");

    private final By logoutLink = By.xpath("//a[contains(.,'Logout') or contains(.,'LOGOUT')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // ===== Akcje sekcji "New User Signup!" =====

    public void enterNewUserNameAndEmail(String name, String email) {
        writeText(newUserNameInput, name);
        writeText(newUserEmailInput, email);
    }

    public void clickSignupButton() {
        click(signupButton);
    }

    public boolean isNewUserSignupSectionVisible() {
        return isElementPresent(newUserSignupSection);
    }

    public boolean isEmailAlreadyExistsErrorVisible() {
        return isElementPresent(emailAlreadyExistsError);
    }

    public boolean isLoginIncorrectErrorVisible() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            shortWait.until(ExpectedConditions.presenceOfElementLocated(loginIncorrectError));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginToYourAccountSectionVisible() {
        return isElementPresent(loginToYourAccountSection);
    }

    public void clickLogout() {
        click(logoutLink);
    }

    public void enterLoginCredentials(String email, String password) {
        writeText(loginEmailInput, email);
        writeText(loginPasswordInput, password);
    }

    public void clickLoginButton() {
        click(loginButton);
    }

    public boolean isLoggedInAsUser() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(loggedInAsLabel));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickDeleteAccount() {
        clickViaJavaScript(deleteAccountButton);
    }

    public boolean isAccountDeletedMessageVisible() {
        try {
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement el = longWait.until(ExpectedConditions.presenceOfElementLocated(accountDeletedMessage));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
            longWait.until(ExpectedConditions.visibilityOf(el));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickContinueAfterDelete() {
        clickViaJavaScript(continueButtonAfterDelete);
    }
}

