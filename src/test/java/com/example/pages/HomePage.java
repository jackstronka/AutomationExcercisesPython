package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends BasePage {

    // ***** Constructor *****
    public HomePage (WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }


    // ***** Page variables *****
    String PageURL = "https://www.b2c2.com/";


    // ***** WebElements *****
    String contactButtonId = "w-node-a094f373-c424-e839-0601-e63d23c7f149-23c7ef81";
    String subscribeButtonId = "a094f373-c424-e839-0601-e63d23c7f14c";
    String cookiesAcceptButtonXpath = "//*[@id=\"termly-code-snippet-support\"]/div/div/div/div/div[2]/button[3]";

    // ***** Page Methods *****
    public void goToB2C2HomePage () {
        driver.get(PageURL);
    }

    public void goToContactPage() { click(By.id(contactButtonId)); }

    public boolean isHomePageDisplayed() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id(contactButtonId)));
        return this.checkWebElementPresence(By.id(contactButtonId));}

    public void acceptCookieButton() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(cookiesAcceptButtonXpath)));
        click(By.xpath(cookiesAcceptButtonXpath));
    }

}
