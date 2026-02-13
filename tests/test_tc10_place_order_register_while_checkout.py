"""TC10: Place Order – Register while Checkout."""
import pytest

from pages.account_created_page import AccountCreatedPage
from pages.cart_page import CartPage
from pages.checkout_page import CheckoutPage
from pages.home_page import HomePage
from pages.login_page import LoginPage
from pages.order_success_page import OrderSuccessPage
from pages.product_detail_page import ProductDetailPage
from pages.signup_page import SignupPage
from testdata.test_data_factory import (
    DEFAULT_ORDER_COMMENT,
    default_account_info,
    default_address,
    default_payment_details,
    checkout_user_data,
)
from tests.base_test import ensure_enter_account_information_visible


@pytest.mark.tc10
@pytest.mark.regression
@pytest.mark.checkout
@pytest.mark.smoke
@pytest.mark.parametrize("user", checkout_user_data())
def test_place_order_register_while_checkout(driver, base_url, user):
    account = default_account_info()
    address = default_address()
    payment = default_payment_details()

    home_page = HomePage(driver)
    assert home_page.is_displayed(), "Home page should be visible"

    home_page.click_first_home_view_product()
    product_detail_page = ProductDetailPage(driver)
    assert product_detail_page.is_product_name_visible(), "User should be on product detail page"
    product_detail_page.set_quantity(1)
    product_detail_page.click_add_to_cart()
    product_detail_page.click_view_cart_in_modal()

    cart_page = CartPage(driver)
    assert cart_page.is_cart_page_visible(), "Cart page (Shopping Cart) should be visible"
    cart_page.click_proceed_to_checkout()
    cart_page.click_register_login_in_checkout_modal()

    login_page = LoginPage(driver)
    assert login_page.is_new_user_signup_section_visible(), (
        '"New User Signup!" section should be visible'
    )
    login_page.enter_new_user_name_and_email(user.name, user.email)
    login_page.click_signup_button()
    signup_page = ensure_enter_account_information_visible(
        driver, login_page, SignupPage(driver), user.name, user.email
    )
    assert signup_page is not None, "Signup page should be present after signup"
    assert signup_page.is_enter_account_information_section_visible(), (
        '"ENTER ACCOUNT INFORMATION" section should be visible'
    )
    signup_page.fill_account_information(
        account.password, account.title, account.day, account.month, account.year
    )
    signup_page.subscribe_to_newsletter()
    signup_page.agree_to_special_offers()
    signup_page.fill_address_details(
        address.first_name, address.last_name, address.street_address,
        address.country, address.state, address.city, address.zipcode, address.mobile
    )
    signup_page.click_create_account()
    account_created_page = AccountCreatedPage(driver)
    assert account_created_page.is_account_created_message_visible(), (
        "ACCOUNT CREATED! should be visible"
    )
    account_created_page.click_continue()
    assert login_page.is_logged_in_as_user(), "User should be logged in"

    home_page = HomePage(driver)
    home_page.click_cart()
    cart_page = CartPage(driver)
    cart_page.click_proceed_to_checkout()
    checkout_page = CheckoutPage(driver)
    assert checkout_page.is_address_and_review_visible(), (
        '"Address Details" and "Review Your Order" should be visible'
    )
    checkout_page.enter_order_comment(DEFAULT_ORDER_COMMENT)
    checkout_page.click_place_order()
    checkout_page.fill_payment_details(
        payment.name_on_card, payment.card_number, payment.cvc,
        payment.expiry_month, payment.expiry_year
    )
    checkout_page.click_pay_and_confirm_order()
    order_success_page = OrderSuccessPage(driver)
    assert order_success_page.wait_for_order_success_message_or_payment_done(), (
        '"Your order has been placed successfully!" message or payment_done page should be visible'
    )

    login_page = LoginPage(driver)
    login_page.click_delete_account()
    assert login_page.is_account_deleted_message_visible(), "ACCOUNT DELETED! should be visible"
    login_page.click_continue_after_delete()
