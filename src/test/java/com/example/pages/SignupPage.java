package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object dla formularza rejestracji – sekcja "Enter Account Information" i dane adresowe.
 */
public class SignupPage extends BasePage {

    // ===== Sekcja "Enter Account Information" =====
    // <b>Enter Account Information</b>
    private final By enterAccountInformationSection =
            By.xpath("//*[contains(text(),'Enter Account Information')]");

    // Tytuł: Mr / Mrs
    private final By titleMr = By.id("id_gender1");
    private final By titleMrs = By.id("id_gender2");

    // Pola formularza (data-qa)
    private final By passwordInput = By.cssSelector("[data-qa='password']");
    private final By daysSelect = By.cssSelector("[data-qa='days']");
    private final By monthsSelect = By.cssSelector("[data-qa='months']");
    private final By yearsSelect = By.cssSelector("[data-qa='years']");

    // Checkboxy
    private final By newsletterCheckbox = By.id("newsletter");
    private final By optinCheckbox = By.id("optin");

    // ===== Dane adresowe (typowe data-qa na automationexercise) =====
    private final By firstNameInput = By.cssSelector("[data-qa='first_name']");
    private final By lastNameInput = By.cssSelector("[data-qa='last_name']");
    private final By address1Input = By.cssSelector("[data-qa='address']");
    private final By countrySelect = By.cssSelector("[data-qa='country']");
    private final By stateInput = By.cssSelector("[data-qa='state']");
    private final By cityInput = By.cssSelector("[data-qa='city']");
    private final By zipcodeInput = By.cssSelector("[data-qa='zipcode']");
    private final By mobileNumberInput = By.cssSelector("[data-qa='mobile_number']");

    // Przyciski
    private final By createAccountButton = By.cssSelector("[data-qa='create-account']");

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    // ===== Weryfikacja sekcji =====

    public boolean isEnterAccountInformationSectionVisible() {
        return isElementPresent(enterAccountInformationSection);
    }

    // ===== Wypełnianie danych konta =====

    public void fillAccountInformation(String password, String title, String day, String month, String year) {
        if ("Mrs".equalsIgnoreCase(title)) {
            click(titleMrs);
        } else {
            click(titleMr);
        }
        writeText(passwordInput, password);
        selectByValue(daysSelect, day);
        selectByValue(monthsSelect, month);
        selectByValue(yearsSelect, year);
    }

    public void subscribeToNewsletter() {
        if (!isCheckboxSelected(newsletterCheckbox)) {
            clickViaJavaScript(newsletterCheckbox);
        }
    }

    public void agreeToSpecialOffers() {
        if (!isCheckboxSelected(optinCheckbox)) {
            clickViaJavaScript(optinCheckbox);
        }
    }

    // ===== Dane adresowe =====

    public void fillAddressDetails(
            String firstName,
            String lastName,
            String address1,
            String country,
            String state,
            String city,
            String zipcode,
            String mobileNumber
    ) {
        writeText(firstNameInput, firstName);
        writeText(lastNameInput, lastName);
        writeText(address1Input, address1);
        selectByVisibleText(countrySelect, country);
        writeText(stateInput, state);
        writeText(cityInput, city);
        writeText(zipcodeInput, zipcode);
        writeText(mobileNumberInput, mobileNumber);
    }

    // ===== Akcje =====

    public void clickCreateAccount() {
        wait.until(ExpectedConditions.elementToBeClickable(createAccountButton));
        clickViaJavaScript(createAccountButton);
    }

    // ===== Pomocnicze =====

    private void selectByValue(By locator, String value) {
        if (value == null || value.isEmpty()) return;
        selectByValueViaJavaScript(locator, value);
    }

    private void selectByVisibleText(By locator, String text) {
        if (text == null || text.isEmpty()) return;
        selectByVisibleTextViaJavaScript(locator, text);
    }

    private boolean isCheckboxSelected(By locator) {
        return isElementPresent(locator) && getElement(locator).isSelected();
    }
}
