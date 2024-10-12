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


CREATE TABLE app.email_status
(
id bigserial,
name character varying NOT NULL,
CONSTRAINT email_status_pk PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS app.email_status
    OWNER to postgres;

CREATE TABLE app.email
(
id uuid,
title character varying,
body_text character varying,
status_id bigint,
created_at TIMESTAMP NOT NULL,
updated_at TIMESTAMP NOT NULL,
CONSTRAINT email_pk PRIMARY KEY (id),
CONSTRAINT email_email_status_fk  FOREIGN KEY (status_id) REFERENCES app.email_status(id)
);

ALTER TABLE IF EXISTS app.email
    OWNER to postgres;

CREATE TABLE app.email_recipients
(
id uuid,
email_id uuid,
address_id uuid,
type_id bigint,
CONSTRAINT recipients_pk PRIMARY KEY (id),
CONSTRAINT recipients_email_fk  FOREIGN KEY (email_id) REFERENCES app.email(id),
CONSTRAINT recipients_rec_address_fk  FOREIGN KEY (address_id) REFERENCES app.recipient_address(id),
CONSTRAINT recipients_rec_type_fk  FOREIGN KEY (type_id) REFERENCES app.recipient_type(id),
CONSTRAINT  recipients_row_unq  UNIQUE (email_id,address_id, type_id)
);

ALTER TABLE IF EXISTS app.email_recipients
    OWNER to postgres;