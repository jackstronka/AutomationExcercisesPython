package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ContactPage extends BasePage{

    // ***** Constructor *****
        public ContactPage (WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }


    // ***** Page variables *****
    String contactUsPageURL = "https://www.b2c2.com/contact-us";

    // ***** WebElements *****
    private final String teamSelectListId = "w-dropdown-toggle-5";
    private final String onboardingTeamOptionXpath = "//a[text()='Onboarding']\n";
    private final String nameFieldId = "Name";
    private final String emailId = "email";
    private final String yourOrganizationListId = "w-dropdown-toggle-6";
    private final String exchangeOptionXpath = "//*[@id=\"w-dropdown-list-6\"]/a[2]";
    private final String companyNameId = "Company";
    private final String positionAtCompanyId = "Position-at-company-2";
    private final String tradingVolumeId = "w-dropdown-toggle-7";
    private final String oneToTenMillionOptionXpatch = "//*[@id=\"w-dropdown-list-7\"]/a[3]";
    private final String messageFieldId = "Message";
    private final String submitButtonXpah = "//*[@id=\"wf-form-Email-Form-2-Contact-Page\"]/div[2]/div[2]/input";



    public void openContactUs() {
        driver.get(contactUsPageURL);
    }

    public void acceptCookiesIfPresent() {
        try {
            WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[data-tid='banner-accept']")));
            acceptButton.click();


            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.cssSelector("div[class*='t-consentPrompt']")));
        } catch (Exception ignored) {

        }
    }


    public void selectTeamOnboarding() {
        driver.findElement(By.id(teamSelectListId)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(onboardingTeamOptionXpath))).click();
    }

    public void enterName(String name) {
        driver.findElement(By.id(nameFieldId)).sendKeys(name);
    }

    public void enterEmail(String email) {
        driver.findElement(By.id(emailId)).sendKeys(email);
    }

    public void selectOrganizationExchange() {
        driver.findElement(By.id(yourOrganizationListId)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(exchangeOptionXpath))).click();

    }

    public void enterCompanyName(String company) {
        driver.findElement(By.id(companyNameId)).sendKeys(company);
    }

    public void enterPosition(String position) {
        driver.findElement(By.id(positionAtCompanyId)).sendKeys(position);
    }

    public void selectTradingVolumeOneToTenMilLion() {
        driver.findElement(By.id(tradingVolumeId)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(oneToTenMillionOptionXpatch))).click();
    }

    public void enterMessage(String message) {
        driver.findElement(By.id(messageFieldId)).sendKeys(message);
    }

    public void submitForm() {
        driver.findElement(By.xpath(submitButtonXpah)).click();
    }


}
