## Automation Exercises Cucumber – E2E

UI test automation project for [automationexercise.com](https://automationexercise.com) – **Selenium WebDriver**, **Cucumber** (BDD), **TestNG**.

---

## 📁 Project structure

```text
AutomationExcercisesCucumber/
├── .github/
│   └── workflows/
│       └── ci.yml
├── pom.xml
├── README.md
└── src/
    └── test/
        ├── java/
        │   └── com/example/
        │       ├── context/
        │       │   └── ScenarioContext.java
        │       ├── hooks/
        │       │   └── Hooks.java
        │       ├── pages/
        │       │   ├── BasePage.java
        │       │   ├── HomePage.java
        │       │   ├── LoginPage.java
        │       │   ├── SignupPage.java
        │       │   ├── AccountCreatedPage.java
        │       │   ├── ContactUsPage.java
        │       │   ├── ProductsPage.java
        │       │   ├── ProductDetailPage.java
        │       │   ├── CartPage.java
        │       │   ├── CheckoutPage.java
        │       │   └── OrderSuccessPage.java
        │       ├── steps/
        │       │   ├── CommonSteps.java
        │       │   ├── RegistrationSteps.java
        │       │   ├── LoginSteps.java
        │       │   ├── AccountSteps.java
        │       │   ├── ContactUsSteps.java
        │       │   ├── ProductsSteps.java
        │       │   ├── ProductQuantitySteps.java
        │       │   ├── SearchProductSteps.java
        │       │   └── CheckoutSteps.java
        │       ├── runner/
        │       │   └── CucumberTestRunner.java
        │       └── utilities/
        │           ├── ConfigReader.java
        │           └── WebDriverFactory.java
        └── resources/
            ├── config.properties
            ├── testdata/
            │   └── upload.txt
            └── features/
                ├── TC01_RegisterUser.feature
                ├── TC02_LoginUser.feature
                ├── TC03_LoginUserIncorrect.feature
                ├── TC04_LogoutUser.feature
                ├── TC05_RegisterUserExistingEmail.feature
                ├── TC06_ContactUsForm.feature
                ├── TC07_VerifyAllProducts.feature
                ├── TC08_SearchProduct.feature
                ├── TC09_VerifyProductQuantityInCart.feature
                ├── TC10_PlaceOrderRegisterWhileCheckout.feature
                └── TC11_DownloadInvoiceAfterPurchase.feature
```

### Directory description

- **context** – `ScenarioContext` – shared state between step classes within a scenario
- **pages** – Page Objects (BasePage + application pages)
- **hooks** – Cucumber hooks (`@Before`, `@After`, `@BeforeStep`) – browser setup, cookie/ad overlays
- **steps** – Gherkin step definitions (`Given` / `When` / `Then`)
- **runner** – `CucumberTestRunner` executed via Maven profile `cucumber`
- **utilities** – `WebDriverFactory`, `ConfigReader`
- **resources/config.properties** – environment configuration
- **resources/features** – Cucumber `.feature` files
- **.github/workflows** – GitHub Actions CI (runs tests on push/PR)

---

## ✅ Requirements

- Java **17+** (project uses **JDK 21**)
- Maven **3+**
- Chrome and/or Firefox

---

## ⚙️ Configuration – `config.properties`

### Key properties

```properties
baseUrl=https://automationexercise.com
browser=chrome
headless=true
windowWidth=1200
windowHeight=800
maximizeWindow=true
implicitWait=0
explicitWait=10
pageLoadTimeout=30
orderSuccessWaitTimeout=15
accountDeletedWaitTimeout=15
alertWaitTimeout=2
```

Values can be overridden from command line via `-D`:

```bash
mvn test -Pcucumber -Dbrowser=firefox -Dheadless=true
```

### Priority order (`ConfigReader`)

1. System property (e.g. `-Dbrowser=firefox`)
2. `config.properties`

---

## ▶️ Running tests

### All tests

```bash
mvn test -Pcucumber
```

With options:

```bash
mvn test -Pcucumber -Dbrowser=chrome -Dheadless=false
```

### Running individual tests

Each test scenario (TC) has its own tag in format `@tcXX` (e.g. `@tc01`, `@tc02`, ..., `@tc11`), allowing easy single-test execution.

**1. By feature file** – run only a selected `.feature` file:

```bash
mvn test -Pcucumber -Dcucumber.features="src/test/resources/features/TC01_RegisterUser.feature"
```

Other file examples:

```bash
mvn test -Pcucumber -Dcucumber.features="src/test/resources/features/TC02_LoginUser.feature"
mvn test -Pcucumber -Dcucumber.features="src/test/resources/features/TC10_PlaceOrderRegisterWhileCheckout.feature"
```

**2. By tag** – run only scenarios with a given TC tag (e.g. `@tc01`, `@tc10`):

```bash
mvn test -Pcucumber "-Dcucumber.filter.tags=@tc01"
```

To run one tag while still excluding `@ignore` scenarios:

```bash
mvn test -Pcucumber "-Dcucumber.filter.tags=not @ignore and @tc01"
```

**3. From IDE (IntelliJ / VS Code)**  
- Right-click the `.feature` file → **Run Feature** (entire file)  
- Or in a specific scenario → **Run Scenario** (only that scenario)

### Test suites by tags

- **Smoke tests** – quick, critical suite:

```bash
mvn test -Pcucumber "-Dcucumber.filter.tags=@smoke"
```

- **Full regression** – all regression tests (excluding `@ignore`):

```bash
mvn test -Pcucumber "-Dcucumber.filter.tags=@regression and not @ignore"
```

- **Functional areas** – e.g. checkout only:

```bash
mvn test -Pcucumber "-Dcucumber.filter.tags=@checkout and not @ignore"
```

### Reports

- `target/cucumber-reports.html` – Cucumber HTML report
- `target/cucumber-report.json` – Cucumber JSON (for integrations)
- `target/allure-report/` – **Allure** report (interactive, with trends and screenshots on failure)

**Generate Allure report locally:**
```bash
mvn test -Pcucumber
mvn allure:report -Pcucumber
# Open target/allure-report/index.html in browser
```

**Or serve interactively:**
```bash
mvn allure:serve -Pcucumber
# Runs tests, generates report, opens in browser
```

---

## 🚀 GitHub Actions CI

Tests run automatically on push and pull requests to `main`:

| Event | What runs |
|-------|-----------|
| **push** to `main` | **Full regression** |
| **pull_request** to `main` | **Smoke tests only** (`@smoke`) |
| **schedule** (cron) | **Full regression** – Monday 9:00 UTC |

- **Job:** `test` – checkout, JDK 21, Chrome (`browser-actions/setup-chrome`), Cucumber (headless)
- **Concurrency:** new run on the same branch/PR cancels the previous one (`cancel-in-progress`)
- **Timeout:** 30 minutes
- **Artifacts:** `surefire-reports`, `cucumber-reports.html`, `cucumber-report.json`, `allure-report/` – available for download after each run
- **GitHub Pages:** The Allure report is deployed to GitHub Pages on **push to main** and on **schedule** (weekly run). See below.

Workflow file: `.github/workflows/ci.yml`

### Viewing Allure report on GitHub Pages

The Allure report is published automatically on each push to `main` and on the weekly schedule. Enable it once:

1. Repo **Settings** → **Pages**
2. Under **Build and deployment**, set **Source** to **GitHub Actions**
3. After the next push to `main` or scheduled run, the report will be available at:

   **https://jackstronka.github.io/AutomationExcercisesCucumber/**

Status badge (optional; replace `jackstronka` with your GitHub username):

```markdown
[![CI](https://github.com/jackstronka/AutomationExcercisesCucumber/actions/workflows/ci.yml/badge.svg)](https://github.com/jackstronka/AutomationExcercisesCucumber/actions)
```

---

## 📊 Allure

- **Screenshot on failure** – `Hooks.@After` attaches a screenshot to Allure when a scenario fails (visible in **Tear Down** section of the failed test)
- **@Step on Page Objects** – public Page Object methods are annotated with `@Step` for detailed step hierarchy in Allure (requires AspectJ agent in Surefire)
- **Plugin:** `io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm` in `CucumberTestRunner`
- **Results:** `target/allure-results` (raw)

**Commands:**
- `mvn allure:report -Pcucumber` – generate report to `target/allure-report/` (run after `mvn test`)
- `mvn allure:serve -Pcucumber` – run tests, generate report, open in browser (all-in-one)

**GitHub Pages:** CI deploys the Allure report on push to `main` and on schedule. Report URL: **https://jackstronka.github.io/AutomationExcercisesCucumber/** (enable **Settings → Pages → Source: GitHub Actions** once).

---

## 🧠 Framework architecture

### WebDriverFactory

- Reads `browser`, `headless`, `maximizeWindow`, `windowWidth`, `windowHeight` from config
- Creates WebDriver (Chrome / Firefox)
- When `maximizeWindow=true`, skips `setSize` (window is maximized in Hooks)

### ConfigReader

- Loads `config.properties` from classpath
- Methods: `get(key)`, `get(key, defaultValue)`
- Validation: `get(key)` throws when key is missing or value is empty

### Hooks

**@Before**
- Creates WebDriver (shared between scenarios)
- Maximizes window (if `maximizeWindow=true`)
- Opens `baseUrl`
- Dismisses cookie overlay, removes ads, clears `#google_vignette`

**@After**
- If scenario failed – attaches screenshot to Allure
- Does not close browser (shared); closing in shutdown hook after all tests complete

**@BeforeStep**
- Removes ad overlays before each step

### BasePage

Shared methods: `click`, `clickViaJavaScript`, `writeText`, `readText`, `getElement`, `isElementPresent`, `selectByValueViaJavaScript`, `selectByVisibleTextViaJavaScript`.

### Feature files (Test Cases)

| TC    | Tag    | Description |
|-------|--------|-------------|
| **TC01** | `@tc01` | Register User |
| **TC02** | `@tc02` | Login User (correct credentials) |
| **TC03** | `@tc03` | Login User (incorrect credentials) |
| **TC04** | `@tc04` | Logout User |
| **TC05** | `@tc05` | Register User with existing email |
| **TC06** | `@tc06` | Contact Us Form |
| **TC07** | `@tc07` | Verify All Products and product detail page |
| **TC08** | `@tc08` | Search Product |
| **TC09** | `@tc09` | Verify Product quantity in Cart |
| **TC10** | `@tc10` | Place Order: Register while Checkout |
| **TC11** | `@tc11` | Download Invoice after purchase order |

Scenarios with `@ignore` tag are skipped on default run (`tags = "not @ignore"`). Use `@tcXX` tags to run individual TCs, e.g.:

```bash
mvn test -Pcucumber -Dcucumber.filter.tags="@tc07"
```

---
