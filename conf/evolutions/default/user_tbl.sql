-- Table: public.user_tbl

-- DROP TABLE public.user_tbl;

CREATE TABLE public.user_tbl
(
    user_id bigint NOT NULL DEFAULT nextval('mccp_user_tbl_user_id_seq'::regclass),
    user_email character varying(255) COLLATE "default".pg_catalog NOT NULL,
    user_first_name character varying(255) COLLATE "default".pg_catalog,
    user_last_name character varying(255) COLLATE "default".pg_catalog,
    user_role integer,
    user_password character varying(255) COLLATE "default".pg_catalog,
    user_confirm_password character varying(255) COLLATE "default".pg_catalog,
    CONSTRAINT mccp_user_tbl_pkey PRIMARY KEY (user_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.user_tbl
    OWNER to postgres;

