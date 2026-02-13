"""Dismiss cookie consent and ad overlays that may block clicks (per Rule 15)."""
import logging

from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webdriver import WebDriver
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait

logger = logging.getLogger(__name__)
COOKIE_WAIT_SECONDS = 3


def dismiss(driver: WebDriver) -> None:
    """Dismiss cookie consent, remove/hide ad overlays, remove #google_vignette from URL."""
    _remove_google_vignette_from_url(driver)
    _dismiss_ad_overlays(driver)
    _dismiss_cookie_overlay(driver)


def _remove_google_vignette_from_url(driver: WebDriver) -> None:
    try:
        url = driver.current_url
        if url and "#google_vignette" in url:
            driver.execute_script(
                "window.history.replaceState(null, '', window.location.pathname + window.location.search);"
            )
    except Exception as e:
        logger.debug("removeGoogleVignetteFromUrl: %s", e)


def _dismiss_ad_overlays(driver: WebDriver) -> None:
    try:
        remove_script = (
            "document.querySelectorAll('ins.adsbygoogle, .adsbygoogle-noablate, "
            "iframe[id*=\"aswift\"], iframe[id*=\"google_ads_iframe\"], iframe[id*=\"ad_iframe\"]')"
            ".forEach(function(e){ e.remove(); });"
        )
        driver.execute_script(remove_script)
        hide_script = (
            "document.querySelectorAll('iframe[title*=\"Advertisement\"], iframe[title*=\"Reklama\"], "
            "[id*=\"google_ads\"]').forEach(function(e){ "
            "e.style.setProperty('display','none','important'); "
            "e.style.setProperty('visibility','hidden','important'); });"
        )
        driver.execute_script(hide_script)
    except Exception as e:
        logger.debug("dismissAdOverlays: %s", e)


def _dismiss_cookie_overlay(driver: WebDriver) -> None:
    try:
        wait = WebDriverWait(driver, COOKIE_WAIT_SECONDS)
        if not driver.find_elements(By.CSS_SELECTOR, ".fc-dialog-overlay, .fc-consent"):
            return
        accept_selectors = [
            (By.CSS_SELECTOR, ".fc-cta-consent"),
            (By.CSS_SELECTOR, ".fc-consent .fc-primary-button"),
            (By.XPATH, "//button[contains(translate(., 'ACCEPT', 'accept'), 'accept')]"),
            (By.XPATH, "//a[contains(translate(., 'ACCEPT', 'accept'), 'accept')]"),
        ]
        for by, selector in accept_selectors:
            if driver.find_elements(by, selector):
                wait.until(EC.element_to_be_clickable((by, selector))).click()
                return
        driver.execute_script(
            "var el = document.querySelector('.fc-dialog-overlay'); if(el) el.style.display='none';"
        )
    except Exception as e:
        logger.debug("dismissCookieOverlay: %s (overlay may not be visible)", e)
