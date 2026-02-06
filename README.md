## Automation Exercises Cucumber – E2E

Projekt automatyzacji testów UI dla [automationexercise.com](https://automationexercise.com) – **Selenium WebDriver**, **Cucumber** (BDD), **TestNG**.

---

## 📁 Struktura projektu

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
        │       ├── pages/
        │       │   ├── BasePage.java
        │       │   ├── HomePage.java
        │       │   ├── LoginPage.java
        │       │   ├── SignupPage.java
        │       │   ├── AccountCreatedPage.java
        │       │   └── ContactUsPage.java
        │       ├── hooks/
        │       │   └── Hooks.java
        │       ├── steps/
        │       │   ├── CommonSteps.java
        │       │   ├── RegistrationSteps.java
        │       │   ├── LoginSteps.java
        │       │   ├── AccountSteps.java
        │       │   └── ContactUsSteps.java
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
                └── TC06_ContactUsForm.feature
```

### Opis katalogów

- **pages** – Page Objects (BasePage + strony aplikacji)
- **hooks** – Cucumber hooks (`@Before`, `@After`, `@BeforeStep`) – setup przeglądarki, overlay cookies/reklam
- **steps** – definicje kroków Gherkin (`Given` / `When` / `Then`)
- **runner** – `CucumberTestRunner` uruchamiany przez profil Maven `cucumber`
- **utilities** – `WebDriverFactory`, `ConfigReader`
- **resources/config.properties** – konfiguracja środowiska
- **resources/features** – pliki `.feature` Cucumber

---

## ✅ Wymagania

- Java **17+** (projekt na **JDK 21**)
- Maven **3+**
- Chrome i/lub Firefox

---

## ⚙️ Konfiguracja – `config.properties`

### Kluczowe właściwości

```properties
baseUrl=https://automationexercise.com
browser=firefox
headless=false
windowWidth=1200
windowHeight=800
maximizeWindow=true
explicitWait=10
pageLoadTimeout=30
```

Wartości można nadpisać z linii poleceń przez `-D`:

```bash
mvn test -Pcucumber -Dbrowser=firefox -Dheadless=true
```

### Kolejność (`ConfigReader`)

1. System property (np. `-Dbrowser=firefox`)
2. `config.properties`

---

## ▶️ Uruchamianie testów

```bash
mvn test -Pcucumber
```

Z opcjami:

```bash
mvn test -Pcucumber -Dbrowser=chrome -Dheadless=false
```

### Raporty

- `target/cucumber-reports.html`
- `target/cucumber-report.json`

---

## 🧠 Działanie frameworka

### WebDriverFactory

- Odczytuje `browser`, `headless`, `maximizeWindow`, `windowWidth`, `windowHeight` z konfiguracji
- Tworzy WebDriver (Chrome / Firefox)
- Gdy `maximizeWindow=true`, pomija `setSize` (okno jest maksymalizowane w Hooks)

### ConfigReader

- Ładuje `config.properties` z classpath
- Metody: `get(key)`, `get(key, defaultValue)`
- Walidacja: `get(key)` rzuca wyjątek, gdy klucz brakuje lub wartość jest pusta

### Hooks

**@Before**
- Tworzy WebDriver (współdzielony między scenariuszami)
- Maksymalizuje okno (jeśli `maximizeWindow=true`)
- Otwiera `baseUrl`
- Zamyka overlay cookies, usuwa reklamy, czyści `#google_vignette`

**@After**
- Nie zamyka przeglądarki (współdzielona)
- Zamykanie w shutdown hook po zakończeniu wszystkich testów

**@BeforeStep**
- Usuwa overlaye reklam przed każdym krokiem

### BasePage

Wspólne metody: `click`, `clickViaJavaScript`, `writeText`, `readText`, `getElement`, `isElementPresent`.

### Feature files (Test Cases)

- **TC01** – Register User
- **TC02** – Login User (correct credentials)
- **TC03** – Login User (incorrect credentials)
- **TC04** – Logout User
- **TC05** – Register User with existing email
- **TC06** – Contact Us Form

---

## 🧩 Użycie jako szablon

1. Skopiuj projekt.
2. Zmień `baseUrl` w `config.properties`.
3. Dodaj Page Objects w `pages/`.
4. Dodaj pliki `.feature` w `resources/features/`.
5. Dodaj definicje kroków w `steps/`.
6. Opcjonalnie rozszerz Hooks (screenshots, logowanie).
