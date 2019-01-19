import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet(name = "SingleMovieServlet", urlPatterns = "/api/single_movie")

public class SingleMovieServlet extends HttpServlet {

    @Resource(name = "moviedb")
    private DataSource dataSource;
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException
    {
        response.setContentType("application/json");
        String id = request.getParameter("id");
        PrintWriter out = response.getWriter();

        try
        {
            Connection dbcon = dataSource.getConnection();

            String query =
                    "SELECT movies.id, title, `year`, director, rating, GROUP_CONCAT(distinct genres.name SEPARATOR ', ') as gname, GROUP_CONCAT(distinct  stars.name SEPARATOR ',') as sname\n" +
                            "FROM movies, ratings, genres, genres_in_movies, stars_in_movies, stars\n" +
                            "where movies.id = ? and movies.id = ratings.movieId and movies.id = genres_in_movies.movieId and genres_in_movies.genreId = genres.id and stars_in_movies.movieId = movies.id and stars_in_movies.starId = stars.id\n" +
                            "group by title;";

            PreparedStatement statement = dbcon.prepareStatement(query);


            statement.setString(1,id);

            ResultSet rs = statement.executeQuery();

            JsonArray movie_list = new JsonArray();

            while(rs.next())
            {
                String movie_id = rs.getString("id");
                String title  = rs.getString("title");
                int year = rs.getInt("year");
                String director = rs.getString("director");
                float rating = rs.getFloat("rating");
                String list_of_genres = rs.getString("gname");
                String list_of_stars = rs.getString("sname");

                JsonArray json_stars = new JsonArray();
                String star_query = "SELECT distinct stars.name, stars.id\n" +
                        "FROM movies, stars, stars_in_movies\n" +
                        "WHERE movies.id = ? and movies.id = stars_in_movies.movieId and stars_in_movies.starId = stars.id;";
                PreparedStatement s = dbcon.prepareStatement(star_query);
                s.setString(1, movie_id);
                ResultSet r = s.executeQuery();
                while (r.next())
                {
                    JsonObject star = new JsonObject();
                    star.addProperty("star_name", r.getString("name"));
                    star.addProperty("star_id", r.getString("id"));
                    json_stars.add(star);
                }

                JsonObject movie = new JsonObject();
                movie.addProperty("id", movie_id);
                movie.addProperty("title", title);
                movie.addProperty("year", year);
                movie.addProperty("director", director);
                movie.addProperty("rating", rating);
                movie.addProperty("genre_list", list_of_genres);
                movie.add("star_list", json_stars);

                movie_list.add(movie);
            }

            out.write(movie_list.toString());

            response.setStatus(200);

            rs.close();
            dbcon.close();
            statement.close();
        }
        catch (Exception e)
        {
            JsonObject error = new JsonObject();
            error.addProperty("error_message", e.getMessage());
            out.write(error.toString());

            response.setStatus(500);
        }
        out.close();
    }
}

