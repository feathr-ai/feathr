# This file will generate sample data for Feathr use cases. It's targeting a product recommendation use case


# To use this file, you need to do `pip install faker_commerce faker`

import random

import faker_commerce
import pandas as pd
from faker import Faker

# Define the number of products to generate
NUM_PRODUCTS = 500
# Define the number of purchases to generate
NUM_PURCHASES = 200000
# Define the number of rows in the DataFrame
NUM_CUSTOMERS = 1000
# Define the number of observation data in the DataFrame
NUM_OBSERVATION = 20

# Initialize Faker generator
fake = Faker()
fake.add_provider(faker_commerce.Provider)


# Define the list of products
products = []

# Generate fake products and add them to the list
for i in range(NUM_PRODUCTS):
    product_id = i + 10000
    product_name = fake.ecommerce_name()
    category = fake.ecommerce_category()
    price = round(random.uniform(1, 1000), 2)
    quantity = random.randint(1, 100)
    recent_sold = random.randint(0, 50)
    made_in_state = fake.state_abbr()
    discount = round(random.uniform(0, 0.5), 2)

    product = {
        "product_id": product_id,
        "product_name": product_name,
        "category": category,
        "price": price,
        "quantity": quantity,
        "recent_sold": recent_sold,
        "made_in_state": made_in_state,
        "discount": discount,
    }

    products.append(product)


products_df = pd.DataFrame(products)


# Create an empty DataFrame with the specified columns
customers = []
# Generate fake data using the profile API and add it to the DataFrame
for i in range(NUM_CUSTOMERS):
    profile = fake.profile()
    name = profile["name"]
    username = profile["username"]
    email = profile["mail"]
    job_title = profile["job"]  # fix here
    company = profile["company"]
    address = profile["address"]
    phone = fake.phone_number()
    user_id = i
    gender = profile["sex"]
    age = random.randint(18, 80)
    gift_card_balance = round(random.uniform(0, 1000), 2)
    number_of_credit_cards = random.randint(1, 5)
    state = fake.state_abbr()
    tax_rate = round(random.uniform(0.05, 0.15), 3)

    customers.append(
        {
            "name": name,
            "username": username,
            "email": email,
            "job_title": job_title,
            "company": company,
            "address": address,
            "phone": phone,
            "user_id": user_id,
            "gender": gender,
            "age": age,
            "gift_card_balance": gift_card_balance,
            "number_of_credit_cards": number_of_credit_cards,
            "state": state,
            "tax_rate": tax_rate,
        }
    )

# Create a Pandas DataFrame from the list of purchases
customers_df = pd.DataFrame(customers)


# Define the list of purchases
purchases = []

# Generate fake purchase data and add it to the list
for i in range(NUM_PURCHASES):
    user_id = random.choice(customers)["user_id"]
    purchase_date = fake.date_between(start_date="-1y", end_date="today")
    purchase_amount = round(random.uniform(10, 500), 2)
    product_id = random.choice(products)["product_id"]
    transaction_id = fake.uuid4()
    price = round(random.uniform(1, 100), 2)
    discounts = round(random.uniform(0, 20), 2)
    taxes_and_fees = round(random.uniform(0, 10), 2)
    total_cost = (price * quantity) - discounts + taxes_and_fees
    payment_method = random.choice(["Credit Card", "PayPal", "Apple Pay", "Google Wallet"])
    shipping_address = fake.address()
    status = random.choice(["Pending", "Complete", "Refunded"])
    notes = fake.sentence()
    purchase_quantity = random.randint(1, 100)

    purchase = {
        "user_id": user_id,
        "purchase_date": purchase_date,
        "purchase_amount": purchase_amount,
        "product_id": product_id,
        "purchase_quantity": purchase_quantity,
        "transaction_id": transaction_id,
        "price": price,
        "discounts": discounts,
        "taxes_and_fees": taxes_and_fees,
        "total_cost": total_cost,
        "payment_method": payment_method,
        "shipping_address": shipping_address,
        "status": status,
        "notes": notes,
    }

    purchases.append(purchase)

# Create a Pandas DataFrame from the list of purchases
purchase_df = pd.DataFrame(purchases)


# Define the list of observations
observations = []

# Generate fake user observation data and add it to the list
for i in range(NUM_OBSERVATION):
    user_id = random.choice(customers)["user_id"]
    purchase_date = fake.date_between(start_date="-1y", end_date="today")
    product_id = random.choice(products)["product_id"]
    browser = fake.user_agent()

    observation = {"user_id": user_id, "purchase_date": purchase_date, "product_id": product_id, "browser": browser}

    observations.append(observation)

# Create a Pandas DataFrame from the list of observations
observation_df = pd.DataFrame(observations)


# Print the DataFrame
print(products_df)
print(customers_df)
print(purchase_df)
print(observation_df)

# Save the products DataFrame to a CSV file
products_df.to_csv("product_detail_mock_data.csv", index=False)

# Save the purchases DataFrame to a CSV file
purchase_df.to_csv("user_purchase_history_mock_data.csv", index=False)

# Save the purchases DataFrame to a CSV file
customers_df.to_csv("user_profile_mock_data.csv", index=False)

# Save the observation_df DataFrame to a CSV file
observation_df.to_csv("user_observation_mock_data.csv", index=False)
