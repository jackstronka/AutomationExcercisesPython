package com.example.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "com.example.steps",
                "com.example.hooks" // includes Hooks for setup/teardown
        },
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber-report.json"
        },
        monochrome = true
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
}
