package com.example.hooks;

import com.example.utilities.ConfigReader;
import com.example.utilities.WebDriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;

public class Hooks {

    public static WebDriver driver;

    @Before
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

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}