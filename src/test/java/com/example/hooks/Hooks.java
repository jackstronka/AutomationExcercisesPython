package com.example.hooks;

import com.example.context.ScenarioContext;
import com.example.utilities.ConfigReader;
import com.example.utilities.WebDriverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Hooks {

    private static final Logger log = LoggerFactory.getLogger(Hooks.class);

    public static WebDriver driver;

    private static boolean shutdownHookRegistered;

    @Before
    public void setUp() {
        ScenarioContext.clear();
        if (driver == null) {
            driver = WebDriverFactory.create();
            registerShutdownHook();

            boolean maximize = Boolean.parseBoolean(
                    ConfigReader.get("maximizeWindow", "false")
            );
            if (maximize) {
                driver.manage().window().maximize();
            }
        }

        driver.get(ConfigReader.get("baseUrl"));
        dismissCookieOverlay();
        dismissAdOverlays();
        removeGoogleVignetteFromUrl();
    }

    private static synchronized void registerShutdownHook() {
        if (!shutdownHookRegistered) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (driver != null) {
                    driver.quit();
                    driver = null;
                }
            }));
            shutdownHookRegistered = true;
        }
    }

    @BeforeStep
    public void beforeStep() {
        dismissCookieOverlay();
        dismissAdOverlays();
        removeGoogleVignetteFromUrl();
    }

    /**
     * Removes #google_vignette from URL (added automatically by Google ads).
     */
    private void removeGoogleVignetteFromUrl() {
        try {
            String url = driver.getCurrentUrl();
            if (url != null && url.contains("#google_vignette")) {
                ((JavascriptExecutor) driver).executeScript(
                        "window.history.replaceState(null, '', window.location.pathname + window.location.search);"
                );
            }
        } catch (Exception e) {
            log.debug("removeGoogleVignetteFromUrl: {}", e.getMessage());
        }
    }

    /**
     * Hides/removes ads – including Google Vignette ads, Google Ads iframes.
     */
    private void dismissAdOverlays() {
        try {
            // 1. Remove from DOM (most effective for Vignette)
            String removeScript = ""
                    + "document.querySelectorAll('ins.adsbygoogle, .adsbygoogle-noablate, "
                    + "iframe[id*=\"aswift\"], iframe[id*=\"google_ads_iframe\"], iframe[id*=\"ad_iframe\"]').forEach(function(e){ e.remove(); });";
            ((JavascriptExecutor) driver).executeScript(removeScript);

            // 2. Hide remaining (fallback)
            String hideScript = ""
                    + "document.querySelectorAll('iframe[title*=\"Advertisement\"], iframe[title*=\"Reklama\"], [id*=\"google_ads\"]').forEach(function(e){ "
                    + "e.style.setProperty('display','none','important'); e.style.setProperty('visibility','hidden','important'); });";
            ((JavascriptExecutor) driver).executeScript(hideScript);
        } catch (Exception e) {
            log.debug("dismissAdOverlays: {}", e.getMessage());
        }
    }

    /**
     * Dismisses cookie consent overlay (fc-dialog-overlay) that may block elements.
     */
    private void dismissCookieOverlay() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            // Wait for overlay (may not be visible on repeat visits)
            if (driver.findElements(By.cssSelector(".fc-dialog-overlay, .fc-consent")).isEmpty()) {
                return;
            }

            // Attempt 1: consent button (elementToBeClickable waits for animation)
            By[] acceptSelectors = {
                    By.cssSelector(".fc-cta-consent"),
                    By.cssSelector(".fc-consent .fc-primary-button"),
                    By.xpath("//button[contains(translate(., 'ACCEPT', 'accept'), 'accept')]"),
                    By.xpath("//a[contains(translate(., 'ACCEPT', 'accept'), 'accept')]")
            };
            for (By sel : acceptSelectors) {
                if (!driver.findElements(sel).isEmpty()) {
                    wait.until(ExpectedConditions.elementToBeClickable(sel)).click();
                    return;
                }
            }
            // Attempt 2: hide overlay via JavaScript (fallback)
            ((JavascriptExecutor) driver).executeScript(
                    "var el = document.querySelector('.fc-dialog-overlay'); if(el) el.style.display='none';"
            );
        } catch (Exception e) {
            log.debug("dismissCookieOverlay: {} (overlay may not be visible)", e.getMessage());
        }
    }

    @After
    public void tearDown() {
        // Do not close browser – shared between scenarios.
        // Closed in shutdown hook after all tests complete.
    }
}