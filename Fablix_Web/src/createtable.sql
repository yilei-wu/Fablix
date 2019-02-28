DROP DATABASE if exists 122B;
CREATE DATABASE 122B;
USE 122B;

drop table if exists movies;
CREATE TABLE movies
(
  id varchar(10),
  title varchar(100),
  year int,
  director varchar(100),

  PRIMARY KEY (id),
  FULLTEXT (title)
);

drop table if exists stars;
CREATE TABLE stars
(
  id varchar(10),
  name varchar(100),
  birthYear int,

  PRIMARY KEY (id)
);

drop table if exists stars_in_movies;
CREATE TABLE stars_in_movies
(
  starId varchar (10),
  movieId varchar (10),

  FOREIGN KEY (starId) REFERENCES stars(id),
  FOREIGN KEY (movieId) REFERENCES movies(id)
);

drop table if exists genres;
CREATE TABLE genres
(
  id Int,
  name varchar (32),

  PRIMARY KEY (id)
);

drop table if exists genres_in_movies;
CREATE TABLE genres_in_movies
(
  genreId Int,
  movieId varchar (10),

  FOREIGN KEY (genreId) REFERENCES genres(id),
  FOREIGN KEY (movieId) REFERENCES movies(id)
);

drop table if exists creditcards;
CREATE TABLE creditcards
(
  id varchar (20),
  firstName varchar (50),
  lastName varchar (50),
  expiration date,

  PRIMARY KEY (id)
);

drop table if exists customers;
CREATE TABLE customers
(
  id Int,
  firstName varchar (50),
  lastName varchar (50),
  ccId varchar (20),
  address varchar (200),
  email varchar (50),
  `password` varchar (20),

  PRIMARY KEY (id),
  FOREIGN KEY (ccId) REFERENCES creditcards(id)
);

drop table if exists sales;
CREATE TABLE sales
(
  id Int,
  customerId Int,
  movieId varchar(10),
  saleDate date,

  PRIMARY KEY (id),
  FOREIGN KEY (customerId) REFERENCES customers(id),
  FOREIGN KEY (movieId) REFERENCES movies(id)
);



drop table if exists ratings;
CREATE TABLE ratings
(
  movieId varchar (10),
  rating float,
  numVotes Int,

  FOREIGN KEY (movieId) REFERENCES movies(id)
);

drop table if exists employees;
CREATE TABLE employees
(
  email varchar(50),
  password varchar(20) not null,
  fullname varchar(100),
  PRIMARY KEY (email)
);

drop table if exists titlefts;
CREATE TABLE titlefts
(
  entreeid INT AUTO_INCREMENT,
  entree text,
  PRIMARY KEY(entreeid),
  FULLTEXT(entree)
);

ALTER TABLE movies ADD FULLTEXT (title);

