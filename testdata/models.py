"""Test data models – dataclasses for account, address, payment, contact, signup."""
from dataclasses import dataclass


@dataclass(frozen=True)
class AccountInfo:
    password: str
    title: str
    day: str
    month: str
    year: str


@dataclass(frozen=True)
class Address:
    first_name: str
    last_name: str
    street_address: str
    country: str
    state: str
    city: str
    zipcode: str
    mobile: str


@dataclass(frozen=True)
class ContactFormData:
    name: str
    email: str
    subject: str
    message: str


@dataclass(frozen=True)
class PaymentDetails:
    name_on_card: str
    card_number: str
    cvc: str
    expiry_month: str
    expiry_year: str


@dataclass(frozen=True)
class SignupUser:
    name: str
    email: str
