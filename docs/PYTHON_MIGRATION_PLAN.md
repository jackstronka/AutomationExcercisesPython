# Python Migration Plan

This document describes how to migrate the current **Java + Selenium + TestNG** E2E project to **Python + Selenium + pytest**, preserving the same structure and patterns where applicable.

---

## 1. Is it possible?

**Yes.** The project uses standard patterns (Page Object Model, config-driven WebDriver, explicit waits, overlays handling) that map directly to Python and pytest. Selenium WebDriver API is nearly identical in Python.

---

## 2. Technology mapping

| Java (current)              | Python equivalent                          |
|----------------------------|--------------------------------------------|
| Maven                      | `pip` / `poetry` / `uv` + `requirements.txt` or `pyproject.toml` |
| TestNG                     | **pytest** (groups → markers, suites → `pytest.ini` / `conftest.py`) |
| Selenium Java              | **selenium** (PyPI)                        |
| WebDriverManager           | **webdriver-manager** (PyPI)               |
| Allure TestNG              | **allure-pytest**                          |
| config.properties          | **config.ini** or **.env** or **config.yaml** + small loader |
| SLF4J                      | **logging** (stdlib) or **loguru**          |

---

## 3. Suggested Python project structure

Mirror the current layout so that rules (Page Objects, tests, testdata, utilities, config) stay consistent:

```text
AutomationExercisesPython/
├── .github/workflows/ci.yml
├── pyproject.toml          # or requirements.txt
├── pytest.ini              # markers, options (replaces testng.xml)
├── conftest.py             # fixtures: driver, base_url, overlay dismissal, screenshot on failure
├── config.ini             # same keys as config.properties
├── README.md
├── docs/
└── src/
    └── test/
        ├── pages/
        │   ├── base_page.py
        │   ├── home_page.py
        │   ├── login_page.py
        │   └── ...         # same pages as in Java
        ├── testdata/
        │   ├── __init__.py
        │   ├── models.py   # dataclasses / NamedTuple for AccountInfo, Address, etc.
        │   └── test_data_factory.py
        ├── tests/
        │   ├── base_test.py      # shared helpers (ensure_registered_user_ready_for_login, etc.)
        │   ├── test_tc01_register_user.py
        │   ├── test_tc02_login_user.py
        │   └── ...
        └── utilities/
            ├── config_reader.py
            ├── overlay_helper.py
            └── web_driver_factory.py
```

---

## 4. Implementation outline

### 4.1 Configuration

- **Single source of truth:** all parameters (`browser`, `headless`, `windowWidth`, `windowHeight`, `maximizeWindow`, `implicitWait`, `explicitWait`, `pageLoadTimeout`, `baseUrl`) in `config.ini` (or `.env`).
- Override via environment variables (e.g. `BROWSER=firefox`) or pytest `-D`-style options if you add a small CLI.
- **utilities/config_reader.py:** read config and expose `get(key, default)`.

### 4.2 BasePage (Python)

- Accept `driver` and `timeout` (from config) in `__init__`.
- Create a single `WebDriverWait(driver, timeout)`.
- Methods: `click(locator)`, `click_via_javascript(locator)`, `write_text(locator, text)`, `read_text(locator)`, `is_element_present(locator)`, `get_element(locator)`, `scroll_into_view(locator)`, `select_by_value_via_javascript(locator, value)`, etc.
- Use `expected_conditions` from `selenium.webdriver.support.ui` (e.g. `element_to_be_clickable`, `visibility_of_element_located`) — same logic as Java.
- Locators: use `By` from `selenium.webdriver.common.by` (e.g. `By.ID`, `By.CSS_SELECTOR`, `By.XPATH`).

### 4.3 Page Objects

- One module per page (e.g. `login_page.py`). Class holding `driver` and locators (as class or instance attributes).
- Only UI interaction; no assertions. Same locator priority: ID → name → data attributes → CSS → XPath for text.

### 4.4 WebDriver lifecycle and fixtures (conftest.py)

