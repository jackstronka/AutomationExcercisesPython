"""Checkout page – address, review, payment, Place Order."""
from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webdriver import WebDriver

from pages.base_page import BasePage


class CheckoutPage(BasePage):
    address_details_section = (By.XPATH, "//*[contains(.,'Address Details')]")
    review_your_order_section = (By.XPATH, "//*[contains(.,'Review Your Order')]")
    comment_textarea = (By.NAME, "message")
    name_on_card_input = (By.NAME, "name_on_card")
    card_number_input = (By.NAME, "card_number")
    cvc_input = (By.NAME, "cvc")
    expiry_month_input = (By.NAME, "expiry_month")
    expiry_year_input = (By.NAME, "expiry_year")
    place_order_button = (By.CSS_SELECTOR, "a.check_out[href='/payment'], a.check_out")
    pay_and_confirm_order_button = (
        By.CSS_SELECTOR,
        "button[data-qa='pay-button'], button.btn.btn-default.check_out"
    )

    def __init__(self, driver: WebDriver):
        super().__init__(driver)

    def is_address_and_review_visible(self) -> bool:
        return (
            self.is_element_present(self.address_details_section)
            and self.is_element_present(self.review_your_order_section)
        )

    def enter_order_comment(self, comment: str) -> None:
        self.write_text(self.comment_textarea, comment)

    def fill_payment_details(
        self,
        name_on_card: str,
        card_number: str,
        cvc: str,
        expiry_month: str,
        expiry_year: str,
    ) -> None:
        self.scroll_into_view(self.name_on_card_input)
        self.write_text(self.name_on_card_input, name_on_card)
        self.write_text(self.card_number_input, card_number)
        self.write_text(self.cvc_input, cvc)
        self.write_text(self.expiry_month_input, expiry_month)
        self.write_text(self.expiry_year_input, expiry_year)

    def click_place_order(self) -> None:
        self.click_via_javascript(self.place_order_button)

    def click_pay_and_confirm_order(self) -> None:
        self.click(self.pay_and_confirm_order_button)
