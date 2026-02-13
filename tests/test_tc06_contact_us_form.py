"""TC06: Contact Us Form."""
from pathlib import Path

import pytest

from pages.contact_us_page import ContactUsPage
from pages.home_page import HomePage
from testdata.test_data_factory import default_contact_form


def _get_upload_file_path() -> str:
    """Resolve path to resources/testdata/upload.txt (project root)."""
    root = Path(__file__).resolve().parent.parent
    return str((root / "resources" / "testdata" / "upload.txt").resolve())


@pytest.mark.tc06
@pytest.mark.regression
@pytest.mark.contact
def test_contact_us_form(driver, base_url):
    home_page = HomePage(driver)
    assert home_page.is_displayed(), "Home page should be visible"
    home_page.click_contact_us()
    contact_us_page = ContactUsPage(driver)
    assert contact_us_page.is_get_in_touch_visible(), '"GET IN TOUCH" section should be visible'

    contact = default_contact_form()
    contact_us_page.fill_contact_form(contact.name, contact.email, contact.subject, contact.message)
    contact_us_page.upload_file(_get_upload_file_path())
    contact_us_page.click_submit()
    contact_us_page.accept_alert_if_present()
    assert contact_us_page.is_success_message_visible(), (
        "Contact Us success message should be visible"
    )
    contact_us_page.click_home()
    home_page = HomePage(driver)
    assert home_page.is_displayed(), "Home page should be visible"
