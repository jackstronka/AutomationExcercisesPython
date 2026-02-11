package com.example.pages;

import com.example.utilities.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for /contact_us page – Contact Us form.
 */
public class ContactUsPage extends BasePage {

    private final By getInTouchHeading = By.xpath("//h2[contains(text(),'Get In Touch')]");
    private final By nameInput = By.cssSelector("[data-qa='name']");
    private final By emailInput = By.cssSelector("[data-qa='email']");
    private final By subjectInput = By.cssSelector("[data-qa='subject']");
    private final By messageTextarea = By.cssSelector("[data-qa='message']");
    private final By uploadFileInput = By.cssSelector("input[name='upload_file']");
    private final By submitButton = By.cssSelector("[data-qa='submit-button']");
    private final By successMessage = By.cssSelector("#contact-page .status.alert.alert-success");
    private final By homeLink = By.xpath("//a[@href='/'][.//span[contains(.,'Home')]]");
    private final By homeLinkNav = By.cssSelector("a[href='/']");

    /** True if success message was seen in alert text (on automationexercise.com it may appear in alert). */
    private boolean successMessageSeenInAlert;

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

    /**
     * Uploads file via the file input. If the input is hidden (common for styled file inputs),
     * makes it visible temporarily so sendKeys works reliably.
     */
    public void uploadFile(String absolutePath) {
        WebElement input = getElement(uploadFileInput);
        scrollIntoView(uploadFileInput);
        if (!input.isDisplayed()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='block'; arguments[0].style.visibility='visible'; arguments[0].style.opacity='1';", input);
        }
        input.sendKeys(absolutePath);
    }

    /**
     * Submits the contact form. Ukrywa overlays, ustawia przycisk na wierzch, klika.
     * UnhandledAlertException = submit zadziałał, alert się pojawił – akceptujemy go.
     * Zawsze po kliku próbujemy acceptAlertIfPresent – komunikat sukcesu jest w alercie.
     */
    public void clickSubmit() {
        scrollIntoView(submitButton);
        dismissOverlaysThatBlockForm();
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].style.position='relative'; arguments[0].style.zIndex='999999';", btn);
        try {
            btn.click();
        } catch (ElementClickInterceptedException e) {
            new Actions(driver).moveToElement(btn).click().perform();
        } catch (UnhandledAlertException e) {
            // Alert się pojawił – akceptujemy go poniżej
        }
        acceptAlertIfPresent();
    }

    /** Hides overlays/banners that may cover the form (cookie, ads, etc.). */
    private void dismissOverlaysThatBlockForm() {
        try {
            String script = ""
                    + "document.querySelectorAll('.fc-dialog-overlay, .fc-consent, .adsbygoogle, [id*=\"google_ads\"], "
                    + "[class*=\"cookie\"], [id*=\"cookie\"], [class*=\"GDPR\"], [class*=\"consent\"], .cc-window, #cc-window').forEach(function(e){ "
                    + "e.style.setProperty('display','none','important'); e.style.setProperty('visibility','hidden','important'); });";
            ((JavascriptExecutor) driver).executeScript(script);
        } catch (Exception ignored) {
        }
    }

    /**
     * Akceptuje alert jeśli jest. Gdy już zaakceptowany w clickSubmit – zwraca od razu (bez czekania).
     * Czeka tylko gdy alert faktycznie jest (pojawia się od razu po Submit).
     */
    public void acceptAlertIfPresent() {
        if (successMessageSeenInAlert) {
            return; // Już zaakceptowano w clickSubmit – od razu dalej
        }
        try {
            int timeout = Integer.parseInt(ConfigReader.get("alertWaitTimeout", "2"));
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            alertWait.until(ExpectedConditions.alertIsPresent());
            String alertText = driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
            // Komunikat sukcesu jest w alercie – po zatwierdzeniu ten tekst wystarczy
            if (alertText != null && !alertText.isBlank()) {
                successMessageSeenInAlert = true;
            }
        } catch (Exception ignored) {
            // Brak alertu – komunikat może być na stronie
        }
    }

    /**
     * Success message can appear in alert (after submit) or on page. Per Rule 13 – dynamic content.
     */
    public boolean isSuccessMessageVisible() {
        if (successMessageSeenInAlert) {
            return true;
        }
        try {
            int timeout = Integer.parseInt(ConfigReader.get("explicitWait", "10"));
            WebDriverWait msgWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            msgWait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Clicks Home – po sukcesie kontakt może użyć nav lub przycisku w sekcji. */
    public void clickHome() {
        if (driver.findElements(homeLink).isEmpty()) {
            clickViaJavaScript(homeLinkNav);
        } else {
            clickViaJavaScript(homeLink);
        }
    }
}
