CREATE TABLE keys (
    id bigint NOT NULL,
    name character varying(255),
    type character varying(255),
    feature_set_id bigint
);


CREATE TABLE anchors (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    labels text,
    max_age bigint,
    name character varying(255) NOT NULL,
    status character varying(255),
    project_name character varying(255),
    source character varying(255)
);


CREATE TABLE features (
    id bigint NOT NULL,
    archived boolean NOT NULL,
    bool_domain bytea,
    domain character varying(255),
    float_domain bytea,
    group_presence bytea,
    image_domain bytea,
    int_domain bytea,
    labels text,
    mid_domain bytea,
    name character varying(255),
    natural_language_domain bytea,
    presence bytea,
    shape bytea,
    string_domain bytea,
    struct_domain bytea,
    time_domain bytea,
    time_of_day_domain bytea,
    type character varying(255),
    url_domain bytea,
    value_count bytea,
    feature_set_id bigint
);


CREATE TABLE derived_features (
    id character varying(255) NOT NULL,
    created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    ext_id character varying(255),
    runner character varying(255),
    status character varying(16),
    source_id character varying(255),
    store_name character varying(255)
);



CREATE TABLE projects (
    name character varying(255) NOT NULL,
    archived boolean NOT NULL
);


CREATE TABLE sources (
    id character varying(255) NOT NULL,
    path character varying(255),
    event_timestamp_column boolean,
    timestamp_format character varying(255),
    type character varying(255) NOT NULL
    preprocessing
    tags
);