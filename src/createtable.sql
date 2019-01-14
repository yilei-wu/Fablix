CREATE TABLE movies
(
  id varchar(10),
  title varchar(100),
  year int,
  director varchar(100),

  PRIMARY KEY (id)
);

CREATE TABLE stars
(
  id varchar(10),
  name varchar(100),
  birthYear int,

  PRIMARY KEY (id)
);

CREATE TABLE stars_in_movies
(
  starId varchar (10) FOREIGN KEY REFERENCES stars(id),
  movieId varchar (10) FOREIGN KEY REFERENCES movies(id)
);

CREATE TABLE genres
(
  id Int PRIMARY KEY,
  name varchar (32)
);

CREATE TABLE genres_in_movies
(
  genreId Int FOREIGN KEY REFERENCES genres(id),
  movieId varchar (10) FOREIGN KEY REFERENCES movies(id)
);

CREATE TABLE customers
(
  id Int PRIMARY KEY,
  firstName varchar (50),
  lastName varchar (50),
  ccId varchar (20) FOREIGN KEY REFERENCES customers(id),
  address varchar (200),
  email varchar (50),
  password varchar (20)_
);

CREATE TABLE sales
(
  id Int PRIMARY KEY,
  customerId Int FOREIGN KEY REFERENCES customers(id),
  movieId varchar (10) FOREIGN KEY movies(id),
  saleDate date
);

CREATE TABLE creditcards
(
  id varchar (20) PRIMARY KEY,
  firstName varchar (50),
  lastName varchar (50),
  expiration date
);

CREATE TABLE ratings
(
  movieId varchar (10) FOREIGN KEY REFERENCES movies(id),
  rating float,
  numVotes Int
);