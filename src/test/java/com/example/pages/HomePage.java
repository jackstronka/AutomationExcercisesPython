package com.example.pages;

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
        click(signupLoginLink);
    }

    public void clickContactUs() {
        click(contactUsLink);
    }

    public void clickProducts() {
        click(productsLink);
    }
}