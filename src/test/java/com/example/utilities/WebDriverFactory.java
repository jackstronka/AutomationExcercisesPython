package com.example.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.Dimension;
import java.time.Duration;


public class WebDriverFactory {

    // ⭐ METODA DOCELOWA – TEMPLATE
    public static WebDriver create() {
        String browser = ConfigReader.get("browser");
        boolean headless = Boolean.parseBoolean(ConfigReader.get("headless", "false"));
        boolean maximize = Boolean.parseBoolean(ConfigReader.get("maximizeWindow", "false"));
        int width = Integer.parseInt(ConfigReader.get("windowWidth", "1200"));
        int height = Integer.parseInt(ConfigReader.get("windowHeight", "800"));

        return create(browser, headless, maximize, width, height);
    }

    // ⭐ METODA TECHNICZNA
    private static WebDriver create(String browser, boolean headless, boolean maximize, int width, int height) {
        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless");
                }
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(Long.parseLong(ConfigReader.get("implicitWait", "0")))
        );
        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(Long.parseLong(ConfigReader.get("pageLoadTimeout", "30")))
        );

        if (!maximize) {
            driver.manage().window().setSize(new Dimension(width, height));
        }
        return driver;
    }
}