"""Pytest fixtures: WebDriver lifecycle, base URL, overlay dismissal, screenshot on failure."""
import atexit
import logging
import sys

import allure
import pytest

from utilities.config_reader import get
from utilities.overlay_helper import dismiss
from utilities.web_driver_factory import create

logging.basicConfig(level=logging.INFO, stream=sys.stdout)
logger = logging.getLogger(__name__)

DEFAULT_BASE_URL = "https://automationexercise.com"

_driver = None


def _get_driver():
    global _driver
    if _driver is None:
        _driver = create()
        atexit.register(_quit_driver)
    return _driver


def _quit_driver():
    global _driver
    if _driver is not None:
        _driver.quit()
        _driver = None


def _get_base_url():
    url = get("baseUrl", DEFAULT_BASE_URL)
    if not url or not isinstance(url, str):
        return DEFAULT_BASE_URL
    return url


def _attach_screenshot_on_failure(driver):
    try:
        body = driver.get_screenshot_as_png()
        allure.attach(
            body,
            name="Screenshot on failure",
            attachment_type=allure.attachment_type.PNG,
        )
    except Exception as e:
        logger.warning("Failed to attach screenshot to Allure: %s", e)


def _driver_from_item(item):
    """Obtain session driver from test item for hook (e.g. screenshot on failure)."""
    try:
        return item._request.getfixturevalue("driver")
    except Exception:
        return None


@pytest.hookimpl(hookwrapper=True)
def pytest_runtest_makereport(item):
    outcome = yield
    report = outcome.get_result()
    if report.when == "call" and report.failed:
        driver = _driver_from_item(item)
        if driver is not None:
            _attach_screenshot_on_failure(driver)


@pytest.fixture(scope="session")
def driver():
    d = _get_driver()
    maximize = get("maximizeWindow", "false").lower() in ("true", "1", "yes")
    if maximize:
        d.maximize_window()
    yield d


@pytest.fixture(scope="function")
def base_url(driver):
    url = _get_base_url()
    driver.get(url)
    dismiss(driver)
    yield url


@pytest.fixture(autouse=True)
def _ensure_base_url_per_test(driver, base_url):
    """Ensures every test gets driver and base_url (navigate + dismiss overlays)."""
    yield
