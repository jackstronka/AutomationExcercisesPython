"""TC09: Verify Product quantity in Cart."""
import pytest

from pages.cart_page import CartPage
from pages.home_page import HomePage
from pages.product_detail_page import ProductDetailPage


@pytest.mark.tc09
@pytest.mark.regression
@pytest.mark.products
def test_verify_product_quantity_in_cart(driver, base_url):
    home_page = HomePage(driver)
    assert home_page.is_displayed(), "Home page should be visible"
    home_page.click_first_home_view_product()
    product_detail_page = ProductDetailPage(driver)
    assert product_detail_page.is_product_name_visible(), "User should be on product detail page"
    product_detail_page.set_quantity(4)
    product_detail_page.click_add_to_cart()
    product_detail_page.click_view_cart_in_modal()
    cart_page = CartPage(driver)
    cart_page.wait_for_product_to_appear()
    assert cart_page.is_any_product_visible(), "At least one product should be visible in cart"
    actual_quantity = cart_page.get_first_product_quantity()
    assert actual_quantity == 4, "Product quantity in cart should equal 4"
