package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object dla strony /contact_us – formularz Contact Us.
 */
public class ContactUsPage extends BasePage {

    private final By getInTouchHeading = By.xpath("//h2[contains(text(),'Get In Touch')]");
    private final By nameInput = By.cssSelector("[data-qa='name']");
    private final By emailInput = By.cssSelector("[data-qa='email']");
    private final By subjectInput = By.cssSelector("[data-qa='subject']");
    private final By messageTextarea = By.cssSelector("[data-qa='message']");
    private final By uploadFileInput = By.cssSelector("input[name='upload_file']");
    private final By submitButton = By.cssSelector("[data-qa='submit-button']");
    private final By successMessage = By.xpath("//*[contains(text(),'Success! Your details have been submitted successfully.')]");
    private final By homeLink = By.cssSelector("a[href='/']");

    public ContactUsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isGetInTouchVisible() {
        return isElementPresent(getInTouchHeading);
    }

    public void fillContactForm(String name, String email, String subject, String message) {
        writeText(nameInput, name);
        writeText(emailInput, email);
        writeText(subjectInput, subject);
        writeText(messageTextarea, message);
    }

    public void uploadFile(String absolutePath) {
        getElement(uploadFileInput).sendKeys(absolutePath);
    }

    public void clickSubmit() {
        click(submitButton);
    }

    /** Akceptuje alert jeśli jest widoczny; jeśli nie ma alertu – kontynuuje (formularz może pokazać komunikat na stronie). */
    public void acceptAlertIfPresent() {
        try {
            driver.switchTo().alert().accept();
        } catch (Exception ignored) {
            // Brak alertu – komunikat sukcesu może być wyświetlony bezpośrednio na stronie
        }
    }

    public boolean isSuccessMessageVisible() {
        return isElementPresent(successMessage);
    }

    public void clickHome() {
        click(homeLink);
    }
}
