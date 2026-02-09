package com.example.steps;

import com.example.context.ScenarioContext;
import com.example.hooks.Hooks;
import com.example.pages.HomePage;
import com.example.pages.ProductDetailPage;
import com.example.pages.ProductsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class ProductsSteps {

    @When("I click on Products button")
    public void iClickOnProductsButton() {
        HomePage homePage = ScenarioContext.get(ScenarioContext.HOME_PAGE);
        homePage.clickProducts();
        ScenarioContext.put(ScenarioContext.PRODUCTS_PAGE, new ProductsPage(Hooks.driver));
    }

    @Then("I should be navigated to the All Products page")
    public void iShouldBeNavigatedToAllProductsPage() {
        ProductsPage productsPage = ScenarioContext.get(ScenarioContext.PRODUCTS_PAGE);
        if (productsPage == null) {
            productsPage = new ProductsPage(Hooks.driver);
            ScenarioContext.put(ScenarioContext.PRODUCTS_PAGE, productsPage);
        }
        Assert.assertTrue(
                productsPage.isAllProductsPageVisible(),
                "Użytkownik powinien być przekierowany na stronę All Products"
        );
    }

    @And("the products list is visible")
    public void theProductsListIsVisible() {
        ProductsPage productsPage = ScenarioContext.get(ScenarioContext.PRODUCTS_PAGE);
        Assert.assertTrue(
                productsPage.isProductsListVisible(),
                "Lista produktów powinna być widoczna"
        );
    }

    @When("I click on {string} of the first product")
    public void iClickOnViewProductOfFirst(String linkText) {
        if ("View Product".equals(linkText)) {
            ProductsPage productsPage = ScenarioContext.get(ScenarioContext.PRODUCTS_PAGE);
            productsPage.clickFirstViewProduct();
            ScenarioContext.put(ScenarioContext.PRODUCT_DETAIL_PAGE, new ProductDetailPage(Hooks.driver));
        }
    }

    @Then("I should be on the product detail page")
    public void iShouldBeOnProductDetailPage() {
        ProductDetailPage productDetailPage = ScenarioContext.get(ScenarioContext.PRODUCT_DETAIL_PAGE);
        if (productDetailPage == null) {
            productDetailPage = new ProductDetailPage(Hooks.driver);
            ScenarioContext.put(ScenarioContext.PRODUCT_DETAIL_PAGE, productDetailPage);
        }
        Assert.assertTrue(
                productDetailPage.isProductNameVisible(),
                "Użytkownik powinien być na stronie szczegółów produktu"
        );
    }

    @And("I should see product details: product name, category, price, availability, condition, brand")
    public void iShouldSeeProductDetails() {
        ProductDetailPage productDetailPage = ScenarioContext.get(ScenarioContext.PRODUCT_DETAIL_PAGE);
        Assert.assertTrue(
                productDetailPage.areAllProductDetailsVisible(),
                "Wszystkie szczegóły produktu (nazwa, kategoria, cena, dostępność, stan, marka) powinny być widoczne"
        );
    }
}
