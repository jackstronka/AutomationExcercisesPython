package com.example.tests;

import com.example.utilities.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.time.Duration;

public class BaseTests {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(BaseTests.class);

    @BeforeMethod(description = "Method Level Setup!")
    @Parameters({"browser", "headless"})
    public void setup(String browser, String headless) {
        logger.info("🔧 Test setup starting for browser: {}, headless: {}", browser, headless);

        driver = WebDriverFactory.create(browser, Boolean.parseBoolean(headless));

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();

        logger.info("✅ WebDriver setup completed.");
    }

    @AfterMethod(description = "Method Level Teardown!")
    public void teardown() {
        logger.info("🛑 Executing teardown phase.");
        if (driver != null) {
            driver.quit();
            logger.info("✅ WebDriver closed.");
        }
    }
}
