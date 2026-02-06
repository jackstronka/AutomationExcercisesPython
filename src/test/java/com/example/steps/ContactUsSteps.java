package com.example.steps;

import com.example.context.ScenarioContext;
import com.example.hooks.Hooks;
import com.example.pages.ContactUsPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ContactUsSteps {

    @When("I fill in the contact form")
    public void iFillInTheContactForm(DataTable dataTable) {
        var row = dataTable.asMaps(String.class, String.class).get(0);
        ContactUsPage contactUsPage = ScenarioContext.get(ScenarioContext.CONTACT_US_PAGE);
        contactUsPage.fillContactForm(
                row.get("name"),
                row.get("email"),
                row.get("subject"),
                row.get("message")
        );
    }

    @When("I upload a file")
    public void iUploadAFile() {
        String uploadPath = getUploadFilePath();
        ContactUsPage contactUsPage = ScenarioContext.get(ScenarioContext.CONTACT_US_PAGE);
        contactUsPage.uploadFile(uploadPath);
    }

    private String getUploadFilePath() {
        URL resource = ContactUsSteps.class.getClassLoader().getResource("testdata/upload.txt");
        if (resource == null) {
            return Paths.get("src", "test", "resources", "testdata", "upload.txt")
                    .toAbsolutePath()
                    .toString();
        }
        try {
            return Path.of(resource.toURI()).toAbsolutePath().toString();
        } catch (Exception e) {
            return Paths.get("src", "test", "resources", "testdata", "upload.txt")
                    .toAbsolutePath()
                    .toString();
        }
    }
}
