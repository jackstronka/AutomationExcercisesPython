"""Cart page – proceed to checkout, quantity."""
from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webdriver import WebDriver
from selenium.webdriver.support import expected_conditions as EC

from pages.base_page import BasePage


class CartPage(BasePage):
    cart_page_heading = (By.CSS_SELECTOR, "#cart_items, .cart_info, .table.table-condensed")
    first_cart_product_row = (By.CSS_SELECTOR, "tr[id^='product-']")
    proceed_to_checkout_button = (By.CSS_SELECTOR, "a[href='/checkout'], a.check_out")
    register_login_in_modal = (By.CSS_SELECTOR, ".modal-content a[href='/login']")
    quantity_cell_inside_first_row = (
        By.CSS_SELECTOR,
        "tr[id^='product-'] td.cart_quantity, "
        "tr[id^='product-'] td.cart_quantity button, "
        "tr[id^='product-'] td.cart_quantity span"
    )

    def __init__(self, driver: WebDriver):
        super().__init__(driver)

    def wait_for_product_to_appear(self) -> None:
        self.wait.until(EC.presence_of_element_located(self.first_cart_product_row))

    def is_cart_page_visible(self) -> bool:
        return self.is_element_present(self.cart_page_heading)

    def is_any_product_visible(self) -> bool:
        return self.is_element_present(self.first_cart_product_row)

    def click_proceed_to_checkout(self) -> None:
        self.click(self.proceed_to_checkout_button)

    def click_register_login_in_checkout_modal(self) -> None:
        self.click(self.register_login_in_modal)

    def get_first_product_quantity(self) -> int:
        import re
        text = self.read_text(self.quantity_cell_inside_first_row).strip()
        digits = re.sub(r"[^0-9]", "", text)
        return int(digits) if digits else 0
