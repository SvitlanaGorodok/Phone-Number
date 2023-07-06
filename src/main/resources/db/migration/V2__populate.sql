INSERT INTO users (id, login, password) VALUES ('4e3c27be-76de-496a-bed2-fb2dcb71ab7a', 'admin', '$2a$12$nyhcs.Zi0QBKHNns3Qd08.3sNM9VCLSYYJYUxlpjti1WOXWcTauQ6');

INSERT INTO contacts (id, name, user_id)
VALUES ('072f378d-a7a8-44db-856a-4044668dfbe3', 'contact1', '4e3c27be-76de-496a-bed2-fb2dcb71ab7a'),
       ('e9072bc9-9402-45eb-8e9a-cc088692879c', 'contact2', '4e3c27be-76de-496a-bed2-fb2dcb71ab7a');

INSERT INTO emails (id, email)
VALUES ('5c3814d7-2d48-4ac3-92de-7a6fbe819240', 'email1@gmail.com'),
       ('e814e6c8-64f4-4c3a-97be-cde232852686', 'email2@gmail.com');

INSERT INTO phone_numbers (id, number)
VALUES ('4e720329-ce31-49bb-b713-0da9e6becab0', '+380985555555'),
       ('8ca38881-ca4e-4536-8a57-65cba5ef675b', '+380982222222');

INSERT INTO contacts_emails (contact_id, email_id)
VALUES ('072f378d-a7a8-44db-856a-4044668dfbe3', '5c3814d7-2d48-4ac3-92de-7a6fbe819240'),
       ('e9072bc9-9402-45eb-8e9a-cc088692879c', 'e814e6c8-64f4-4c3a-97be-cde232852686');

INSERT INTO contacts_numbers (contact_id, number_id)
VALUES ('072f378d-a7a8-44db-856a-4044668dfbe3', '4e720329-ce31-49bb-b713-0da9e6becab0'),
       ('e9072bc9-9402-45eb-8e9a-cc088692879c', '8ca38881-ca4e-4536-8a57-65cba5ef675b');



