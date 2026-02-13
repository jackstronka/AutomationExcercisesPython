"""Contact Us page – form and submit."""
from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webdriver import WebDriver
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.action_chains import ActionChains

from pages.base_page import BasePage
from utilities.config_reader import get


class ContactUsPage(BasePage):
    get_in_touch_heading = (By.XPATH, "//h2[contains(text(),'Get In Touch')]")
    name_input = (By.CSS_SELECTOR, "[data-qa='name']")
    email_input = (By.CSS_SELECTOR, "[data-qa='email']")
    subject_input = (By.CSS_SELECTOR, "[data-qa='subject']")
    message_textarea = (By.CSS_SELECTOR, "[data-qa='message']")
    upload_file_input = (By.CSS_SELECTOR, "input[name='upload_file']")
    submit_button = (By.CSS_SELECTOR, "[data-qa='submit-button']")
    success_message = (By.CSS_SELECTOR, "#contact-page .status.alert.alert-success")
    home_link = (By.XPATH, "//a[@href='/'][.//span[contains(.,'Home')]]")
    home_link_nav = (By.CSS_SELECTOR, "a[href='/']")

    def __init__(self, driver: WebDriver):
        super().__init__(driver)
        self._success_message_seen_in_alert = False

    def is_get_in_touch_visible(self) -> bool:
        return self.is_element_present(self.get_in_touch_heading)

    def fill_contact_form(self, name: str, email: str, subject: str, message: str) -> None:
        self.write_text(self.name_input, name)
        self.write_text(self.email_input, email)
        self.write_text(self.subject_input, subject)
        self.write_text(self.message_textarea, message)

    def upload_file(self, absolute_path: str) -> None:
        input_el = self.get_element(self.upload_file_input)
        self.scroll_into_view(self.upload_file_input)
        if not input_el.is_displayed():
            self.driver.execute_script(
                "arguments[0].style.display='block'; arguments[0].style.visibility='visible'; "
                "arguments[0].style.opacity='1';",
                input_el
            )
        input_el.send_keys(absolute_path)

    def click_submit(self) -> None:
        self.scroll_into_view(self.submit_button)
        self._dismiss_overlays_that_block_form()
        btn = self.wait.until(EC.element_to_be_clickable(self.submit_button))
        self.driver.execute_script(
            "arguments[0].style.position='relative'; arguments[0].style.zIndex='999999';", btn
        )
        try:
            btn.click()
        except Exception:
            try:
                ActionChains(self.driver).move_to_element(btn).click().perform()
            except Exception:
                pass
        self.accept_alert_if_present()

    def _dismiss_overlays_that_block_form(self) -> None:
        try:
            script = (
                "document.querySelectorAll('.fc-dialog-overlay, .fc-consent, .adsbygoogle, "
                "[id*=\"google_ads\"], [class*=\"cookie\"], [id*=\"cookie\"], "
                "[class*=\"GDPR\"], [class*=\"consent\"], .cc-window, #cc-window').forEach(function(e){ "
                "e.style.setProperty('display','none','important'); "
                "e.style.setProperty('visibility','hidden','important'); });"
            )
            self.driver.execute_script(script)
        except Exception:
            pass

    def accept_alert_if_present(self) -> None:
        if self._success_message_seen_in_alert:
            return
        try:
            timeout = int(get("alertWaitTimeout", "2"))
            alert_wait = WebDriverWait(self.driver, timeout)
            alert_wait.until(EC.alert_is_present())
            alert = self.driver.switch_to.alert
            alert_text = alert.text
            alert.accept()
            if alert_text and alert_text.strip():
                self._success_message_seen_in_alert = True
        except Exception:
            pass

    def is_success_message_visible(self) -> bool:
        if self._success_message_seen_in_alert:
            return True
        try:
            timeout = int(get("explicitWait", "10"))
            w = WebDriverWait(self.driver, timeout)
            w.until(EC.visibility_of_element_located(self.success_message))
            return True
        except Exception:
            return False

    def click_home(self) -> None:
        if self.driver.find_elements(*self.home_link):
            self.click_via_javascript(self.home_link)
        else:
            self.click_via_javascript(self.home_link_nav)
