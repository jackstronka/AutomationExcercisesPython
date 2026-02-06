package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object dla strony /account_created – potwierdzenie utworzenia konta.
 * Zawiera: Account Created!, Congratulations message oraz przycisk Continue.
 */
public class AccountCreatedPage extends BasePage {

    private final By accountCreatedHeading =
            By.xpath("//*[contains(.,'Account Created') or contains(.,'ACCOUNT CREATED')]");
    private final By continueButton =
            By.xpath("//*[contains(.,'Account Created') or contains(.,'ACCOUNT CREATED')]//a[contains(.,'Continue') or contains(.,'CONTINUE')]");

    public AccountCreatedPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        return isElementPresent(accountCreatedHeading);
    }

    public boolean isAccountCreatedMessageVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(accountCreatedHeading));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickContinue() {
        clickViaJavaScript(continueButton);
    }
}
