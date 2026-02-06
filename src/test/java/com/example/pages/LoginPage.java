package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object dla strony /login – zawiera formularz logowania oraz sekcję rejestracji nowego użytkownika.
 */
public class LoginPage extends BasePage {

    // ===== Lokatory sekcji "New User Signup!" (data-qa z HTML-a) =====
    private final By newUserNameInput = By.cssSelector("[data-qa='signup-name']");
    private final By newUserEmailInput = By.cssSelector("[data-qa='signup-email']");
    private final By signupButton = By.cssSelector("[data-qa='signup-button']");
    private final By newUserSignupSection = By.xpath("//*[contains(text(),'New User Signup!')]");

    // Komunikat gdy email już istnieje: <p style="color: red;">Email Address already exist!</p>
    private final By emailAlreadyExistsError = By.xpath("//p[contains(text(),'Email Address already exist!')]");

    // Komunikat przy błędnym logowaniu: "Your email or password is incorrect!"
    private final By loginIncorrectError = By.xpath("//p[contains(text(),'Your email or password is incorrect!')]");

    // ===== Lokatory sekcji "Login to your account" (data-qa z HTML-a) =====
    private final By loginToYourAccountSection = By.xpath("//*[contains(text(),'Login to your account')]");
    private final By loginEmailInput = By.cssSelector("[data-qa='login-email']");
    private final By loginPasswordInput = By.cssSelector("[data-qa='login-password']");
    private final By loginButton = By.cssSelector("[data-qa='login-button']");

    // Lokator dla informacji typu "Logged in as <user>" w headerze
    private final By loggedInAsLabel =
            By.xpath("//*[contains(.,'Logged in as') or contains(.,'LOGGED IN AS')]");

    // Lokatory przycisków związanych z kontem
    private final By deleteAccountButton =
            By.xpath("//a[contains(.,'Delete Account') or contains(.,'DELETE ACCOUNT')]");

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
        return isElementPresent(loginIncorrectError);
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

