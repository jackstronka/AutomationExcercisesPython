"""Product detail page – quantity, Add to Cart, View Cart in modal."""
from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webdriver import WebDriver
from selenium.webdriver.support import expected_conditions as EC

from pages.base_page import BasePage


class ProductDetailPage(BasePage):
    product_name = (By.CSS_SELECTOR, ".product-information h2, .product-info h2")
    product_category = (
        By.XPATH,
        "//div[contains(@class,'product-information') or contains(@class,'product-info')]"
        "//*[contains(.,'Category:')]"
    )
    product_price = (
        By.XPATH,
        "//div[contains(@class,'product-information') or contains(@class,'product-info')]//*[contains(.,'Rs.')]"
    )
    product_availability = (
        By.XPATH,
        "//div[contains(@class,'product-information') or contains(@class,'product-info')]"
        "//*[contains(.,'Availability:')]"
    )
    product_condition = (
        By.XPATH,
        "//div[contains(@class,'product-information') or contains(@class,'product-info')]"
        "//*[contains(.,'Condition:')]"
    )
    product_brand = (
        By.XPATH,
        "//div[contains(@class,'product-information') or contains(@class,'product-info')]"
        "//*[contains(.,'Brand:')]"
    )
    add_to_cart_button = (By.CSS_SELECTOR, "button.btn.btn-default.cart")
    view_cart_link_in_modal = (By.CSS_SELECTOR, ".modal a[href='/view_cart'], .modal-content a[href='/view_cart']")
    quantity_input = (By.ID, "quantity")

    def __init__(self, driver: WebDriver):
        super().__init__(driver)

    def is_product_name_visible(self) -> bool:
        try:
            return self.get_element(self.product_name).is_displayed()
        except Exception:
            return False

    def is_product_category_visible(self) -> bool:
        return self.is_element_present(self.product_category)

    def is_product_price_visible(self) -> bool:
        return self.is_element_present(self.product_price)

    def is_product_availability_visible(self) -> bool:
        return self.is_element_present(self.product_availability)

    def is_product_condition_visible(self) -> bool:
        return self.is_element_present(self.product_condition)

    def is_product_brand_visible(self) -> bool:
        return self.is_element_present(self.product_brand)

    def are_all_product_details_visible(self) -> bool:
        return (
            self.is_product_name_visible()
            and self.is_product_category_visible()
            and self.is_product_price_visible()
            and self.is_product_availability_visible()
            and self.is_product_condition_visible()
            and self.is_product_brand_visible()
        )

    def set_quantity(self, quantity: int) -> None:
        self.write_text(self.quantity_input, str(quantity))

    def click_add_to_cart(self) -> None:
        self.click(self.add_to_cart_button)

    def click_view_cart_in_modal(self) -> None:
        self.wait.until(EC.element_to_be_clickable(self.view_cart_link_in_modal))
        self.click(self.view_cart_link_in_modal)
