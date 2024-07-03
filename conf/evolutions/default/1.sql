# --- First database schema

# --- !Ups

set ignorecase true;

create table author (
  id                        bigint AUTO_INCREMENT,
  name                      varchar(255) not null,
  constraint pk_author primary key (id))
;

create table book (
  id                        bigint AUTO_INCREMENT,
  title                      varchar(255) not null,
  subtitle                      varchar(255)  null,
  ISBN                          varchar(255) not null,
  published               timestamp,
  author_id                bigint,
  constraint pk_book primary key (id))
;

create sequence author_seq start with 1000;

create sequence book_seq start with 1000;

alter table book add constraint fk_book_author_1 foreign key (author_id) references author (id) on delete restrict on update restrict;
create index ix_book_author_1 on book (author_id);


# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists author;

drop table if exists book;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists author_seq;

drop sequence if exists book_seq;

