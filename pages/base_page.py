"""Base Page with shared WebDriverWait and common actions (click, writeText, etc.)."""
from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webdriver import WebDriver
from selenium.webdriver.remote.webelement import WebElement
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait

from utilities.config_reader import get


class BasePage:
    def __init__(self, driver: WebDriver):
        self.driver = driver
        timeout = int(get("explicitWait", "10"))
        self.wait = WebDriverWait(driver, timeout)

    def click(self, locator: tuple) -> None:
        self.wait.until(EC.element_to_be_clickable(locator)).click()

    def click_via_javascript(self, locator: tuple) -> None:
        """Click via JS – bypasses overlay/ads blocking the element."""
        element = self.wait.until(EC.presence_of_element_located(locator))
        self.driver.execute_script("arguments[0].click();", element)

    def write_text(self, locator: tuple, text: str) -> None:
        element = self.wait.until(EC.visibility_of_element_located(locator))
        element.clear()
        element.send_keys(text)

    def read_text(self, locator: tuple) -> str:
        return self.wait.until(EC.visibility_of_element_located(locator)).text

    def is_element_present(self, locator: tuple) -> bool:
        return len(self.driver.find_elements(*locator)) > 0

    def get_element(self, locator: tuple) -> WebElement:
        return self.wait.until(EC.presence_of_element_located(locator))

    def scroll_into_view(self, locator: tuple) -> None:
        element = self.get_element(locator)
        self.driver.execute_script("arguments[0].scrollIntoView({block:'center'});", element)

    def select_by_value_via_javascript(self, locator: tuple, value: str) -> None:
        if not value:
            return
        element = self.wait.until(EC.presence_of_element_located(locator))
        self.driver.execute_script(
            "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
            element, value
        )

    def select_by_visible_text_via_javascript(self, locator: tuple, text: str) -> None:
        if not text:
            return
        element = self.wait.until(EC.presence_of_element_located(locator))
        self.driver.execute_script(
            "var opt = Array.from(arguments[0].options).find(o => o.text === arguments[1]); "
            "if(opt) { arguments[0].value = opt.value; arguments[0].dispatchEvent(new Event('change', { bubbles: true })); }",
            element, text
        )

    def select_by_value(self, locator: tuple, value: str) -> None:
        if value:
            self.select_by_value_via_javascript(locator, value)

    def select_by_visible_text(self, locator: tuple, text: str) -> None:
        if text:
            self.select_by_visible_text_via_javascript(locator, text)

    def is_checkbox_selected(self, locator: tuple) -> bool:
        return self.is_element_present(locator) and self.get_element(locator).is_selected()

    def get_title(self) -> str:
        return self.driver.title

    def get_current_url(self) -> str:
        return self.driver.current_url
