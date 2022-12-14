-- DROP TABLE events CASCADE;
-- DROP TABLE users CASCADE;
-- DROP TABLE participant_requests CASCADE;
-- DROP TABLE categories CASCADE;
-- DROP TABLE compilations CASCADE;
-- DROP TABLE compilation_event CASCADE;

CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                     name VARCHAR(50) NOT NULL,
                                     email VARCHAR(50) UNIQUE NOT NULL,
                                     CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories (
                                          id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                          name VARCHAR(50) UNIQUE NOT NULL,
                                          CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS locations (
                                         id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                         lat FLOAT NOT NULL,
                                         lon FlOAT NOT NULL,
                                         CONSTRAINT pk_location PRIMARY KEY (id)

);

CREATE TABLE IF NOT EXISTS events (
                                      id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                      owner BIGINT NOT NULL,
                                      title VARCHAR(100) unique NOT NULL,
                                      annotation VARCHAR(500) unique NOT NULL,
                                      category BIGINT NOT NULL,
                                      confirmed_requests BIGINT,
                                      created_on TIMESTAMP NOT NULL,
                                      location BIGINT NOT NULL,
                                      state VARCHAR(9) NOT NULL,
                                      description VARCHAR(2000) unique NOT NULL,
                                      event_date TIMESTAMP NOT NULL,
                                      paid BOOLEAN NOT NULL,
                                      participant_limit BIGINT,
                                      published_on TIMESTAMP,
                                      request_moderation BOOLEAN,
                                      views BIGINT,
                                      CONSTRAINT pk_event PRIMARY KEY (id),
                                      CONSTRAINT owner_of_event FOREIGN KEY(owner) REFERENCES users(id),
                                      CONSTRAINT category_of_event FOREIGN KEY(category) REFERENCES categories(id),
                                      CONSTRAINT location_of_event FOREIGN KEY(location) REFERENCES locations(id)
);

CREATE TABLE IF NOT EXISTS participant_requests (
                                                    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                                    event_owner BIGINT  NOT NULL,
                                                    event BIGINT NOT NULL,
                                                    requester BIGINT NOT NULL,
                                                    created_on TIMESTAMP NOT NULL,
                                                    status VARCHAR(9) NOT NULL,
                                                    CONSTRAINT pk_request PRIMARY KEY (id),
                                                    CONSTRAINT event_of_request FOREIGN KEY(event) REFERENCES events(id),
                                                    CONSTRAINT event_owner FOREIGN KEY(event_owner) REFERENCES users(id),
                                                    CONSTRAINT requester_of_request FOREIGN KEY(requester) REFERENCES users(id)
);




CREATE TABLE IF NOT EXISTS compilations (
                                            id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                            title VARCHAR(100) NOT NULL,
                                            pinned BOOLEAN NOT NULL,
                                            CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilation_event (
                                                 compilation_id BIGINT NOT NULL,
                                                 event_id BIGINT NOT NULL,
                                                 CONSTRAINT fk_compilation FOREIGN KEY (compilation_id) REFERENCES compilations(id),
                                                 CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events(id)
);

CREATE TABLE IF NOT EXISTS reviews (
                                       id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                       author BIGINT  NOT NULL,
                                       event BIGINT NOT NULL,
                                       text VARCHAR(1000) NOT NULL ,
                                       created_on TIMESTAMP NOT NULL ,
                                       event_rating INTEGER NOT NULL ,
                                       review_rating FLOAT NOT NULL ,
                                       counter BIGINT,
                                       state VARCHAR(9) NOT NULL,
                                       CONSTRAINT pk_reviews PRIMARY KEY (id),
                                       CONSTRAINT author_of_review FOREIGN KEY(author) REFERENCES users(id),
                                       CONSTRAINT event_of_review FOREIGN KEY(event) REFERENCES events(id)
);
