USE 122B;

# LOAD DATA LOCAL INFILE '/Users/BrianJiang/JavaProjects/Fablix/src/test.csv'
#   IGNORE INTO TABLE movies
#   FIELDS TERMINATED BY ','
#   ENCLOSED BY '"'
#   ESCAPED BY '"'
#   LINES TERMINATED BY '\n'
#   IGNORE 1 LINES
#   (id, title, year, director);

LOAD DATA LOCAL INFILE '/Users/BrianJiang/JavaProjects/Fablix/cast.csv'
  IGNORE INTO TABLE `122B`.movies
  FIELDS TERMINATED BY ','
  ENCLOSED BY '"'
  ESCAPED BY '"'
  LINES TERMINATED BY '\n'
  IGNORE 1 LINES
  (id, title, director);


# SELECT * FROM `122B`.genres
# INTO OUTFILE 'asdf/temp.csv'
#   FIELDS TERMINATED BY ','
#   ENCLOSED BY '"'
#   LINES TERMINATED BY '\n';