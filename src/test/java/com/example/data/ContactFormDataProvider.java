package com.example.data;

import org.testng.annotations.DataProvider;

public class ContactFormDataProvider {

    @DataProvider(name = "contactFormData")
    public static Object[][] contactFormData() {
        return new Object[][]{
                {"John Smith", "john@b2c2.com", "B2C2 Ltd.", "Trader", "Looking forward to onboarding."},
                {"Alice Novak", "alice@cryptox.com", "CryptoX", "Analyst", "Please contact me with API details."}
        };
    }
}
