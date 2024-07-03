# --- Sample dataset

# --- !Ups

insert into author (id,name) values (  1,'Isaac Asimov');
insert into author (id,name) values (  2,'Ursula K. Le Guin');
insert into author (id,name) values (  3,'Arthur C. Clarke');
insert into author (id,name) values (  4,'Jules Verne');
insert into author (id,name) values (  5,'William Gibson');
insert into author (id,name) values (  6,'Liu Cixin');
insert into author (id,name) values (  7,'Dan Simmons');



insert into book (id,title, subtitle ,ISBN, published, author_id) values ( 1,'Foundation', 'first book in the Foundation Trilogy', '0-8108-5420-1', '2006-01-10',1);
insert into book (id,title, subtitle ,ISBN, published, author_id) values (  2,'I, Robot',null,'0-8108-5420-2','2006-01-10', 1);
insert into book (id,title, subtitle ,ISBN, published, author_id) values (  3,'A Wizard of Earthsea','fantasy novel','0-8108-5420-3','2006-01-10', 2);
insert into book (id,title, subtitle ,ISBN, published, author_id) values (  4,'2001: A Space Odyssey',null,'0-8108-5420-4','2006-01-10', 3);
insert into book (id,title, subtitle ,ISBN, published, author_id) values (  5,'2010: Odyssey Two',' subtitle 1991-01-01','0-8108-5420-5','1991-01-01', 3);
insert into book (id,title, subtitle ,ISBN, published, author_id) values (  6,'2061: Odyssey Three', null, '0-8108-5420-6','1991-01-01', 3);
insert into book (id,title, subtitle ,ISBN, published, author_id) values (  7,'Journey to the Center of the Earth',null,'0-8108-5420-7','2006-01-10', 4);
insert into book (id,title, subtitle ,ISBN, published, author_id) values (  8,'Neuromancer',null,'0-8108-5420-8', '2006-01-10', 5);
insert into book (id,title, subtitle ,ISBN, published, author_id) values (  9,'Count Zero',null,'0-8108-5420-9','2006-01-10', 5);
insert into book (id,title, subtitle ,ISBN, published, author_id) values ( 10,'The Three-Body Problem', null, '0-8108-5420-10', '2006-01-10', 6);
insert into book (id,title, subtitle ,ISBN, published, author_id) values ( 11,'The Dark Forest', null, '0-8108-5420-11','2006-01-10', 6);
insert into book (id,title, subtitle ,ISBN, published, author_id) values ( 12,'Hyperion','1980-05-01','0-8108-5420-12', '1984-04-01',7);
insert into book (id,title, subtitle ,ISBN, published, author_id) values ( 13,'The Fall of Hyperion',null,'0-8108-5420-13','2006-01-10', 7);


# --- !Downs

delete from book;
delete from author;
