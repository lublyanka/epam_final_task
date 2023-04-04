CREATE TABLE IF NOT EXISTS dict_countries (
  alpha_2 CHAR(2) PRIMARY KEY,
  name VARCHAR(50) NOT NULL
);

INSERT INTO dict_countries (alpha_2, name) VALUES ('AF', 'Afghanistan') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('AX', 'Åland Islands') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('AL', 'Albania') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('DZ', 'Algeria') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('AS', 'American Samoa') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('AD', 'Andorra') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('AO', 'Angola') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('AI', 'Anguilla') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('AQ', 'Antarctica') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('AG', 'Antigua and Barbuda') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('AR', 'Argentina') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('AM', 'Armenia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('AW', 'Aruba') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('AU', 'Australia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('AT', 'Austria') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('AZ', 'Azerbaijan') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BS', 'Bahamas') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BH', 'Bahrain') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BD', 'Bangladesh') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BB', 'Barbados') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BY', 'Belarus') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BE', 'Belgium') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BZ', 'Belize') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BJ', 'Benin') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BM', 'Bermuda') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BT', 'Bhutan') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BO', 'Bolivia (Plurinational State of)') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BQ', 'Bonaire, Sint Eustatius and Saba') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BA', 'Bosnia and Herzegovina') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BW', 'Botswana') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BV', 'Bouvet Island') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BR', 'Brazil') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('IO', 'British Indian Ocean Territory') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BN', 'Brunei Darussalam') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BG', 'Bulgaria') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BF', 'Burkina Faso') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BI', 'Burundi') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CV', 'Cabo Verde') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('KH', 'Cambodia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CM', 'Cameroon') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CA', 'Canada') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('KY', 'Cayman Islands') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CF', 'Central African Republic') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('TD', 'Chad') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CL', 'Chile') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CN', 'China') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CX', 'Christmas Island') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CC', 'Cocos (Keeling) Islands') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CO', 'Colombia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('KM', 'Comoros') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CG', 'Congo') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CD', 'Congo, Democratic Republic of the') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CK', 'Cook Islands') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CR', 'Costa Rica') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CI', 'Côte d''Ivoire') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('HR', 'Croatia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CU', 'Cuba') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CW', 'Curaçao') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CY', 'Cyprus') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CZ', 'Czechia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('DK', 'Denmark') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('DJ', 'Djibouti') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('DM', 'Dominica') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('DO', 'Dominican Republic') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('EC', 'Ecuador') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('EG', 'Egypt') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SV', 'El Salvador') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GQ', 'Equatorial Guinea') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('ER', 'Eritrea') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('EE', 'Estonia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('ET', 'Ethiopia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('FK', 'Falkland Islands (Malvinas)') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('FO', 'Faroe Islands') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('FJ', 'Fiji') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('FI', 'Finland') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('FR', 'France') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GF', 'French Guiana') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('PF', 'French Polynesia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('TF', 'French Southern Territories') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GA', 'Gabon') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GM', 'Gambia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GE', 'Georgia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('DE', 'Germany') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GH', 'Ghana') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GI', 'Gibraltar') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GR', 'Greece') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GL', 'Greenland') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GD', 'Grenada') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GP', 'Guadeloupe') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GU', 'Guam') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GT', 'Guatemala') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GG', 'Guernsey') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GN', 'Guinea') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GW', 'Guinea-Bissau') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GY', 'Guyana') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('HT', 'Haiti') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('HM', 'Heard Island and McDonald Islands') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('VA', 'Holy See') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('HN', 'Honduras') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('HK', 'Hong Kong') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('HU', 'Hungary') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('IS', 'Iceland') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('IN', 'India') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('ID', 'Indonesia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('IR', 'Iran, Islamic Republic of') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('IQ', 'Iraq') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('IE', 'Ireland') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('IM', 'Isle of Man') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('IL', 'Israel') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('IT', 'Italy') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('JM', 'Jamaica') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('JP', 'Japan') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('JE', 'Jersey') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('JO', 'Jordan') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('KZ', 'Kazakhstan') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('KE', 'Kenya') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('KI', 'Kiribati') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('KP', 'Korea, Democratic People''s Republic of') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('KR', 'Korea, Republic of') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('KW', 'Kuwait') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('KG', 'Kyrgyzstan') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('LA', 'Lao People''s Democratic Republic') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('LV', 'Latvia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('LB', 'Lebanon') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('LS', 'Lesotho') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('LR', 'Liberia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('LY', 'Libya') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('LI', 'Liechtenstein') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('LT', 'Lithuania') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('LU', 'Luxembourg') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MO', 'Macao') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MK', 'Macedonia, the former Yugoslav Republic of') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MG', 'Madagascar') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MW', 'Malawi') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MY', 'Malaysia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MV', 'Maldives') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('ML', 'Mali') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MT', 'Malta') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MH', 'Marshall Islands') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MQ', 'Martinique') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MR', 'Mauritania') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MU', 'Mauritius') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('YT', 'Mayotte') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MX', 'Mexico') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('FM', 'Micronesia, Federated States of') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MD', 'Moldova, Republic of') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MC', 'Monaco') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MN', 'Mongolia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('ME', 'Montenegro') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MS', 'Montserrat') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MA', 'Morocco') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MZ', 'Mozambique') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MM', 'Myanmar') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('NA', 'Namibia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('NR', 'Nauru') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('NP', 'Nepal') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('NL', 'Netherlands') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('NC', 'New Caledonia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('NZ', 'New Zealand') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('NI', 'Nicaragua') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('NE', 'Niger') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('NG', 'Nigeria') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('NU', 'Niue') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('NF', 'Norfolk Island') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MP', 'Northern Mariana Islands') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('NO', 'Norway') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('OM', 'Oman') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('PK', 'Pakistan') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('PW', 'Palau') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('PS', 'Palestine, State of') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('PA', 'Panama') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('PG', 'Papua New Guinea') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('PY', 'Paraguay') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('PE', 'Peru') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('PH', 'Philippines') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('PN', 'Pitcairn') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('PL', 'Poland') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('PT', 'Portugal') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('PR', 'Puerto Rico') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('QA', 'Qatar') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('RE', 'Réunion') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('RO', 'Romania') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('RU', 'Russian Federation') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('RW', 'Rwanda') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('BL', 'Saint Barthélemy') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SH', 'Saint Helena, Ascension and Tristan da Cunha') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('KN', 'Saint Kitts and Nevis') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('LC', 'Saint Lucia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('MF', 'Saint Martin (French part)') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('PM', 'Saint Pierre and Miquelon') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('VC', 'Saint Vincent and the Grenadines') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('WS', 'Samoa') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SM', 'San Marino') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('ST', 'Sao Tome and Principe') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SA', 'Saudi Arabia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SN', 'Senegal') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('RS', 'Serbia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SC', 'Seychelles') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SL', 'Sierra Leone') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SG', 'Singapore') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SX', 'Sint Maarten (Dutch part)') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SK', 'Slovakia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SI', 'Slovenia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SB', 'Solomon Islands') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SO', 'Somalia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('ZA', 'South Africa') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GS', 'South Georgia and the South Sandwich Islands') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SS', 'South Sudan') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('ES', 'Spain') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('LK', 'Sri Lanka') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SD', 'Sudan') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SR', 'Suriname') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SJ', 'Svalbard and Jan Mayen') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SZ', 'Swaziland') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SE', 'Sweden') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('CH', 'Switzerland') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('SY', 'Syrian Arab Republic') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('TW', 'Taiwan, Province of China') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('TJ', 'Tajikistan') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('TZ', 'Tanzania, United Republic of') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('TH', 'Thailand') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('TL', 'Timor-Leste') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('TG', 'Togo') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('TK', 'Tokelau') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('TO', 'Tonga') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('TT', 'Trinidad and Tobago') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('TN', 'Tunisia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('TR', 'Turkey') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('TM', 'Turkmenistan') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('TC', 'Turks and Caicos Islands') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('TV', 'Tuvalu') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('UG', 'Uganda') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('UA', 'Ukraine') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('AE', 'United Arab Emirates') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('GB', 'United Kingdom') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('US', 'United States') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('UM', 'United States Minor Outlying Islands') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('UY', 'Uruguay') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('UZ', 'Uzbekistan') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('VU', 'Vanuatu') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('VE', 'Venezuela, Bolivarian Republic of') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('VN', 'Viet Nam') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('VG', 'Virgin Islands, British') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('VI', 'Virgin Islands, U.S.') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('WF', 'Wallis and Futuna') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('EH', 'Western Sahara') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('YE', 'Yemen') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('ZM', 'Zambia') ON CONFLICT DO NOTHING;
INSERT INTO dict_countries (alpha_2, name) VALUES ('ZW', 'Zimbabwe') ON CONFLICT DO NOTHING;