"""Login page – login form and new user signup section."""
from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webdriver import WebDriver
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait

from pages.base_page import BasePage
from utilities.config_reader import get


class LoginPage(BasePage):
    new_user_name_input = (By.CSS_SELECTOR, "[data-qa='signup-name']")
    new_user_email_input = (By.CSS_SELECTOR, "[data-qa='signup-email']")
    signup_button = (By.CSS_SELECTOR, "[data-qa='signup-button']")
    new_user_signup_section = (By.XPATH, "//*[contains(text(),'New User Signup!')]")
    email_already_exists_error = (By.XPATH, "//p[contains(text(),'Email Address already exist!')]")
    login_incorrect_error = (By.XPATH, "//p[contains(text(),'Your email or password is incorrect!')]")
    login_to_your_account_section = (By.XPATH, "//*[contains(text(),'Login to your account')]")
    login_email_input = (By.CSS_SELECTOR, "[data-qa='login-email']")
    login_password_input = (By.CSS_SELECTOR, "[data-qa='login-password']")
    login_button = (By.CSS_SELECTOR, "[data-qa='login-button']")
    logged_in_as_label = (By.XPATH, "//*[contains(.,'Logged in as') or contains(.,'LOGGED IN AS')]")
    delete_account_button = (By.CSS_SELECTOR, "a[href='/delete_account']")
    account_deleted_message = (By.XPATH, "//*[contains(.,'Account Deleted') or contains(.,'ACCOUNT DELETED')]")
    continue_button_after_delete = (By.XPATH, "//a[contains(.,'Continue') or contains(.,'CONTINUE')]")
    logout_link = (By.XPATH, "//a[contains(.,'Logout') or contains(.,'LOGOUT')]")

    def __init__(self, driver: WebDriver):
        super().__init__(driver)

    def enter_new_user_name_and_email(self, name: str, email: str) -> None:
        self.write_text(self.new_user_name_input, name)
        self.write_text(self.new_user_email_input, email)

    def click_signup_button(self) -> None:
        self.click(self.signup_button)

    def is_new_user_signup_section_visible(self) -> bool:
        return self.is_element_present(self.new_user_signup_section)

    def is_email_already_exists_error_visible(self) -> bool:
        try:
            timeout = int(get("explicitWait", "10"))
            w = WebDriverWait(self.driver, timeout)
            w.until(EC.presence_of_element_located(self.email_already_exists_error))
            return True
        except Exception:
            return False

    def is_login_incorrect_error_visible(self) -> bool:
        try:
            timeout = int(get("explicitWait", "10"))
            w = WebDriverWait(self.driver, timeout)
            w.until(EC.presence_of_element_located(self.login_incorrect_error))
            return True
        except Exception:
            return False

    def is_login_to_your_account_section_visible(self) -> bool:
        return self.is_element_present(self.login_to_your_account_section)

    def click_logout(self) -> None:
        self.click(self.logout_link)

    def enter_login_credentials(self, email: str, password: str) -> None:
        self.write_text(self.login_email_input, email)
        self.write_text(self.login_password_input, password)

    def click_login_button(self) -> None:
        self.click(self.login_button)

    def is_logged_in_as_user(self) -> bool:
        try:
            self.wait.until(EC.visibility_of_element_located(self.logged_in_as_label))
            return True
        except Exception:
            return False

    def click_delete_account(self) -> None:
        self.click_via_javascript(self.delete_account_button)

    def is_account_deleted_message_visible(self) -> bool:
        try:
            timeout = int(get("accountDeletedWaitTimeout", "15"))
            w = WebDriverWait(self.driver, timeout)
            el = w.until(EC.presence_of_element_located(self.account_deleted_message))
            self.driver.execute_script("arguments[0].scrollIntoView(true);", el)
            w.until(EC.visibility_of(el))
            return True
        except Exception:
            return False

    def click_continue_after_delete(self) -> None:
        self.click_via_javascript(self.continue_button_after_delete)
