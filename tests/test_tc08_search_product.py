"""TC08: Search Product."""
import pytest

from pages.home_page import HomePage
from pages.products_page import ProductsPage


@pytest.mark.tc08
@pytest.mark.regression
@pytest.mark.products
def test_search_product(driver, base_url):
    home_page = HomePage(driver)
    assert home_page.is_displayed(), "Home page should be visible"
    home_page.click_products()
    products_page = ProductsPage(driver)
    assert products_page.is_all_products_page_visible(), (
        "User should be redirected to All Products page"
    )
    products_page.search_product("winter")
    assert products_page.is_searched_products_visible(), (
        '"SEARCHED PRODUCTS" section should be visible'
    )
    assert products_page.are_search_results_containing("Winter Top"), (
        "Search results should contain: Winter Top"
    )
