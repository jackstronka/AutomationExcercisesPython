"""TC07: Verify All Products and product detail page."""
import pytest

from pages.home_page import HomePage
from pages.product_detail_page import ProductDetailPage
from pages.products_page import ProductsPage


@pytest.mark.tc07
@pytest.mark.regression
@pytest.mark.products
def test_verify_all_products_and_detail_page(driver, base_url):
    home_page = HomePage(driver)
    assert home_page.is_displayed(), "Home page should be visible"
    home_page.click_products()
    products_page = ProductsPage(driver)
    assert products_page.is_all_products_page_visible(), (
        "User should be redirected to All Products page"
    )
    assert products_page.is_products_list_visible(), "Products list should be visible"
    products_page.click_first_view_product()
    product_detail_page = ProductDetailPage(driver)
    assert product_detail_page.is_product_name_visible(), (
        "User should be on product detail page"
    )
    assert product_detail_page.are_all_product_details_visible(), (
        "All product details (name, category, price, availability, condition, brand) should be visible"
    )
