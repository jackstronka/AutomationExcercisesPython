package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object dla strony szczegółów produktu (np. /product_details/1).
 */
public class ProductDetailPage extends BasePage {

    private final By productName = By.cssSelector(".product-information h2, .product-info h2");
    private final By productCategory = By.xpath("//div[contains(@class,'product-information') or contains(@class,'product-info')]//*[contains(.,'Category:')]");
    private final By productPrice = By.xpath("//div[contains(@class,'product-information') or contains(@class,'product-info')]//*[contains(.,'Rs.')]");
    private final By productAvailability = By.xpath("//div[contains(@class,'product-information') or contains(@class,'product-info')]//*[contains(.,'Availability:')]");
    private final By productCondition = By.xpath("//div[contains(@class,'product-information') or contains(@class,'product-info')]//*[contains(.,'Condition:')]");
    private final By productBrand = By.xpath("//div[contains(@class,'product-information') or contains(@class,'product-info')]//*[contains(.,'Brand:')]");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    public boolean isProductNameVisible() {
        return isElementPresent(productName);
    }

    public boolean isProductCategoryVisible() {
        return isElementPresent(productCategory);
    }

    public boolean isProductPriceVisible() {
        return isElementPresent(productPrice);
    }

    public boolean isProductAvailabilityVisible() {
        return isElementPresent(productAvailability);
    }

    public boolean isProductConditionVisible() {
        return isElementPresent(productCondition);
    }

    public boolean isProductBrandVisible() {
        return isElementPresent(productBrand);
    }

    public boolean areAllProductDetailsVisible() {
        return isProductNameVisible()
                && isProductCategoryVisible()
                && isProductPriceVisible()
                && isProductAvailabilityVisible()
                && isProductConditionVisible()
                && isProductBrandVisible();
    }
}
