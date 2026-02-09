package com.example.steps;

import com.example.context.ScenarioContext;
import com.example.pages.ProductsPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class SearchProductSteps {

    @When("I enter {string} in search and click search button")
    public void iEnterInSearchAndClickSearch(String searchTerm) {
        ProductsPage productsPage = ScenarioContext.get(ScenarioContext.PRODUCTS_PAGE);
        productsPage.searchProduct(searchTerm);
    }

    @And("I should see products related to search containing {string}")
    public void iShouldSeeProductsRelatedToSearchContaining(String expectedProductName) {
        ProductsPage productsPage = ScenarioContext.get(ScenarioContext.PRODUCTS_PAGE);
        Assert.assertTrue(
                productsPage.areSearchResultsContaining(expectedProductName),
                "Wyniki wyszukiwania powinny zawierać: " + expectedProductName
        );
    }
}
