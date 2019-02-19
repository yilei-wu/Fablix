
DROP FUNCTION IF EXISTS `SAME_MOVIE` ;
CREATE FUNCTION SAME_MOVIE
(
  MTITLE VARCHAR(100), `MYEAR` INT, MDIRECTOR VARCHAR(100)
)
RETURNS BOOL
BEGIN
  DECLARE res int;
  SELECT year INTO res FROM movies WHERE director=MDIRECTOR AND year=MYEAR AND title=MTITLE;
  if (res is NULL) THEN
    RETURN 0 ;
  else
    RETURN 1;
  end if ;
end ;



DROP FUNCTION IF EXISTS GET_GENRE_ID ;
CREATE FUNCTION GET_GENRE_ID(GNAME VARCHAR(32))
RETURNS INT
BEGIN
  DECLARE res int;
  SELECT id INTO res FROM genres WHERE name=GNAME LIMIT 1;
  IF (res is NULL) THEN
    SELECT (COUNT(ID)+1) INTO res FROM genres;
    INSERT INTO genres VALUES (res, GNAME);
  end if ;
  RETURN res;
end ;



DROP FUNCTION IF EXISTS GET_STAR_ID ;
CREATE FUNCTION GET_STAR_ID(SNAME VARCHAR(100))
RETURNS INT
  BEGIN
    DECLARE res int;
    SELECT id INTO res FROM stars WHERE name=SNAME LIMIT 1;
    IF (res is NULL) THEN
      SELECT (COUNT(ID)+1) INTO res FROM stars;
      INSERT INTO stars VALUES(res, SNAME, null);
    end if ;
    RETURN res;
  end ;



DROP PROCEDURE IF EXISTS `INSERT_MOVIE` ;
CREATE PROCEDURE INSERT_MOVIE
(MTITLE VARCHAR(100), MYEAR INT,  MDIRECTOR VARCHAR(100), MSTAR VARCHAR(100), MGENRE VARCHAR(32))
BEGIN
  DECLARE mid INT;
  IF SAME_MOVIE(MTITLE,MYEAR,MDIRECTOR) THEN
    SELECT 'duplicate' as res;
  ELSE
    SELECT (COUNT(id)+1) into mid FROM movies;
    INSERT INTO movies VALUES (mid,MTITLE, MYEAR, MDIRECTOR);
    INSERT INTO stars_in_movies VALUES (GET_STAR_ID(MSTAR),mid);
    INSERT INTO genres_in_movies VALUES (GET_GENRE_ID(MGENRE),mid);
    SELECT 'succeed insert movie' as res;
  end if ;
  end ;


DROP FUNCTION IF EXISTS SAME_STAR ;
CREATE FUNCTION SAME_STAR(SNAME VARCHAR(100), SBIRTH INT)
RETURNS BOOL
BEGIN
  DECLARE b INT;
  SELECT birthYear INTO b FROM stars WHERE SNAME=name AND SBIRTH=birthYear;
  IF (b is NULL) THEN
    RETURN FALSE;
  ELSE
    RETURN TRUE;
  end if ;
end ;


DROP FUNCTION IF EXISTS STAR_ID ;
CREATE FUNCTION STAR_ID()
RETURNS INT
  BEGIN
    DECLARE res INT;
    SELECT (COUNT(ID)+1) INTO res FROM stars;
    RETURN res;
  end ;


DROP PROCEDURE IF EXISTS INSERT_STAR ;
CREATE PROCEDURE INSERT_STAR(SNAME VARCHAR(100), SBIRTH INT)
BEGIN
#   IF (SAME_STAR(SNAME,SBIRTH)) THEN
#     SELECT 'DUPLICATE STAR' as res;
#   ELSE
    INSERT INTO stars VALUES (STAR_ID(),SNAME,SBIRTH);
    SELECT 'succeed insert star' as res;
#   end if ;
end ;