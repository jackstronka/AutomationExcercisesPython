"""TC05: Register User with existing email."""
import pytest

from pages.home_page import HomePage
from pages.login_page import LoginPage
from testdata.test_data_factory import existing_email_data
from tests.base_test import ensure_user_exists_with_email


@pytest.mark.tc05
@pytest.mark.regression
@pytest.mark.auth
@pytest.mark.parametrize("user", existing_email_data())
def test_register_with_existing_email(driver, base_url, user):
    ensure_user_exists_with_email(driver, user.email)

    home_page = HomePage(driver)
    assert home_page.is_displayed(), "Home page should be visible"
    home_page.click_signup_login()
    login_page = LoginPage(driver)
    assert login_page.is_new_user_signup_section_visible(), (
        '"New User Signup!" section should be visible'
    )
    login_page.enter_new_user_name_and_email(user.name, user.email)
    login_page.click_signup_button()
    assert login_page.is_email_already_exists_error_visible(), (
        '"Email Address already exist!" message should be visible'
    )
