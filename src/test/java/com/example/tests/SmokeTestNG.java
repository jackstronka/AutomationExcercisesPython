package com.example.tests;

import com.example.utilities.ConfigReader;
import com.example.utilities.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SmokeTestNG {

    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
    driver = WebDriverFactory.create();

    boolean maximize = Boolean.parseBoolean(
            ConfigReader.get("maximizeWindow", "false")
    );
    if (maximize) {
        driver.manage().window().maximize();
    }

    driver.get(ConfigReader.get("baseUrl"));
}

    @Test
    public void shouldOpenHomePage() {
        String title = driver.getTitle();
        Assert.assertFalse(title.isEmpty(), "Page title should not be empty");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
