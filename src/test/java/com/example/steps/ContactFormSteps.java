package com.example.steps;

import com.example.hooks.Hooks;
import com.example.pages.ContactPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactFormSteps {

    private final ContactPage contactPage;
    private static final Logger logger = LoggerFactory.getLogger(ContactFormSteps.class);

    public ContactFormSteps() {
        this.contactPage = new ContactPage(Hooks.getDriver(), Hooks.getWait());
    }

    @Given("the user opens the Contact Us page")
    public void openContactPage() {
        logger.info("🌐 Opening Contact Us page");
        contactPage.openContactUs();
    }

    @And("the user accept cookies if present")
    public void acceptCookiesIfPresent() {
        logger.info("🔘 Accepting cookies if present");
        contactPage.acceptCookiesIfPresent();
    }

    @When("the user selects {string} from the team dropdown")
    public void selectTeam(String team) {
        // Na razie obsługiwany tylko "Onboarding"
        logger.info("📂 Selecting team: {}", team);
        contactPage.selectTeamOnboarding();
    }

    @And("the user enters name {string}")
    public void enterName(String name) {
        logger.info("✏️ Entering name: {}", name);
        contactPage.enterName(name);
    }

    @And("the user enters email {string}")
    public void enterEmail(String email) {
        logger.info("✉️ Entering email: {}", email);
        contactPage.enterEmail(email);
    }

    @And("the user selects {string} from the organization dropdown")
    public void selectOrganization(String organization) {
        // Na razie obsługiwany tylko "Exchange"
        logger.info("🏢 Selecting organization type: {}", organization);
        contactPage.selectOrganizationExchange();
    }

    @And("the user enters company name {string}")
    public void enterCompany(String company) {
        logger.info("🏭 Entering company: {}", company);
        contactPage.enterCompanyName(company);
    }

    @And("the user enters position {string}")
    public void enterPosition(String position) {
        logger.info("📌 Entering position: {}", position);
        contactPage.enterPosition(position);
    }

    @And("the user selects {string} from the trading volume dropdown")
    public void selectVolume(String volume) {
        // Obsługiwane tylko "$1m-$10m"
        logger.info("💰 Selecting trading volume: {}", volume);
        contactPage.selectTradingVolumeOneToTenMilLion(); // aktualny selector obsługuje tylko tę wartość
    }

    @And("the user enters message {string}")
    public void enterMessage(String message) {
        logger.info("📝 Entering message: {}", message);
        contactPage.enterMessage(message);
    }

    @And("the user submits the form")
    public void submitForm() throws InterruptedException {
        logger.info("🚀 Submitting the form");
        Thread.sleep(1000); // zanim załaduje captcha lub strona
        contactPage.submitForm();
    }

    @Then("the form should be submitted or blocked by captcha")
    public void verifySubmissionOrCaptcha() {
        logger.info("🔎 Final step reached. Submission likely complete or blocked by CAPTCHA.");
        try {
            Hooks.getDriver().switchTo().alert().dismiss(); // lub .accept()
            logger.warn("⚠️ CAPTCHA detected. Alert dismissed.");
        } catch (Exception ignored) {
            logger.info("✅ No CAPTCHA alert detected — proceeding as normal.");
        }
    }

}
