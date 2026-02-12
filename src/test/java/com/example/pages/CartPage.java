package com.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for cart page (/view_cart).
 */
public class CartPage extends BasePage {

    // Main cart page container
    private final By cartPageHeading = By.cssSelector("#cart_items, .cart_info, .table.table-condensed");
    // First product row in cart
    private final By firstCartProductRow = By.cssSelector("tr[id^='product-']");
    // Proceed To Checkout button
    private final By proceedToCheckoutButton = By.cssSelector("a[href='/checkout'], a.check_out");
    // Register / Login link in checkout modal (appears when cart has items, user not logged in)
    private final By registerLoginInModal = By.cssSelector(".modal-content a[href='/login']");
    // Quantity cell – may be button/span/text in different implementations
    private final By quantityCellInsideFirstRow = By.cssSelector(
            "tr[id^='product-'] td.cart_quantity, " +
            "tr[id^='product-'] td.cart_quantity button, " +
            "tr[id^='product-'] td.cart_quantity span"
    );

    public CartPage(WebDriver driver) {
        super(driver);
    }

    /** Waits for product to appear in cart (gives time for session update). */
    public void waitForProductToAppear() {
        wait.until(ExpectedConditions.presenceOfElementLocated(firstCartProductRow));
    }

    public boolean isCartPageVisible() {
        return isElementPresent(cartPageHeading);
    }

    public boolean isAnyProductVisible() {
        return isElementPresent(firstCartProductRow);
    }

    @Step("Click Proceed To Checkout")
    public void clickProceedToCheckout() {
        click(proceedToCheckoutButton);
    }

    @Step("Click Register / Login in checkout modal")
    public void clickRegisterLoginInCheckoutModal() {
        click(registerLoginInModal);
    }

    public int getFirstProductQuantity() {
        String text = readText(quantityCellInsideFirstRow).trim();
        // Text may contain other characters – extract digits only
        String digits = text.replaceAll("[^0-9]", "");
        return digits.isEmpty() ? 0 : Integer.parseInt(digits);
    }
}

