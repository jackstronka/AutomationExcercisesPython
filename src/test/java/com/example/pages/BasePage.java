package com.example.pages;

import com.example.utilities.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(
                        Long.parseLong(ConfigReader.get("explicitWait", "10"))
                )
        );
    }



    // ===== Common actions =====

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /** Kliknięcie przez JS – omija overlay/reklamy zasłaniające element. */
    protected void clickViaJavaScript(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void writeText(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    protected String readText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    protected boolean isElementPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    protected WebElement getElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /** Ustawienie wartości select przez JS – omija overlay/reklamy zasłaniające element. */
    protected void selectByValueViaJavaScript(By locator, String value) {
        if (value == null || value.isEmpty()) return;
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                element, value
        );
    }

    /** Ustawienie wartości select przez visible text – omija overlay. */
    protected void selectByVisibleTextViaJavaScript(By locator, String text) {
        if (text == null || text.isEmpty()) return;
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript(
                "var opt = Array.from(arguments[0].options).find(o => o.text === arguments[1]); if(opt) { arguments[0].value = opt.value; arguments[0].dispatchEvent(new Event('change', { bubbles: true })); }",
                element, text
        );
    }

    // ===== Generic page info =====

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
