"""Signup page – Enter Account Information and address details."""
from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webdriver import WebDriver
from selenium.webdriver.support import expected_conditions as EC

from pages.base_page import BasePage


class SignupPage(BasePage):
    enter_account_information_section = (By.XPATH, "//*[contains(text(),'Enter Account Information')]")
    title_mr = (By.ID, "id_gender1")
    title_mrs = (By.ID, "id_gender2")
    password_input = (By.CSS_SELECTOR, "[data-qa='password']")
    days_select = (By.CSS_SELECTOR, "[data-qa='days']")
    months_select = (By.CSS_SELECTOR, "[data-qa='months']")
    years_select = (By.CSS_SELECTOR, "[data-qa='years']")
    newsletter_checkbox = (By.ID, "newsletter")
    optin_checkbox = (By.ID, "optin")
    first_name_input = (By.CSS_SELECTOR, "[data-qa='first_name']")
    last_name_input = (By.CSS_SELECTOR, "[data-qa='last_name']")
    address1_input = (By.CSS_SELECTOR, "[data-qa='address']")
    country_select = (By.CSS_SELECTOR, "[data-qa='country']")
    state_input = (By.CSS_SELECTOR, "[data-qa='state']")
    city_input = (By.CSS_SELECTOR, "[data-qa='city']")
    zipcode_input = (By.CSS_SELECTOR, "[data-qa='zipcode']")
    mobile_number_input = (By.CSS_SELECTOR, "[data-qa='mobile_number']")
    create_account_button = (By.CSS_SELECTOR, "[data-qa='create-account']")

    def __init__(self, driver: WebDriver):
        super().__init__(driver)

    def is_enter_account_information_section_visible(self) -> bool:
        return self.is_element_present(self.enter_account_information_section)

    def fill_account_information(self, password: str, title: str, day: str, month: str, year: str) -> None:
        if title and title.lower() == "mrs":
            self.click(self.title_mrs)
        else:
            self.click(self.title_mr)
        self.write_text(self.password_input, password)
        self.select_by_value(self.days_select, day)
        self.select_by_value(self.months_select, month)
        self.select_by_value(self.years_select, year)

    def subscribe_to_newsletter(self) -> None:
        if not self.is_checkbox_selected(self.newsletter_checkbox):
            self.click_via_javascript(self.newsletter_checkbox)

    def agree_to_special_offers(self) -> None:
        if not self.is_checkbox_selected(self.optin_checkbox):
            self.click_via_javascript(self.optin_checkbox)

    def fill_address_details(
        self,
        first_name: str,
        last_name: str,
        address1: str,
        country: str,
        state: str,
        city: str,
        zipcode: str,
        mobile_number: str,
    ) -> None:
        self.write_text(self.first_name_input, first_name)
        self.write_text(self.last_name_input, last_name)
        self.write_text(self.address1_input, address1)
        self.select_by_visible_text(self.country_select, country)
        self.write_text(self.state_input, state)
        self.write_text(self.city_input, city)
        self.write_text(self.zipcode_input, zipcode)
        self.write_text(self.mobile_number_input, mobile_number)

    def click_create_account(self) -> None:
        self.wait.until(EC.element_to_be_clickable(self.create_account_button))
        self.click_via_javascript(self.create_account_button)
