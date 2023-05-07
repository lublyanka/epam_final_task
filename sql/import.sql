

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