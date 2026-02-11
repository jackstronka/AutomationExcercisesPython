package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for order success page (payment_done).
 * Contains: Download Invoice, Continue (data-qa="continue-button").
 */
public class OrderSuccessPage extends BasePage {

    // <a href="/download_invoice/500" class="btn btn-default check_out">Download Invoice</a>
    private final By downloadInvoiceLink =
            By.cssSelector("a.btn.btn-default.check_out[href^='/download_invoice']");
    // <a href="/" data-qa="continue-button" class="btn btn-primary">Continue</a>
    private final By continueButton =
            By.cssSelector("a.btn.btn-primary[data-qa='continue-button']");

    public OrderSuccessPage(WebDriver driver) {
        super(driver);
    }

    /** Clicks "Download Invoice". */
    public void clickDownloadInvoice() {
        scrollIntoView(downloadInvoiceLink);
        click(downloadInvoiceLink);
    }

    /** Clicks "Continue" (return to home page). */
    public void clickContinue() {
        clickViaJavaScript(continueButton);
    }

    /** Checks if Continue button is visible and clickable. */
    public boolean isContinueButtonVisible() {
        if (!isElementPresent(continueButton)) {
            return false;
        }
        WebElement el = getElement(continueButton);
        return el.isDisplayed() && el.isEnabled();
    }

    /** Waits until Continue button is clickable. */
    public boolean waitUntilContinueClickable() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(continueButton));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Checks if Download Invoice link is visible. */
    public boolean isDownloadInvoiceVisible() {
        return isElementPresent(downloadInvoiceLink);
    }
}
