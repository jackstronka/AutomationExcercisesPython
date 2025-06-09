# Automated Tests for B2C2

This repository contains automated UI tests for the B2C2 website using Selenium WebDriver, TestNG, and Cucumber.

---

## 📁 Project Structure

```
AutomatedTestsB2C2v1/
├── .gitignore
├── pom.xml
├── testng.xml
├── README.md
└── src/
    └── test/
        ├── java/
        │   └── com/
        │       └── example/
        │           ├── pages/
        │           │   ├── BasePage.java
        │           │   ├── ContactPage.java
        │           │   └── HomePage.java
        │           ├── runner/
        │           │   └── Runner.java
        │           ├── steps/
        │           │   ├── ContactFormSteps.java
        │           │   └── HomePageSteps.java
        │           └── tests/
        │               ├── ContactFormTest.java
        │               └── HomePageTest.java
        └── resources/
            └── features/
                ├── contact_form.feature
                └── home_page.feature
```

---

## 🧰 Requirements

- Java 8+ (tested with JDK 21)
- Maven 3+
- Chrome and/or Firefox browser installed

---

## 🚀 How to Run Tests

### 1. 🧪 Run TestNG Tests

#### ✅ With GUI (default):

```bash
mvn clean test -P testng -Dbrowser=chrome -Dheadless=false
```

#### 🧪 Headless Mode:

```bash
mvn clean test -P testng -Dbrowser=chrome -Dheadless=true
```

You can replace `chrome` with `firefox`.

---

### 2. 🥒 Run Cucumber Tests

#### ✅ With GUI:

```bash
mvn clean test -P cucumber -Dbrowser=chrome -Dheadless=false
```

#### 🧪 Headless Mode:

```bash
mvn clean test -P cucumber -Dbrowser=firefox -Dheadless=true
```

---

## 🧪 Included Tests

### Contact Form
- Form submission using different datasets.
- Run as:
  - TestNG test: `ContactFormTest.java`
  - Cucumber scenario: `contact_form.feature`

### Home Page
- Verifies title of the home page.
- Run as:
  - TestNG test: `HomePageTest.java`
  - Cucumber scenario: `home_page.feature`

---

## ⚠️ Known Limitations

- The contact form includes **reCAPTCHA**, which **blocks automated submission**.
- The test detects this and logs a warning:
  ```
  ⚠️ Submit blocked by reCAPTCHA.
  ```
- This is expected behavior and results in the test failing gracefully.

---

## 📄 Notes

- All tests run in 1280×1024 resolution by default.
- Cookies banner is accepted automatically if visible.
- Test data is defined inside `.feature` files and as TestNG DataProvider.
- The `headless` and `browser` options are read via `System.getProperty()` and can be passed as JVM arguments.

---

## 📦 Build and Dependencies

Dependencies are defined in `pom.xml`, including:
- Selenium
- WebDriverManager
- TestNG
- Cucumber (Java + TestNG)
- SLF4J for logging

Maven Profiles:
- `testng`: runs TestNG tests using `testng.xml`
- `cucumber`: runs feature files via `Runner.java`
