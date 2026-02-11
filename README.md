## Automation Exercises Cucumber – E2E

UI test automation project for [automationexercise.com](https://automationexercise.com) – **Selenium WebDriver**, **Cucumber** (BDD), **TestNG**.

---

## 📁 Project structure

```text
AutomationExcercisesCucumber/
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
headless=false
windowWidth=1200
windowHeight=800
maximizeWindow=true
implicitWait=0
explicitWait=10
pageLoadTimeout=30
orderSuccessWaitTimeout=15
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
mvn test -Pcucumber -Dcucumber.filter.tags="@tc01"
```

To run one tag while still excluding `@ignore` scenarios:

```bash
mvn test -Pcucumber -Dcucumber.filter.tags="not @ignore and @tc01"
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

- `target/cucumber-reports.html`
- `target/cucumber-report.json`

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
- Does not close browser (shared)
- Closing in shutdown hook after all tests complete

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
