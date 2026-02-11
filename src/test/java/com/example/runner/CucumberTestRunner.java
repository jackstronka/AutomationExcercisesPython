package com.example.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        tags = "not @ignore",
        glue = {
                "com.example.steps",
                "com.example.hooks" // includes Hooks for setup/teardown
        },
        plugin = "pretty",
        monochrome = true
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
}
