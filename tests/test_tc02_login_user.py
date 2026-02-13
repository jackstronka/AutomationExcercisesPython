"""TC02: Login User with correct email and password."""
import pytest

from pages.home_page import HomePage
from pages.login_page import LoginPage
from tests.base_test import ensure_registered_user_ready_for_login


@pytest.mark.tc02
@pytest.mark.regression
@pytest.mark.auth
@pytest.mark.smoke
def test_login_user_with_correct_credentials(driver, base_url):
    registered_email, registered_password = ensure_registered_user_ready_for_login(driver)

    home_page = HomePage(driver)
    home_page.click_signup_login()
    login_page = LoginPage(driver)
    assert login_page.is_login_to_your_account_section_visible(), (
        '"Login to your account" section should be visible'
    )
    login_page.enter_login_credentials(registered_email, registered_password)
    login_page.click_login_button()
    assert login_page.is_logged_in_as_user(), "User should be logged in"
    login_page.click_delete_account()
    assert login_page.is_account_deleted_message_visible(), "ACCOUNT DELETED! should be visible"
    login_page.click_continue_after_delete()
