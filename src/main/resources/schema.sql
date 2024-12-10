CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  email VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  description VARCHAR(500) NOT NULL,
  requester BIGINT NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP, --DEFAULT CURRENT_TIMESTAMP
  CONSTRAINT pk_request PRIMARY KEY (id),
  CONSTRAINT fk_request_user FOREIGN KEY (requester) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  available BOOLEAN NOT NULL DEFAULT FALSE,
  owner BIGINT DEFAULT NULL,
  request BIGINT DEFAULT NULL,
  CONSTRAINT pk_item PRIMARY KEY (id),
  CONSTRAINT fk_item_owner FOREIGN KEY (owner) REFERENCES users(id),
  CONSTRAINT fk_item_request FOREIGN KEY (request) REFERENCES requests(id)
);

CREATE TABLE IF NOT EXISTS bookings (
 id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
 time_start TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 time_end TIMESTAMP WITHOUT TIME ZONE NOT NULL,
 item BIGINT NOT NULL,
 booker BIGINT NOT NULL,
 status VARCHAR(25) NOT NULL,
 CONSTRAINT pk_booking PRIMARY KEY (id),
 CONSTRAINT fk_booking_item FOREIGN KEY (item) REFERENCES items(id),
 CONSTRAINT fk_booking_booker FOREIGN KEY (booker) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS comments (
 id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
 text VARCHAR(1000) NOT NULL,
 item BIGINT NOT NULL,
 author BIGINT NOT NULL,
 comment_time TIMESTAMP WITHOUT TIME ZONE,
 CONSTRAINT pk_comment PRIMARY KEY (id),
 CONSTRAINT fk_comment_item FOREIGN KEY (item) REFERENCES items(id),
 CONSTRAINT fk_comment_author FOREIGN KEY (author) REFERENCES users(id)
);
