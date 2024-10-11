CREATE SCHEMA app
    AUTHORIZATION postgres;

CREATE TABLE app.recipient_type
(
id bigserial,
type character varying(3) NOT NULL,
CONSTRAINT recipient_type_pk PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS app.recipient_type
    OWNER to postgres;

CREATE TABLE app.recipient_address
(
id uuid,
email character varying NOT NULL,
CONSTRAINT recipient_address_pk PRIMARY KEY (id),
CONSTRAINT email_unique  UNIQUE(email)
);

ALTER TABLE IF EXISTS app.recipient_address
    OWNER to postgres;

CREATE TABLE app.email
(
id uuid,
title character varying,
body_text character varying,
CONSTRAINT email_pk PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS app.email
    OWNER to postgres;

CREATE TABLE app.email_recipients
(
id uuid,
email_id uuid,
address_id uuid,
type_id bigint,
CONSTRAINT crs_e_addr_type_pk PRIMARY KEY (id),
CONSTRAINT crs_e_addr_type_e_fk  FOREIGN KEY (email_id) REFERENCES app.email(id),
CONSTRAINT crs_e_addr_type_addr_fk  FOREIGN KEY (address_id) REFERENCES app.recipient_address(id),
CONSTRAINT crs_e_addr_type_type_fk  FOREIGN KEY (type_id) REFERENCES app.recipient_type(id),
CONSTRAINT crs_e_addr_type_row_unq  UNIQUE (email_id,address_id, type_id)
);

ALTER TABLE IF EXISTS app.email_recipients
    OWNER to postgres;





