package com.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    private final By header = By.tagName("header");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        WebElement headerElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(header)
        );
        return headerElement.isDisplayed();
    }
}