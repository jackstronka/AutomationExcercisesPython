package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object dla strony https://automationexercise.com/products – All Products.
 */
public class ProductsPage extends BasePage {

    private final By allProductsHeading = By.xpath("//h2[contains(text(),'All Products')]");
    private final By productsList = By.cssSelector(".features_items .single-products, .features_items .product-image-wrapper");
    private final By firstViewProductLink = By.xpath("(//a[contains(@href,'product_details')])[1]");
    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");
    private final By searchedProductsHeading = By.xpath("//h2[contains(.,'Searched Products')]");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isAllProductsPageVisible() {
        return isElementPresent(allProductsHeading);
    }

    public boolean isProductsListVisible() {
        return !driver.findElements(productsList).isEmpty();
    }

    public void clickFirstViewProduct() {
        scrollIntoView(firstViewProductLink);
        clickViaJavaScript(firstViewProductLink);
    }

    public void searchProduct(String searchTerm) {
        writeText(searchInput, searchTerm);
        click(searchButton);
    }

    public boolean isSearchedProductsVisible() {
        return isElementPresent(searchedProductsHeading);
    }

    public boolean areSearchResultsContaining(String expectedProductName) {
        String pageText = driver.findElement(By.tagName("body")).getText();
        return pageText.contains(expectedProductName);
    }
}
