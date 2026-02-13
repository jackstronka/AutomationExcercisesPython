"""Central factory for test data – defaults and unique email generation."""
import time

from testdata.models import AccountInfo, Address, ContactFormData, PaymentDetails, SignupUser


DEFAULT_ORDER_COMMENT = "Please deliver ASAP"


def default_account_info() -> AccountInfo:
    return AccountInfo("Test123!", "Mr", "15", "6", "1990")


def default_address() -> Address:
    return Address(
        "Jan", "Kowalski", "ul. Test 1", "India",
        "Mazowieckie", "Warsaw", "00-001", "123456789"
    )


def default_payment_details() -> PaymentDetails:
    return PaymentDetails("Jan Kowalski", "4111111111111111", "123", "12", "2030")


def default_contact_form() -> ContactFormData:
    return ContactFormData(
        "Jan Kowalski", "jan@example.com", "Test subject", "This is a test message."
    )


def generate_unique_email(base_email: str) -> str:
    """Generate unique email from base (e.g. for retry when email already exists)."""
    if not base_email:
        local_part = ""
        domain = "@example.com"
    elif "@" in base_email:
        idx = base_email.index("@")
        local_part = base_email[:idx]
        domain = base_email[idx:]
    else:
        local_part = base_email
        domain = "@example.com"
    return f"{local_part}+{int(time.time() * 1000)}{domain}"


# Parameterized test data (equivalent to DataProviders)
def register_user_data():
    return [SignupUser("Test User", "testuser3@example.com")]


def existing_email_data():
    return [SignupUser("Test User", "testuser@example.com")]


def checkout_user_data():
    return [SignupUser("CheckoutUser", "checkout_user@example.com")]


def invoice_user_data():
    return [SignupUser("InvoiceUser", "invoice_user@example.com")]
