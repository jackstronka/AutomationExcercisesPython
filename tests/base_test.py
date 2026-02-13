"""Shared flow helpers – registration, checkout, ensure signup visible. Used by test modules."""
from pages.account_created_page import AccountCreatedPage
from pages.home_page import HomePage
from pages.login_page import LoginPage
from pages.signup_page import SignupPage
from testdata.models import AccountInfo, Address
from testdata.test_data_factory import (
    default_account_info,
    default_address,
    generate_unique_email,
)


def ensure_registered_user_ready_for_login(driver) -> tuple[str, str]:
    """Register a new user and logout. Returns (registered_email, registered_password)."""
    account = default_account_info()
    address = default_address()
    name = "Login User"
    registered_password = account.password
    registered_email = generate_unique_email("loginuser@example.com")
    _complete_registration_and_logout(driver, name, registered_email, account, address)
    return registered_email, registered_password


def ensure_user_exists_with_email(driver, email: str) -> None:
    """Ensure a user with the given email exists (register if needed), then navigate to home."""
    _ensure_user_exists_and_navigate_to_home(
        driver, email, "Test User", default_account_info(), default_address()
    )


def _complete_registration_and_logout(
    driver, name: str, email: str, account: AccountInfo, address: Address
) -> None:
    home = HomePage(driver)
    home.click_signup_login()
    login = LoginPage(driver)
    login.enter_new_user_name_and_email(name, email)
    login.click_signup_button()
    signup = SignupPage(driver)
    _complete_signup_and_logout(driver, signup, account, address)


def _ensure_user_exists_and_navigate_to_home(
    driver, email: str, name: str, account: AccountInfo, address: Address
) -> None:
    home = HomePage(driver)
    home.click_signup_login()
    login = LoginPage(driver)
    login.enter_new_user_name_and_email(name, email)
    login.click_signup_button()
    signup = SignupPage(driver)
    if signup.is_enter_account_information_section_visible():
        _complete_signup_and_logout(driver, signup, account, address)
    home = HomePage(driver)
    home.navigate_to_home()


def _complete_signup_and_logout(driver, signup: SignupPage, account: AccountInfo, address: Address) -> None:
    signup.fill_account_information(
        account.password, account.title, account.day, account.month, account.year
    )
    signup.subscribe_to_newsletter()
    signup.agree_to_special_offers()
    signup.fill_address_details(
        address.first_name, address.last_name, address.street_address,
        address.country, address.state, address.city, address.zipcode, address.mobile
    )
    signup.click_create_account()
    account_created = AccountCreatedPage(driver)
    account_created.click_continue()
    login = LoginPage(driver)
    login.click_logout()


def ensure_enter_account_information_visible(
    driver, login_page: LoginPage, signup_page: SignupPage | None,
    name: str, base_email: str
) -> SignupPage | None:
    """After entering name/email and clicking Signup, retry with unique email if 'Email already exists'.
    Returns SignupPage when ENTER ACCOUNT INFORMATION section is visible."""
    max_retries = 3
    for _ in range(max_retries):
        if signup_page and signup_page.is_enter_account_information_section_visible():
            return signup_page
        if login_page and login_page.is_email_already_exists_error_visible():
            unique_email = generate_unique_email(base_email)
            login_page.enter_new_user_name_and_email(name, unique_email)
            login_page.click_signup_button()
            signup_page = SignupPage(driver)
        else:
            break
    return signup_page
