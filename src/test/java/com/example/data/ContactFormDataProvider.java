package com.example.data;

import org.testng.annotations.DataProvider;

/**
 * Template DataProvider to reuse across different tests.
 * 1) Rename this class to your own name (e.g. LoginDataProvider).
 * 2) Change provider name "NAME_TUTAJ" to a concrete one (e.g. "loginData").
 * 3) Fill the array with your test data.
 */
public class ContactFormDataProvider {

    @DataProvider(name = "NAME_TUTAJ")
    public static Object[][] someData() {
        return new Object[][]{
                // column1, column2, ...
                { /* TODO: put your data here */ },
                { /* TODO: put your data here */ }
        };
    }
}