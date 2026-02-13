"""TC01: Register User – full registration and account deletion."""
import pytest

from pages.account_created_page import AccountCreatedPage
from pages.home_page import HomePage
from pages.login_page import LoginPage
from pages.signup_page import SignupPage
from testdata.test_data_factory import (
    default_account_info,
    default_address,
    register_user_data,
)
from tests.base_test import ensure_enter_account_information_visible


@pytest.mark.tc01
@pytest.mark.regression
@pytest.mark.auth
@pytest.mark.parametrize("user", register_user_data())
def test_register_user(driver, base_url, user):
    account = default_account_info()
    address = default_address()

    home_page = HomePage(driver)
    assert home_page.is_displayed(), "Home page should be visible"

    home_page.click_signup_login()
    login_page = LoginPage(driver)
    assert login_page.is_new_user_signup_section_visible(), '"New User Signup!" section should be visible'

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
    assert account_created_page.is_account_created_message_visible(), "ACCOUNT CREATED! should be visible"
    account_created_page.click_continue()

    assert login_page.is_logged_in_as_user(), "User should be logged in (Logged in as username)"
    login_page.click_delete_account()
    assert login_page.is_account_deleted_message_visible(), "ACCOUNT DELETED! should be visible"
    login_page.click_continue_after_delete()
