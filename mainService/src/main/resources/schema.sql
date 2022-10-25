-- DROP TABLE events CASCADE;
-- DROP TABLE users CASCADE;
-- DROP TABLE participant_requests CASCADE;
-- DROP TABLE categories CASCADE;
-- DROP TABLE compilations CASCADE;
-- DROP TABLE compilation_event CASCADE;

CREATE TABLE IF NOT EXISTS users (
                                      id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                      name VARCHAR,
                                      email VARCHAR,
                                      CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events (
                                      id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                      owner_id BIGINT,
                                      title VARCHAR,
                                      annotation VARCHAR,
                                      category_id BIGINT,
                                      confirmed_requests BIGINT,
                                      created_on VARCHAR,
                                      location_id BIGINT,
                                      state VARCHAR,
                                      description VARCHAR,
                                      event_date_time TIMESTAMP,
                                      paid BOOLEAN,
                                      participant_limit BIGINT,
                                      published_on VARCHAR,
                                      request_moderation BOOLEAN,
                                      views BIGINT,
                                      CONSTRAINT pk_event PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS participant_requests (
                                      id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                      event_owner_id BIGINT,
                                      event BIGINT,
                                      requester BIGINT,
                                      create_on TIMESTAMP,
                                      status VARCHAR,
                                      CONSTRAINT pk_request PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories (
                                      id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                      name VARCHAR UNIQUE,
                                      CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS location (
                                      id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                      lat FLOAT,
                                      lon FlOAT,
                                      CONSTRAINT pk_location PRIMARY KEY (id)

);

CREATE TABLE IF NOT EXISTS compilations (
                                      id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                      events VARCHAR,
                                      title VARCHAR,
                                      pinned BOOLEAN,
                                      CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilation_event (
                                      compilation_id BIGINT,
                                      event_id BIGINT
);