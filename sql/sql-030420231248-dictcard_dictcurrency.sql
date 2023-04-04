CREATE TABLE dict_card_types
(
    code   char(2) primary key,
    letter char(2) unique,
    name   varchar(50)
);

INSERT INTO dict_card_types (code, letter, name)
VALUES ('03', 'A', 'American Express (AmEx)') ON CONFLICT DO NOTHING;
INSERT INTO dict_card_types (code, letter, name)
VALUES ('02', 'V', 'Visa') ON CONFLICT DO NOTHING;
INSERT INTO dict_card_types (code, letter, name)
VALUES ('01', 'M', 'MasterCard (Master)') ON CONFLICT DO NOTHING;
INSERT INTO dict_card_types (code, letter, name)
VALUES ('10', 'DS', 'Discover') ON CONFLICT DO NOTHING;
INSERT INTO dict_card_types (code, letter, name)
VALUES ('28', 'J', 'JCB') ON CONFLICT DO NOTHING;
INSERT INTO dict_card_types (code, letter, name)
VALUES ('20', 'E', 'enRoute') ON CONFLICT DO NOTHING;
INSERT INTO dict_card_types (code, letter, name)
VALUES ('04', 'DN', 'Diners Club (Diners)') ON CONFLICT DO NOTHING;
INSERT INTO dict_card_types (code, letter, name)
VALUES ('72', 'GB', 'GSB') ON CONFLICT DO NOTHING;

CREATE TABLE dict_currencies
(
    --ISO 4217 notation
    code CHAR(3) PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

INSERT INTO dict_currencies (code, name)
VALUES ('USD', 'United States dollar');
INSERT INTO dict_currencies (code, name)
VALUES ('EUR', 'Euro');
INSERT INTO dict_currencies (code, name)
VALUES ('JPY', 'Japanese yen');
INSERT INTO dict_currencies (code, name)
VALUES ('GBP', 'Pound sterling');
INSERT INTO dict_currencies (code, name)
VALUES ('CHF', 'Swiss franc');
INSERT INTO dict_currencies (code, name)
VALUES ('CAD', 'Canadian dollar');
INSERT INTO dict_currencies (code, name)
VALUES ('AUD', 'Australian dollar');
INSERT INTO dict_currencies (code, name)
VALUES ('CNY', 'Chinese yuan');
INSERT INTO dict_currencies (code, name)
VALUES ('NZD', 'New Zealand dollar');
INSERT INTO dict_currencies (code, name)
VALUES ('MXN', 'Mexican peso');
INSERT INTO dict_currencies (code, name)
VALUES ('SGD', 'Singapore dollar');
INSERT INTO dict_currencies (code, name)
VALUES ('HKD', 'Hong Kong dollar');
INSERT INTO dict_currencies (code, name)
VALUES ('NOK', 'Norwegian krone');
INSERT INTO dict_currencies (code, name)
VALUES ('KRW', 'South Korean won');
INSERT INTO dict_currencies (code, name)
VALUES ('SEK', 'Swedish krona');
INSERT INTO dict_currencies (code, name)
VALUES ('RUB', 'Russian Ruble');
INSERT INTO dict_currencies (code, name)
VALUES ('BRL', 'Brazilian real');
INSERT INTO dict_currencies (code, name)
VALUES ('INR', 'Indian rupee');
INSERT INTO dict_currencies (code, name)
VALUES ('TRY', 'Turkish lira');
INSERT INTO dict_currencies (code, name)
VALUES ('ZAR', 'South African rand');
INSERT INTO dict_currencies (code, name)
VALUES ('PLN', 'Polish złoty');
INSERT INTO dict_currencies (code, name)
VALUES ('THB', 'Thai baht');
INSERT INTO dict_currencies (code, name)
VALUES ('IDR', 'Indonesian rupiah');
INSERT INTO dict_currencies (code, name)
VALUES ('MYR', 'Malaysian ringgit');
INSERT INTO dict_currencies (code, name)
VALUES ('PHP', 'Philippine peso');


