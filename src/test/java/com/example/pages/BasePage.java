package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;



public class BasePage {
    public WebDriver driver;
    public WebDriverWait wait;

    //Constructor
    public BasePage (WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // ***** Methods *****
    public void sendKeys (By elementLocation, Keys keys, String string) {
        driver.findElement(elementLocation).sendKeys(keys, string);
    }
    public void click (By elementLocation) {
        driver.findElement(elementLocation).click();
    }

    public void writeText (By elementLocation, String text) {
        driver.findElement(elementLocation).sendKeys(text);
    }

    public String readText (By elementLocation) {
        return driver.findElement(elementLocation).getText();
    }

    public void clearText (By elementLocation) {
        driver.findElement(elementLocation).clear();
    }

    public boolean checkWebElementPresence(By locator) {
        boolean result = false;
        List<WebElement> elementList = driver.findElements(locator);
        if(elementList.size() > 0) {
            result = true;
        }
        return result;
    }

    public WebElement getWebElement(By locator) {
        WebElement element = driver.findElement(locator);
        return element;
    }

}
