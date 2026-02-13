"""WebDriver creation with config-driven options. Uses webdriver-manager."""
import os
from pathlib import Path

from selenium import webdriver
from selenium.webdriver.chrome.options import Options as ChromeOptions
from selenium.webdriver.chrome.service import Service as ChromeService
from selenium.webdriver.firefox.options import Options as FirefoxOptions
from selenium.webdriver.firefox.service import Service as FirefoxService

from utilities.config_reader import get


def create():
    browser = get("browser", "chrome").lower()
    headless = get("headless", "false").lower() in ("true", "1", "yes")
    maximize = get("maximizeWindow", "false").lower() in ("true", "1", "yes")
    width = int(get("windowWidth", "1200"))
    height = int(get("windowHeight", "800"))
    remote_url = os.environ.get("REMOTE_WEBDRIVER_URL", "").strip()

    if browser == "chrome":
        options = ChromeOptions()
        root = Path(__file__).resolve().parent.parent
        download_path = (root / "target" / "downloads").resolve()
        download_path.mkdir(parents=True, exist_ok=True)
        prefs = {
            "credentials_enable_service": False,
            "profile.password_manager_enabled": False,
            "autofill.profile_enabled": False,
            "autofill.credit_card_enabled": False,
            "download.default_directory": str(download_path),
        }
        options.add_experimental_option("prefs", prefs)
        if headless:
            options.add_argument("--headless")
        if os.environ.get("CI"):
            options.add_argument("--no-sandbox")
            options.add_argument("--disable-dev-shm-usage")
            options.add_argument("--disable-gpu")
            options.add_argument("--disable-software-rasterizer")
            options.add_argument("--disable-extensions")
        # CI with browser-actions/setup-chrome: use same Chrome + ChromeDriver to avoid version mismatch and ReadTimeoutError
        chrome_path = os.environ.get("CHROME_PATH", "").strip()
        chromedriver_path = os.environ.get("CHROMEDRIVER_PATH", "").strip()
        if chrome_path and Path(chrome_path).exists():
            options.binary_location = chrome_path
        if remote_url:
            # CI: use Selenium Docker service (no local Chrome/ChromeDriver needed)
            driver = webdriver.Remote(command_executor=remote_url, options=options)
        elif chromedriver_path and Path(chromedriver_path).exists():
            service = ChromeService(executable_path=chromedriver_path)
            driver = webdriver.Chrome(service=service, options=options)
        else:
            from webdriver_manager.chrome import ChromeDriverManager
            service = ChromeService(ChromeDriverManager().install())
            driver = webdriver.Chrome(service=service, options=options)
    elif browser == "firefox":
        options = FirefoxOptions()
        root = Path(__file__).resolve().parent.parent
        ff_download = (root / "target" / "downloads").resolve()
        ff_download.mkdir(parents=True, exist_ok=True)
        options.set_preference("browser.download.folderList", 2)
        options.set_preference("browser.download.dir", str(ff_download))
        options.set_preference(
            "browser.helperApps.neverAsk.saveToDisk",
            "application/pdf,text/plain,application/octet-stream"
        )
        # CI: headless required (no display); avoids "Process unexpectedly closed with status 1"
        if headless or os.environ.get("CI"):
            options.add_argument("--headless")
        # Prefer system geckodriver in CI to match apt-installed Firefox
        gecko_path = "/usr/bin/geckodriver" if os.environ.get("CI") else None
        if gecko_path and Path(gecko_path).exists():
            service = FirefoxService(executable_path=gecko_path)
        else:
            from webdriver_manager.firefox import GeckoDriverManager
            service = FirefoxService(GeckoDriverManager().install())
        driver = webdriver.Firefox(service=service, options=options)
    else:
        raise ValueError(f"Unsupported browser: {browser}")

    driver.implicitly_wait(int(get("implicitWait", "0")))
    driver.set_page_load_timeout(int(get("pageLoadTimeout", "30")))

    if not maximize:
        driver.set_window_size(width, height)

    return driver
