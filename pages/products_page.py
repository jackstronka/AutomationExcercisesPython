"""Products page – All Products list and search."""
from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webdriver import WebDriver

from pages.base_page import BasePage


class ProductsPage(BasePage):
    all_products_heading = (By.XPATH, "//h2[contains(text(),'All Products')]")
    products_list = (
        By.CSS_SELECTOR,
        ".features_items .single-products, .features_items .product-image-wrapper"
    )
    first_view_product_link = (By.XPATH, "(//a[contains(@href,'product_details')])[1]")
    search_input = (By.ID, "search_product")
    search_button = (By.ID, "submit_search")
    searched_products_heading = (By.XPATH, "//h2[contains(.,'Searched Products')]")

    def __init__(self, driver: WebDriver):
        super().__init__(driver)

    def is_all_products_page_visible(self) -> bool:
        return self.is_element_present(self.all_products_heading)

    def is_products_list_visible(self) -> bool:
        return len(self.driver.find_elements(*self.products_list)) > 0

    def click_first_view_product(self) -> None:
        self.scroll_into_view(self.first_view_product_link)
        self.click_via_javascript(self.first_view_product_link)

    def search_product(self, search_term: str) -> None:
        self.write_text(self.search_input, search_term)
        self.click(self.search_button)

    def is_searched_products_visible(self) -> bool:
        return self.is_element_present(self.searched_products_heading)

    def are_search_results_containing(self, expected_product_name: str) -> bool:
        return expected_product_name in self.driver.find_element(By.TAG_NAME, "body").text
