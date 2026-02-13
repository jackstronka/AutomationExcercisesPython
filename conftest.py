"""Pytest fixtures – WebDriver lifecycle, base URL, overlay dismissal, screenshot on failure."""
import logging
import sys

import allure
import pytest

from utilities.config_reader import get
from utilities.overlay_helper import dismiss
from utilities.web_driver_factory import create

logging.basicConfig(level=logging.INFO, stream=sys.stdout)
logger = logging.getLogger(__name__)

_driver = None


def _get_driver():
    global _driver
    if _driver is None:
        _driver = create()
        _register_shutdown_hook()
    return _driver


def _register_shutdown_hook():
    import atexit
    def _quit():
        global _driver
        if _driver is not None:
            _driver.quit()
            globals()["_driver"] = None
    atexit.register(_quit)


@pytest.fixture(scope="session")
def driver():
    d = _get_driver()
    maximize = get("maximizeWindow", "false").lower() in ("true", "1", "yes")
    if maximize:
        d.maximize_window()
    yield d


@pytest.fixture(scope="function")
def base_url(driver):
    url = get("baseUrl", "https://automationexercise.com")
    if not url or not isinstance(url, str):
        url = "https://automationexercise.com"
    driver.get(url)
    dismiss(driver)
    yield url


@pytest.fixture(autouse=True)
def _reset_page_and_overlays(driver, base_url):
    """Before each test: already navigated in base_url fixture. Re-dismiss overlays if needed."""
    yield
    # After each test: attach screenshot on failure
    pass


def _attach_screenshot_on_failure(driver):
    try:
        body = driver.get_screenshot_as_png()
        allure.attach(body, name="Screenshot on failure", attachment_type=allure.attachment_type.PNG)
    except Exception as e:
        logger.warning("Failed to attach screenshot to Allure: %s", e)


@pytest.hookimpl(hookwrapper=True)
def pytest_runtest_makereport(item):
    outcome = yield
    report = outcome.get_result()
    if report.when == "call" and report.failed:
        driver = getattr(item, "funcargs", {}).get("driver", None)
        if driver is not None:
            _attach_screenshot_on_failure(driver)
