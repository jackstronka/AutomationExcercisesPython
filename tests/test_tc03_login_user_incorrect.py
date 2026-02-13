"""TC03: Login User with incorrect email and password."""
import pytest

from pages.home_page import HomePage
from pages.login_page import LoginPage


@pytest.mark.tc03
@pytest.mark.regression
@pytest.mark.auth
def test_login_user_with_incorrect_credentials(driver, base_url):
    home_page = HomePage(driver)
    assert home_page.is_displayed(), "Home page should be visible"
    home_page.click_signup_login()
    login_page = LoginPage(driver)
    assert login_page.is_login_to_your_account_section_visible(), (
        '"Login to your account" section should be visible'
    )
    login_page.enter_login_credentials("wrong@example.com", "WrongPassword123!")
    login_page.click_login_button()
    assert login_page.is_login_incorrect_error_visible(), (
        '"Your email or password is incorrect!" message should be visible'
    )
