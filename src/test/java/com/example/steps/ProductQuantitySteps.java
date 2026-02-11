package com.example.steps;

import com.example.context.ScenarioContext;
import com.example.hooks.Hooks;
import com.example.pages.CartPage;
import com.example.pages.HomePage;
import com.example.pages.ProductDetailPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.testng.Assert;

public class ProductQuantitySteps {

    @When("I click on View Product for first product on home page")
    public void iClickOnViewProductForFirstProductOnHomePage() {
        HomePage homePage = ScenarioContext.get(ScenarioContext.HOME_PAGE);
        homePage.clickFirstHomeViewProduct();
        ScenarioContext.put(ScenarioContext.PRODUCT_DETAIL_PAGE, new ProductDetailPage(Hooks.driver));
    }

    @When("I set product quantity to {int}")
    public void iSetProductQuantityTo(int quantity) {
        // On Automation Exercise page quantity field usually has id='quantity'
        Hooks.driver.findElement(By.id("quantity")).clear();
        Hooks.driver.findElement(By.id("quantity")).sendKeys(String.valueOf(quantity));
    }

    @And("I click Add to cart on product detail page")
    public void iClickAddToCartOnProductDetailPage() {
        ProductDetailPage productDetailPage = ScenarioContext.get(ScenarioContext.PRODUCT_DETAIL_PAGE);
        productDetailPage.clickAddToCart();
    }

    @And("I click View Cart button")
    public void iClickViewCartButton() {
        ProductDetailPage productDetailPage = ScenarioContext.get(ScenarioContext.PRODUCT_DETAIL_PAGE);
        productDetailPage.clickViewCartInModal();
    }

    @Then("I should see the product in cart with quantity {int}")
    public void iShouldSeeTheProductInCartWithQuantity(int expectedQuantity) {
        CartPage cartPage = new CartPage(Hooks.driver);
        cartPage.waitForProductToAppear();
        Assert.assertTrue(
                cartPage.isAnyProductVisible(),
                "At least one product should be visible in cart"
        );
        int actualQuantity = cartPage.getFirstProductQuantity();
        Assert.assertEquals(
                actualQuantity,
                expectedQuantity,
                "Product quantity in cart should equal " + expectedQuantity
        );
    }
}

