"""Home page – signup/login, contact, products, cart links."""
from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webdriver import WebDriver
from selenium.webdriver.support import expected_conditions as EC

from pages.base_page import BasePage
from utilities.config_reader import get


class HomePage(BasePage):
    header = (By.TAG_NAME, "header")
    signup_login_link = (By.CSS_SELECTOR, "a[href='/login']")
    contact_us_link = (By.CSS_SELECTOR, "a[href='/contact_us']")
    products_link = (By.CSS_SELECTOR, "a[href='/products']")
    first_home_view_product_link = (By.XPATH, "(//a[contains(@href,'product_details')])[1]")
    cart_link = (By.CSS_SELECTOR, "a[href='/view_cart']")

    def __init__(self, driver: WebDriver):
        super().__init__(driver)

    def is_displayed(self) -> bool:
        el = self.wait.until(EC.visibility_of_element_located(self.header))
        return el.is_displayed()

    def click_signup_login(self) -> None:
        self.click_via_javascript(self.signup_login_link)

    def click_contact_us(self) -> None:
        self.click_via_javascript(self.contact_us_link)

    def click_products(self) -> None:
        self.click_via_javascript(self.products_link)

    def click_first_home_view_product(self) -> None:
        self.click_via_javascript(self.first_home_view_product_link)

    def click_cart(self) -> None:
        self.click_via_javascript(self.cart_link)

    def navigate_to_home(self) -> None:
        self.driver.get(get("baseUrl"))
