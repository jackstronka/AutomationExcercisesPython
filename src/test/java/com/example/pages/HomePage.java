package com.example.pages;

import com.example.utilities.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    private final By header = By.tagName("header");
    // Link: <a href="/login"><i class="fa fa-lock"></i> Signup / Login</a>
    private final By signupLoginLink = By.cssSelector("a[href='/login']");
    private final By contactUsLink = By.cssSelector("a[href='/contact_us']");
    private final By productsLink = By.cssSelector("a[href='/products']");
    private final By firstHomeViewProductLink = By.xpath("(//a[contains(@href,'product_details')])[1]");
    private final By cartLink = By.cssSelector("a[href='/view_cart']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        WebElement headerElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(header)
        );
        return headerElement.isDisplayed();
    }

    public void clickSignupLogin() {
        // JS click – bypasses cookie/ad overlay blocking the element
        clickViaJavaScript(signupLoginLink);
    }

    public void clickContactUs() {
        clickViaJavaScript(contactUsLink);
    }

    public void clickProducts() {
        clickViaJavaScript(productsLink);
    }

    public void clickFirstHomeViewProduct() {
        clickViaJavaScript(firstHomeViewProductLink);
    }

    public void clickCart() {
        clickViaJavaScript(cartLink);
    }

    public void navigateToHome() {
        driver.get(ConfigReader.get("baseUrl"));
    }
}