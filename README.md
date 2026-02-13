## Automation Exercises – E2E (Python)

UI test automation project for [automationexercise.com](https://automationexercise.com) – **Selenium WebDriver**, **pytest**. Test cases TC01–TC11 with Page Object Model.

---

## Project structure

```text
AutomationExcercisesPython/
├── .github/workflows/ci.yml
├── config.ini
├── conftest.py
├── pytest.ini
├── requirements.txt
├── run_pytest.bat
├── README.md
├── docs/
│   ├── PYTHON_MIGRATION_PLAN.md
│   └── SELENIUM_TEST_PATTERN_ANALYSIS.md
├── pages/
│   ├── base_page.py
│   ├── home_page.py
│   ├── login_page.py
│   ├── signup_page.py
│   ├── account_created_page.py
│   ├── contact_us_page.py
│   ├── products_page.py
│   ├── product_detail_page.py
│   ├── cart_page.py
│   ├── checkout_page.py
│   └── order_success_page.py
├── testdata/
│   ├── models.py
│   └── test_data_factory.py
├── tests/
│   ├── base_test.py
│   ├── test_tc01_register_user.py
│   ├── test_tc02_login_user.py
│   ├── test_tc03_login_user_incorrect.py
│   ├── test_tc04_logout_user.py
│   ├── test_tc05_register_user_existing_email.py
│   ├── test_tc06_contact_us_form.py
│   ├── test_tc07_verify_all_products.py
│   ├── test_tc08_search_product.py
│   ├── test_tc09_verify_product_quantity_in_cart.py
│   ├── test_tc10_place_order_register_while_checkout.py
│   └── test_tc11_download_invoice_after_purchase.py
├── utilities/
│   ├── config_reader.py
│   ├── overlay_helper.py
│   └── web_driver_factory.py
└── resources/
    └── testdata/
        └── upload.txt
```

- **pages** – Page Objects (BasePage + application pages).
- **testdata** – Dataclasses (AccountInfo, Address, etc.) and `test_data_factory` (defaults, parametrized data).
- **tests** – pytest test modules and `base_test` (shared flow helpers); overlay dismissal in `conftest`.
- **utilities** – `config_reader`, `web_driver_factory`, `overlay_helper`.
- **config.ini** – environment and browser configuration.

---

## Requirements

- **Python 3.10+**
- **Chrome** and/or **Firefox**

---

## Configuration – `config.ini`

Main options (sections: `application`, `browser`, `timeouts`):

```ini
[application]
baseUrl = https://automationexercise.com

[browser]
browser = chrome
headless = true
windowWidth = 1200
windowHeight = 800
maximizeWindow = true

[timeouts]
implicitWait = 0
explicitWait = 10
pageLoadTimeout = 30
orderSuccessWaitTimeout = 15
accountDeletedWaitTimeout = 15
alertWaitTimeout = 2
```

Override via **environment variables** (UPPER_SNAKE_CASE): e.g. `BROWSER=firefox`, `HEADLESS=false`, `BASEURL=...`.

---

## Running tests

### Install dependencies

```bash
pip install -r requirements.txt
```

On Windows if `python` is not in PATH:

```bash
py -m pip install -r requirements.txt
```

### All tests

```bash
pytest tests/ -v --tb=short
```

Or on Windows:

```bash
py -m pytest tests/ -v --tb=short
```

### Smoke tests only

```bash
pytest tests/ -m smoke -v
```

### Single TC (e.g. TC02)

```bash
pytest tests/test_tc02_login_user.py -v
```

### By marker (e.g. auth)

```bash
pytest tests/ -m auth -v
```

### Allure report (local)

Generate results and open an interactive report in the browser:

```bash
pytest tests/ --alluredir=allure-results -v
allure serve allure-results
```

- **What you get:** HTML report with test list, steps, and **screenshots on failure** (attached automatically by `conftest.py`).
- **Allure CLI:** Required for `allure serve`. Install from [Allure documentation](https://docs.qameta.io/allure/) (e.g. Windows: scoop/chocolatey; macOS: `brew install allure`).

To generate a static report folder instead of opening a server:

```bash
allure generate allure-results --clean -o allure-report
```

Then open `allure-report/index.html` in a browser.

---

## GitHub Actions CI

Workflow: `.github/workflows/ci.yml`

| Event | What runs |
|-------|-----------|
| **push** to `main` | Full regression (all tests) |
| **pull_request** to `main` | Smoke tests only (`-m smoke`) |
| **schedule** (Monday 9:00 UTC) | Full regression |

- **Browser:** CI runs tests with **Chrome** in **headless** mode via [browser-actions/setup-chrome](https://github.com/browser-actions/setup-chrome) (same approach as in the Java/Maven project).
- **Concurrency:** A new run on the same branch or PR cancels the previous one.
- **Artifacts:** Each run uploads **allure-results** (raw) and **allure-report-pages** (generated HTML) when available.

### How to view reports from CI

1. Open the repository on GitHub → **Actions** tab.
2. Click the workflow run you want (e.g. latest push or PR).
3. At the bottom of the run page, in **Artifacts**, download:
   - **allure-results** – raw data (e.g. for `allure serve` locally).
   - **allure-report-pages** – ready-made HTML report (unzip and open `index.html` in a browser).
4. To view the HTML report: download **allure-report-pages**, unzip, then open **index.html** in your browser. You will see the same report as with `allure serve` (tests, steps, screenshots on failure).

### Optional: Allure report on GitHub Pages

If you enable **GitHub Pages** (Settings → Pages → Source: **GitHub Actions**) and add the **github-pages** environment, the workflow can deploy the Allure report so it is available at a stable URL (e.g. `https://<user>.github.io/AutomationExcercisesPython/`) after each push to `main` or scheduled run. The workflow is already set up for this; you only need to turn on Pages and the environment.

---

## Test cases (TC list)

| TC    | Markers   | Description |
|-------|-----------|--------------|
| **TC01** | `tc01` | Register User |
| **TC02** | `tc02`, `smoke` | Login User (correct credentials) |
| **TC03** | `tc03` | Login User (incorrect credentials) |
| **TC04** | `tc04` | Logout User |
| **TC05** | `tc05` | Register User with existing email |
| **TC06** | `tc06` | Contact Us Form |
| **TC07** | `tc07` | Verify All Products and product detail page |
| **TC08** | `tc08` | Search Product |
| **TC09** | `tc09` | Verify Product quantity in Cart |
| **TC10** | `tc10`, `smoke` | Place Order: Register while Checkout |
| **TC11** | `tc11` | Download Invoice after purchase order |

Markers: `tc01`–`tc11`, `regression`, `smoke`, `auth`, `products`, `checkout`, `contact`.

---

## Framework overview

- **conftest.py** – session-scoped `driver` fixture, function-scoped `base_url` (navigates and dismisses overlays), screenshot on failure to Allure.
- **utilities/config_reader.py** – reads `config.ini`; env vars override; option case preserved.
- **utilities/web_driver_factory.py** – creates Chrome/Firefox via webdriver-manager; options from config.
- **utilities/overlay_helper.py** – `dismiss(driver)` for cookie consent and ad overlays.
- **tests/base_test.py** – shared helpers: `ensure_registered_user_ready_for_login`, `ensure_user_exists_with_email`, `ensure_enter_account_information_visible`.

Migration notes: **docs/PYTHON_MIGRATION_PLAN.md**.
