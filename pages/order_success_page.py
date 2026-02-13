"""Order success page (payment_done) – Download Invoice, Continue."""
from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webdriver import WebDriver
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait

from pages.base_page import BasePage
from utilities.config_reader import get


ORDER_SUCCESS_MESSAGE = (
    By.XPATH,
    "//*[contains(.,'Your order has been placed successfully!')]"
)


class OrderSuccessPage(BasePage):
    download_invoice_link = (
        By.CSS_SELECTOR,
        "a.btn.btn-default.check_out[href^='/download_invoice']"
    )
    continue_button = (By.CSS_SELECTOR, "a.btn.btn-primary[data-qa='continue-button']")

    def __init__(self, driver: WebDriver):
        super().__init__(driver)

    def click_download_invoice(self) -> None:
        self.scroll_into_view(self.download_invoice_link)
        self.click(self.download_invoice_link)

    def click_continue(self) -> None:
        self.click_via_javascript(self.continue_button)

    def is_continue_button_visible(self) -> bool:
        if not self.is_element_present(self.continue_button):
            return False
        el = self.get_element(self.continue_button)
        return el.is_displayed() and el.is_enabled()

    def wait_until_continue_clickable(self) -> bool:
        try:
            self.wait.until(EC.element_to_be_clickable(self.continue_button))
            return True
        except Exception:
            return False

    def is_download_invoice_visible(self) -> bool:
        return self.is_element_present(self.download_invoice_link)

    def wait_for_order_success_message_or_payment_done(self) -> bool:
        timeout = int(get("orderSuccessWaitTimeout", "15"))
        w = WebDriverWait(self.driver, timeout)
        try:
            def _check(d):
                if d.current_url and "payment_done" in d.current_url:
                    return True
                return len(d.find_elements(*ORDER_SUCCESS_MESSAGE)) > 0
            w.until(_check)
            return True
        except Exception:
            return bool(self.driver.current_url and "payment_done" in self.driver.current_url)
