package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for registration form – "Enter Account Information" section and address details.
 */
public class SignupPage extends BasePage {

    // ===== "Enter Account Information" section =====
    // <b>Enter Account Information</b>
    private final By enterAccountInformationSection =
            By.xpath("//*[contains(text(),'Enter Account Information')]");

    // Title: Mr / Mrs
    private final By titleMr = By.id("id_gender1");
    private final By titleMrs = By.id("id_gender2");

    // Form fields (data-qa)
    private final By passwordInput = By.cssSelector("[data-qa='password']");
    private final By daysSelect = By.cssSelector("[data-qa='days']");
    private final By monthsSelect = By.cssSelector("[data-qa='months']");
    private final By yearsSelect = By.cssSelector("[data-qa='years']");

    // Checkboxes
    private final By newsletterCheckbox = By.id("newsletter");
    private final By optinCheckbox = By.id("optin");

    // ===== Address fields (typical data-qa on automationexercise) =====
    private final By firstNameInput = By.cssSelector("[data-qa='first_name']");
    private final By lastNameInput = By.cssSelector("[data-qa='last_name']");
    private final By address1Input = By.cssSelector("[data-qa='address']");
    private final By countrySelect = By.cssSelector("[data-qa='country']");
    private final By stateInput = By.cssSelector("[data-qa='state']");
    private final By cityInput = By.cssSelector("[data-qa='city']");
    private final By zipcodeInput = By.cssSelector("[data-qa='zipcode']");
    private final By mobileNumberInput = By.cssSelector("[data-qa='mobile_number']");

    // Buttons
    private final By createAccountButton = By.cssSelector("[data-qa='create-account']");

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    // ===== Section verification =====

    public boolean isEnterAccountInformationSectionVisible() {
        return isElementPresent(enterAccountInformationSection);
    }

    // ===== Filling account data =====

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

    public void clickCreateAccount() {
        wait.until(ExpectedConditions.elementToBeClickable(createAccountButton));
        clickViaJavaScript(createAccountButton);
    }
}
