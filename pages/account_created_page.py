"""Account created page – confirmation and Continue button."""
from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webdriver import WebDriver
from selenium.webdriver.support import expected_conditions as EC

from pages.base_page import BasePage


class AccountCreatedPage(BasePage):
    account_created_heading = (
        By.XPATH,
        "//*[contains(.,'Account Created') or contains(.,'ACCOUNT CREATED')]"
    )
    continue_button = (
        By.XPATH,
        "//*[contains(.,'Account Created') or contains(.,'ACCOUNT CREATED')]"
        "//a[contains(.,'Continue') or contains(.,'CONTINUE')]"
    )

    def __init__(self, driver: WebDriver):
        super().__init__(driver)

    def is_displayed(self) -> bool:
        return self.is_element_present(self.account_created_heading)

    def is_account_created_message_visible(self) -> bool:
        try:
            self.wait.until(EC.visibility_of_element_located(self.account_created_heading))
            return True
        except Exception:
            return False

    def click_continue(self) -> None:
        self.click_via_javascript(self.continue_button)