- **Session-scoped driver:** create once per test run (or per worker if using pytest-xdist), read config, create via **webdriver-manager** + Selenium options (e.g. Chrome headless, window size).
- **Function-scoped setup:** before each test, `driver.get(base_url)` and call `OverlayHelper.dismiss(driver)`.
- **Teardown:** on failure, attach screenshot to Allure (`allure.attach`); after session, `driver.quit()`.
- Expose `driver` (and optionally `base_url`) as pytest fixtures so tests and `BaseTest`-style helpers receive them.

### 4.5 BaseTest-style helpers

- In `tests/base_test.py` (or a shared module), define functions that take `driver` (and config if needed): e.g. `ensure_registered_user_ready_for_login(driver) -> tuple[str, str]` returning `(email, password)`.
- Reuse the same flows: registration, logout, checkout steps, etc., so tests stay semantic and DRY.

### 4.6 Tests (pytest)

- One file per TC (e.g. `test_tc02_login_user.py`).
- Use **pytest markers** instead of TestNG groups: e.g. `@pytest.mark.tc02`, `@pytest.mark.regression`, `@pytest.mark.auth`, `@pytest.mark.smoke`.
- Test functions with clear names: e.g. `def test_login_user_with_correct_credentials(driver, base_url): ...`.
- Assertions in test code only; use `assert condition, "message"` or `pytest.assert`-style messages (always with a clear description).

### 4.7 Test data

- **dataclasses** or **NamedTuples** for `AccountInfo`, `Address`, `ContactFormData`, `PaymentDetails`, `SignupUser`.
- **test_data_factory.py:** default instances and helpers to build unique emails (e.g. `generate_unique_email(base)`), same logic as Java `TestDataFactory`.

### 4.8 Overlays and waits

- **utilities/overlay_helper.py:** port `OverlayHelper.dismiss(driver)` (cookie consent, ad overlays) to Python; call from fixture after `driver.get(base_url)`.
- No `time.sleep` as primary sync; use `WebDriverWait` + `expected_conditions` everywhere. Optional short `time.sleep` only for temporary debugging, with a comment.

### 4.9 CI (GitHub Actions)

- Replace Maven with: install Python, create venv, `pip install -r requirements.txt`, run `pytest` with the desired markers/suite.
- Use **allure-pytest** and upload Allure results as artifacts; same reporting idea as current workflow.
- Keep **concurrency** and **English** comments in CI.

---

## 5. Rules compliance (Python version)

When porting, keep the same principles:

- **Structure:** pages / tests / testdata / utilities / single config file.
- **Config:** one place for all parameters; override via env (or CLI).
- **Page Objects:** UI only; locators and waits; prefer ID/name/data-qa/CSS, XPath for text.
- **Semantic tests:** clear test names and shared flow helpers; no giant branching tests.
- **Reusability:** shared helpers for registration, checkout, etc.
- **Markers:** tc01…tc11, smoke, regression, auth, products, checkout, contact.
- **Assertions:** in tests only; always with a message.
- **No Thread.sleep:** use explicit waits (WebDriverWait + expected_conditions).
- **Overlays:** scroll_into_view + click_via_javascript when needed; document in comments.
- **README and docs:** English; update README when structure or execution changes.
- **CI:** update README when workflow changes.

---

## 6. Suggested order of migration

1. Create Python project (pyproject.toml or requirements.txt), install selenium, webdriver-manager, pytest, allure-pytest.
2. Implement **config** and **utilities** (config_reader, web_driver_factory, overlay_helper).
3. Implement **BasePage** and one **Page Object** (e.g. HomePage, LoginPage).
4. Add **conftest.py** (driver fixture, base_url, overlay dismissal, screenshot on failure).
5. Port **testdata** (models + test_data_factory).
6. Port **BaseTest** helpers (ensure_registered_user_ready_for_login, etc.).
7. Port tests one by one (e.g. TC01, TC02), adding markers and assertions.
8. Add remaining Page Objects as needed.
9. Add pytest.ini (markers, optional suite selection) and CI workflow.
10. Update README.md (structure, config, TC list, how to run tests and generate Allure report).

---

## 7. Example dependencies (requirements.txt)

```text
selenium>=4.30.0
webdriver-manager>=4.0.0
pytest>=8.0.0
allure-pytest>=2.13.0
```

Optional: `python-dotenv` if using `.env` for overrides.

---

This plan allows a step-by-step migration without rewriting everything at once; you can keep the Java project in parallel until the Python suite is complete.
