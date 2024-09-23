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

CREATE TABLE app.recipient
(
id bigserial,
type_id bigint,
email character varying NOT NULL,
CONSTRAINT recipient_pk PRIMARY KEY (id),
CONSTRAINT recipient_type_fk  FOREIGN KEY (type_id) REFERENCES app.recipient_type(id),
CONSTRAINT email_unique  UNIQUE(email)
);

ALTER TABLE IF EXISTS app.recipient
    OWNER to postgres;


