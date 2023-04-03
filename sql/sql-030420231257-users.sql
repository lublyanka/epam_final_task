CREATE TABLE IF NOT EXISTS user_addresses (
     id SERIAL PRIMARY KEY,
     --ISO 3166-1 alpha-2 notation
     country char(2),
     state varchar(256),
     city varchar(256),
     code varchar(10),
     address1 varchar (100),
     address2 varchar (50),
     note varchar (150),
      FOREIGN KEY (country)
      REFERENCES dict_countries (alpha_2)

);

CREATE TABLE IF NOT EXISTS user_document_ids(
    id SERIAL PRIMARY KEY,
    number varchar (50) unique ,
    valid_from date,
    valid_to date,
    authority varchar (256)
);

CREATE TABLE IF NOT EXISTS users (
     id SERIAL PRIMARY KEY,
     login varchar(15) not null unique ,
     password char (256) not null ,
     email varchar (256) not null unique ,
     name varchar(256) not null ,
     surname varchar(256) not null ,
     middlename varchar (256),
     phone varchar (20),
     created_on TIMESTAMP not null ,
     update_on TIMESTAMP,
     last_login TIMESTAMP ,
     user_add_id integer ,
     user_doc_id integer,
      FOREIGN KEY (user_add_id)
      REFERENCES user_addresses (id),
      FOREIGN KEY (user_doc_id)
      REFERENCES user_document_ids (id)

);