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

    if browser == "chrome":
        from webdriver_manager.chrome import ChromeDriverManager
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
        service = ChromeService(ChromeDriverManager().install())
        driver = webdriver.Chrome(service=service, options=options)
    elif browser == "firefox":
        from webdriver_manager.firefox import GeckoDriverManager
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
        if headless:
            options.add_argument("--headless")
        service = FirefoxService(GeckoDriverManager().install())
        driver = webdriver.Firefox(service=service, options=options)
    else:
        raise ValueError(f"Unsupported browser: {browser}")

    driver.implicitly_wait(int(get("implicitWait", "0")))
    driver.set_page_load_timeout(int(get("pageLoadTimeout", "30")))

    if not maximize:
        driver.set_window_size(width, height)

    return driver
