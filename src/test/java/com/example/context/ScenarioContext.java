package com.example.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Współdzielony kontekst między klasami stepów w ramach jednego scenariusza.
 * Czyszczony przed każdym scenariuszem w Hooks.
 */
public final class ScenarioContext {

    private static final ThreadLocal<Map<String, Object>> DATA = ThreadLocal.withInitial(HashMap::new);

    public static final String HOME_PAGE = "homePage";
    public static final String LOGIN_PAGE = "loginPage";
    public static final String SIGNUP_PAGE = "signupPage";
    public static final String ACCOUNT_CREATED_PAGE = "accountCreatedPage";
    public static final String CONTACT_US_PAGE = "contactUsPage";
    public static final String LAST_ENTERED_NAME = "lastEnteredName";
    public static final String LAST_ENTERED_EMAIL = "lastEnteredEmail";
    public static final String REGISTERED_EMAIL = "registeredEmail";
    public static final String REGISTERED_PASSWORD = "registeredPassword";

    private ScenarioContext() {
    }

    public static void put(String key, Object value) {
        DATA.get().put(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return (T) DATA.get().get(key);
    }

    public static void clear() {
        DATA.get().clear();
    }
}
