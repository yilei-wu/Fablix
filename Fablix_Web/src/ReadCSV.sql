USE 122B;

ALTER TABLE `122B`.movies ADD UNIQUE movie_info(title, year, director);
ALTER TABLE `122B`.genres ADD UNIQUE genre_name(name);

LOAD DATA LOCAL INFILE '/Users/BrianJiang/JavaProjects/Fablix/movies.csv'
  IGNORE INTO TABLE `122B`.movies
  FIELDS TERMINATED BY ','
  ENCLOSED BY '"'
  ESCAPED BY '\\'
  LINES TERMINATED BY '\n'
  IGNORE 1 LINES
  (director, id, title, year);

LOAD DATA LOCAL INFILE '/Users/BrianJiang/JavaProjects/Fablix/genre.csv'
  IGNORE INTO TABLE `122B`.genres
  FIELDS TERMINATED BY ','
  ENCLOSED BY '"'
  ESCAPED BY '\\'
  LINES TERMINATED BY '\n'
  IGNORE 1 LINES
  (name, id);

LOAD DATA LOCAL INFILE '/Users/BrianJiang/JavaProjects/Fablix/genre_in_movie.csv'
  IGNORE INTO TABLE `122B`.genres_in_movies
  FIELDS TERMINATED BY ','
  ENCLOSED BY '"'
  ESCAPED BY '\\'
  LINES TERMINATED BY '\n'
  IGNORE 1 LINES
  (genreId, movieId);

LOAD DATA LOCAL INFILE '/Users/BrianJiang/JavaProjects/Fablix/star.csv'
  IGNORE INTO TABLE `122B`.stars
  FIELDS TERMINATED BY ','
  ENCLOSED BY '"'
  ESCAPED BY '\\'
  LINES TERMINATED BY '\n'
  IGNORE 1 LINES
  (id, name);

LOAD DATA LOCAL INFILE '/Users/BrianJiang/JavaProjects/Fablix/star_in_movie.csv'
  IGNORE INTO TABLE `122B`.stars_in_movies
  FIELDS TERMINATED BY ','
  ENCLOSED BY '"'
  ESCAPED BY '\\'
  LINES TERMINATED BY '\n'
  IGNORE 1 LINES
  (movieId, starId);

LOAD DATA LOCAL INFILE '/Users/BrianJiang/JavaProjects/Fablix/actors.csv'
  IGNORE INTO TABLE `122B`.stars
  FIELDS TERMINATED BY ','
  ENCLOSED BY '"'
  ESCAPED BY '\\'
  LINES TERMINATED BY '\n'
  IGNORE 1 LINES
  (id, name, birthYear);
