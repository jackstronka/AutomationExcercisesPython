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

    /** Click via JS – bypasses overlay/ads blocking the element. */
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

    /** Scrolls element into view (center of viewport). */
    protected void scrollIntoView(By locator) {
        WebElement element = getElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    /** Set select value via JS – bypasses overlay/ads blocking the element. */
    protected void selectByValueViaJavaScript(By locator, String value) {
        if (value == null || value.isEmpty()) return;
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                element, value
        );
    }

    /** Set select value by visible text – bypasses overlay. */
    protected void selectByVisibleTextViaJavaScript(By locator, String text) {
        if (text == null || text.isEmpty()) return;
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript(
                "var opt = Array.from(arguments[0].options).find(o => o.text === arguments[1]); if(opt) { arguments[0].value = opt.value; arguments[0].dispatchEvent(new Event('change', { bubbles: true })); }",
                element, text
        );
    }

    /** Convenience wrapper for select by value. */
    protected void selectByValue(By locator, String value) {
        if (value == null || value.isEmpty()) return;
        selectByValueViaJavaScript(locator, value);
    }

    /** Convenience wrapper for select by visible text. */
    protected void selectByVisibleText(By locator, String text) {
        if (text == null || text.isEmpty()) return;
        selectByVisibleTextViaJavaScript(locator, text);
    }

    /** Checks if checkbox/radio is selected. */
    protected boolean isCheckboxSelected(By locator) {
        return isElementPresent(locator) && getElement(locator).isSelected();
    }

    // ===== Generic page info =====

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
