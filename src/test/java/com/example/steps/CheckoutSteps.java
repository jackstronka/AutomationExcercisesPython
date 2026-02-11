package com.example.steps;

import com.example.context.ScenarioContext;
import com.example.hooks.Hooks;
import com.example.pages.CartPage;
import com.example.pages.CheckoutPage;
import com.example.pages.LoginPage;
import com.example.pages.OrderSuccessPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.util.Map;

public class CheckoutSteps {

    @Then("I should see the cart page")
    public void iShouldSeeTheCartPage() {
        CartPage cartPage = new CartPage(Hooks.driver);
        Assert.assertTrue(
                cartPage.isCartPageVisible(),
                "Cart page (Shopping Cart) should be visible"
        );
    }

    @When("I click the Proceed To Checkout button")
    public void iClickTheProceedToCheckoutButton() {
        CartPage cartPage = new CartPage(Hooks.driver);
        cartPage.clickProceedToCheckout();
    }

    @And("I click Register \\/ Login in checkout modal")
    public void iClickRegisterLoginInCheckoutModal() {
        // Modal on /view_cart with Register / Login link
        Hooks.driver.findElement(By.cssSelector(".modal-content a[href='/login']")).click();
        // After click we are on login page – update context
        ScenarioContext.put(ScenarioContext.LOGIN_PAGE, new LoginPage(Hooks.driver));
    }

    @Then("I should see Address Details and Review Your Order")
    public void iShouldSeeAddressDetailsAndReviewYourOrder() {
        CheckoutPage checkoutPage = new CheckoutPage(Hooks.driver);
        Assert.assertTrue(
                checkoutPage.isAddressAndReviewVisible(),
                "\"Address Details\" and \"Review Your Order\" should be visible"
        );
    }

    @When("I enter {string} in order comment")
    public void iEnterInOrderComment(String comment) {
        CheckoutPage checkoutPage = new CheckoutPage(Hooks.driver);
        checkoutPage.enterOrderComment(comment);
    }

    @And("I click Place Order button")
    public void iClickPlaceOrderButton() {
        CheckoutPage checkoutPage = new CheckoutPage(Hooks.driver);
        checkoutPage.clickPlaceOrder();
    }

    @And("I enter payment details:")
    public void iEnterPaymentDetails(DataTable dataTable) {
        Map<String, String> row = dataTable.asMaps(String.class, String.class).get(0);
        CheckoutPage checkoutPage = new CheckoutPage(Hooks.driver);
        checkoutPage.fillPaymentDetails(
                row.get("nameOnCard"),
                row.get("cardNumber"),
                row.get("cvc"),
                row.get("expiryMonth"),
                row.get("expiryYear")
        );
    }

    @And("I click Pay and Confirm Order button")
    public void iClickPayAndConfirmOrderButton() {
        CheckoutPage checkoutPage = new CheckoutPage(Hooks.driver);
        checkoutPage.clickPayAndConfirmOrder();
    }

    @When("I click the Download Invoice button")
    public void iClickTheDownloadInvoiceButton() {
        OrderSuccessPage orderSuccessPage = new OrderSuccessPage(Hooks.driver);
        orderSuccessPage.clickDownloadInvoice();
    }

    /** Continue only on payment_done page – button a[data-qa='continue-button']. */
    @When("I click the Continue button on order success page")
    public void iClickTheContinueButtonOnOrderSuccessPage() {
        OrderSuccessPage orderSuccessPage = new OrderSuccessPage(Hooks.driver);
        Assert.assertTrue(
                orderSuccessPage.waitUntilContinueClickable(),
                "Continue button on payment_done page (data-qa='continue-button') should be clickable"
        );
        orderSuccessPage.clickContinue();
    }

    @Then("the invoice is downloaded successfully")
    public void theInvoiceIsDownloadedSuccessfully() {
        OrderSuccessPage orderSuccessPage = new OrderSuccessPage(Hooks.driver);
        Assert.assertTrue(
                orderSuccessPage.waitUntilContinueClickable(),
                "Continue button on payment_done page should be clickable after Download Invoice click"
        );
    }
}

