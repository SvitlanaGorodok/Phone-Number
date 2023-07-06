CREATE TABLE IF NOT EXISTS users
(
    id UUID CONSTRAINT user_pk PRIMARY KEY,
    login VARCHAR(50) NOT NULL,
    UNIQUE (login),
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS contacts
(
    id UUID CONSTRAINT contact_pk PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS emails
(
    id UUID CONSTRAINT email_pk PRIMARY KEY,
    email VARCHAR(50) NOT NULL,
    UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS phone_numbers
(
    id UUID CONSTRAINT phone_number_pk PRIMARY KEY,
    number VARCHAR(50) NOT NULL,
    UNIQUE (number)
);

CREATE TABLE IF NOT EXISTS contacts_emails
(
    contact_id UUID REFERENCES contacts (id) ON DELETE CASCADE,
    email_id UUID REFERENCES emails (id) ON DELETE CASCADE,
    CONSTRAINT contact_email_pk PRIMARY KEY (contact_id, email_id)
);

CREATE TABLE IF NOT EXISTS contacts_numbers
(
    contact_id UUID REFERENCES contacts (id) ON DELETE CASCADE,
    number_id UUID REFERENCES phone_numbers (id) ON DELETE CASCADE,
    CONSTRAINT contact_number_pk PRIMARY KEY (contact_id, number_id)
);

