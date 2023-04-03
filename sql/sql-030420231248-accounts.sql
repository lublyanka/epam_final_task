CREATE TABLE accounts
(
    id          uuid PRIMARY KEY,
    card_number varchar(19),
    card_type   char(02),
    month       char(2),
    year        char(4),
    currency    char(3),
    created_on  timestamp,
    updated_on  timestamp,
    FOREIGN KEY (card_type)
        REFERENCES dict_card_types (code),
    FOREIGN KEY (currency)
        REFERENCES dict_currencies (code)
)
;

CREATE TABLE user_accounts
(
    user_id    integer,
    account_id uuid,
    FOREIGN KEY (user_id)
        REFERENCES users (id),
    FOREIGN KEY (account_id) REFERENCES accounts (id)

);

Alter TABLE user_accounts
    add primary key (user_id, account_id);

CREATE TABLE invoices(id uuid,
doc oid
);

CREATE TABLE payments
(
    id         uuid PRIMARY KEY,
    account_id uuid,
    sum        decimal(10, 2),
    currency   char(3),
    recipient  varchar(256),
    invoice_id uuid,
    FOREIGN KEY (currency) REFERENCES dict_currencies (code)
);

ALTER TABLE payments add FOREIGN KEY (invoice_id) REFERENCES invoices (id);