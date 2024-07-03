# --- First database schema

# --- !Ups

CREATE TABLE author (
                        id                        BIGINT AUTO_INCREMENT,
                        name                      VARCHAR(255) NOT NULL,
                        CONSTRAINT pk_author PRIMARY KEY (id)
);

CREATE TABLE book (
                      id                        BIGINT AUTO_INCREMENT,
                      title                     VARCHAR(255) NOT NULL,
                      subtitle                  VARCHAR(255) NULL,
                      ISBN                      VARCHAR(255) NOT NULL,
                      published                 TIMESTAMP,
                      author_id                 BIGINT,
                      CONSTRAINT pk_book PRIMARY KEY (id)
);

ALTER TABLE book ADD CONSTRAINT fk_book_author_1 FOREIGN KEY (author_id) REFERENCES author (id) ON DELETE RESTRICT ON UPDATE RESTRICT;
CREATE INDEX ix_book_author_1 ON book (author_id);

# --- !Downs

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS author;

SET FOREIGN_KEY_CHECKS = 1;
