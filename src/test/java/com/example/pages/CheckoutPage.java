package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for checkout page (address + payment). Order success page (payment_done)
 * is in {@link OrderSuccessPage}.
 */
public class CheckoutPage extends BasePage {

    private final By addressDetailsSection = By.xpath("//*[contains(.,'Address Details')]");
    private final By reviewYourOrderSection = By.xpath("//*[contains(.,'Review Your Order')]");
    private final By commentTextarea = By.name("message");

    private final By nameOnCardInput = By.name("name_on_card");
    private final By cardNumberInput = By.name("card_number");
    private final By cvcInput = By.name("cvc");
    private final By expiryMonthInput = By.name("expiry_month");
    private final By expiryYearInput = By.name("expiry_year");

    private final By placeOrderButton = By.cssSelector("a.check_out[href='/payment'], a.check_out");
    private final By payAndConfirmOrderButton = By.cssSelector("button[data-qa='pay-button'], button.btn.btn-default.check_out");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public boolean isAddressAndReviewVisible() {
        return isElementPresent(addressDetailsSection) && isElementPresent(reviewYourOrderSection);
    }

    public void enterOrderComment(String comment) {
        writeText(commentTextarea, comment);
    }

    public void fillPaymentDetails(String nameOnCard,
                                   String cardNumber,
                                   String cvc,
                                   String expiryMonth,
                                   String expiryYear) {
        scrollIntoView(nameOnCardInput);
        writeText(nameOnCardInput, nameOnCard);
        writeText(cardNumberInput, cardNumber);
        writeText(cvcInput, cvc);
        writeText(expiryMonthInput, expiryMonth);
        writeText(expiryYearInput, expiryYear);
    }

    /** Click via JS – browser bar (e.g. "Save password?") may block the button. */
    public void clickPlaceOrder() {
        clickViaJavaScript(placeOrderButton);
    }

    public void clickPayAndConfirmOrder() {
        click(payAndConfirmOrderButton);
    }
}

