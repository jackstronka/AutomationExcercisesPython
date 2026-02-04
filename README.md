## Automation Template – UI test starter

Template for automated UI tests for any website, based on **Selenium WebDriver**, **TestNG**, and **Cucumber** (with a TestNG runner).

---

## 📁 Project structure

```text
automation-template/
├── pom.xml
├── testng.xml
├── README.md
└── src/
    └── test/
        ├── java/
        │   └── com/example/
        │       ├── pages/
        │       │   ├── BasePage.java
        │       │   └── HomePage.java
        │       ├── hooks/
        │       │   └── Hooks.java
        │       ├── steps/
        │       │   └── SmokeSteps.java
        │       ├── runner/
        │       │   └── CucumberTestRunner.java
        │       ├── tests/
        │       │   └── SmokeTestNG.java
        │       └── utilities/
        │           ├── ConfigReader.java
        │           └── WebDriverFactory.java
        └── resources/
            ├── config.properties
            └── features/
                └── smoke.feature
```

### Directory overview

- **pages** – Page Objects (pages/application, shared logic in `BasePage`)
- **hooks** – Cucumber hooks (`@Before`, `@After`) executed before/after each scenario
- **steps** – Gherkin step definitions (`Given / When / Then`)
- **runner** – `CucumberTestRunner` for Maven profile `cucumber`
- **tests** – TestNG test classes (without Cucumber), e.g. `SmokeTestNG`
- **utilities** – shared utilities: `WebDriverFactory`, `ConfigReader`
- **resources/config.properties** – environment configuration
- **resources/features** – Cucumber `.feature` files

---

## ✅ Requirements

- Java **17+** (project configured for **JDK 21**)
- Maven **3+**
- Installed **Chrome** and/or **Firefox**

---

## ⚙️ Configuration – `config.properties`

### Key properties

#### Environment

```properties
env=local
```

#### Application

```properties
baseUrl=https://automationexercise.com
```

#### Browser

```properties
browser=chrome        # chrome / firefox
headless=false        # true / false
windowWidth=1200
windowHeight=800
```

#### Timeouts (seconds)

```properties
implicitWait=0
explicitWait=10
pageLoadTimeout=30
```

All values can be **overridden from the command line** using `-D`, for example:

```bash
mvn clean test -P cucumber -Dbrowser=firefox -Dheadless=true
```

### Configuration precedence (`ConfigReader`)

1. System property (e.g. `-Dbrowser=firefox`)
2. `config.properties`

---

## ▶️ How to run tests

### 1️⃣ TestNG (test classes)

Profile **`testng`** uses `testng.xml` and the class  
`com.example.tests.SmokeTestNG`.

#### Run smoke test:

```bash
mvn clean test -P testng -Dbrowser=chrome -Dheadless=false
```

#### `testng.xml`

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="TestNG Smoke Suite">
    <test name="SmokeTest">
        <classes>
            <class name="com.example.tests.SmokeTestNG"/>
        </classes>
    </test>
</suite>
```

In `SmokeTestNG`:
- WebDriver is created via `WebDriverFactory.create()`
- `baseUrl` is read from `ConfigReader`

---

### 2️⃣ Cucumber (BDD with TestNG)

Profile **`cucumber`** uses `CucumberTestRunner`.

- **features** – `src/test/resources/features`
- **glue** – `com.example.steps`, `com.example.hooks`
- **reports**:
  - `target/cucumber-reports.html`
  - `target/cucumber-report.json`

#### Run:

```bash
mvn clean test -P cucumber -Dbrowser=chrome -Dheadless=false
```

`CucumberTestRunner`:
- wires `.feature` scenarios with step definitions in `steps`
- automatically uses `Hooks` for browser setup/teardown

---

## 🧠 How the framework works

### WebDriverFactory

```java
public static WebDriver create()
```

- Reads `browser`, `headless`, `windowWidth`, `windowHeight`,
  `implicitWait`, `pageLoadTimeout` from `ConfigReader`
- Creates appropriate WebDriver (Chrome / Firefox)
- Supports headless mode
- Applies timeouts and window size

All configuration comes from `config.properties` or `-D` overrides.

---

### ConfigReader

- Loads `config.properties` from the classpath
- Methods:
  - `get(key)`
  - `get(key, defaultValue)`
– Priority: **system property → file**

---

### Hooks (Cucumber)

**@Before**
- creates WebDriver (`WebDriverFactory.create()`)
- optionally maximizes the window (based on configuration)
- opens `baseUrl`

**@After**
- closes the browser (`driver.quit()`)

---

### BasePage

Shared helper methods:
- `click`
- `writeText`
- `sendKeys`
- `readText`
- `getElement`
- `isElementPresent`
- getting page title and current URL

Uses `explicitWait` from configuration.

---

### HomePage

Example Page Object:
- `isDisplayed()` – checks that the main header is visible

---

### SmokeSteps

Example Cucumber steps:
- `Given user opens web page` – verifies that the driver exists (initialized in Hooks)
- `Then home page should be displayed` – uses `HomePage.isDisplayed()`

---

### `smoke.feature`

```gherkin
Feature: Smoke test

  Scenario: Open home page
    Given user opens web page
    Then home page should be displayed
```

---

## 🧩 How to use this project as a template

1. Clone this repository or copy the folder as a starting template.
2. Change `baseUrl` in `config.properties`.
3. Add your Page Objects in `src/test/java/com/example/pages/`.
4. Add feature files in `src/test/resources/features/`.
5. Add matching step definitions in `src/test/java/com/example/steps/`.
6. Optionally extend `Hooks` (screenshots, logging, reporting).

### Running tests

- **TestNG**
  ```bash
  mvn clean test -P testng
  ```

- **Cucumber (BDD)**
  ```bash
  mvn clean test -P cucumber
  ```

