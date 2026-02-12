package com.example.pages;

import com.example.utilities.ConfigReader;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for order success page (payment_done).
 * Contains: Download Invoice, Continue (data-qa="continue-button").
 */
public class OrderSuccessPage extends BasePage {

    private static final By ORDER_SUCCESS_MESSAGE =
            By.xpath("//*[contains(.,'Your order has been placed successfully!')]");

    // <a href="/download_invoice/500" class="btn btn-default check_out">Download Invoice</a>
    private final By downloadInvoiceLink =
            By.cssSelector("a.btn.btn-default.check_out[href^='/download_invoice']");
    // <a href="/" data-qa="continue-button" class="btn btn-primary">Continue</a>
    private final By continueButton =
            By.cssSelector("a.btn.btn-primary[data-qa='continue-button']");

    public OrderSuccessPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click Download Invoice")
    public void clickDownloadInvoice() {
        scrollIntoView(downloadInvoiceLink);
        click(downloadInvoiceLink);
    }

    @Step("Click Continue on order success page")
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

    /**
     * Waits for order success (redirect to payment_done or success message on page).
     * Uses orderSuccessWaitTimeout from config.
     */
    public boolean waitForOrderSuccessMessageOrPaymentDone() {
        int timeout = Integer.parseInt(ConfigReader.get("orderSuccessWaitTimeout", "15"));
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        try {
            w.until(d -> {
                if (d.getCurrentUrl() != null && d.getCurrentUrl().contains("payment_done")) {
                    return true;
                }
                return !d.findElements(ORDER_SUCCESS_MESSAGE).isEmpty();
            });
            return true;
        } catch (Exception e) {
            return driver.getCurrentUrl() != null && driver.getCurrentUrl().contains("payment_done");
        }
    }
}
