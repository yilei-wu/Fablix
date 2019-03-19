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

@WebServlet(name = "SingleStarServlet", urlPatterns = "/api/single_star")
public class SingleStarServlet extends HttpServlet {
    @Resource(name = "slavedb")
    private DataSource dataSource;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");
        String id = request.getParameter("id");
        PrintWriter out = response.getWriter();

        try
        {
            Connection dbcon = dataSource.getConnection();

            String query = "SELECT stars.id, stars.birthYear, stars.name, stars.birthYear, GROUP_CONCAT(distinct movies.title SEPARATOR ', ') as movie_list\n" +
                    "FROM stars_in_movies INNER JOIN movies ON stars_in_movies.movieId = movies.id\n" +
                    "INNER JOIN stars ON stars_in_movies.starId = stars.id\n" +
                    "WHERE stars.id = ?\n" +
                    "GROUP BY name;";

            PreparedStatement statement = dbcon.prepareStatement(query);
            statement.setString(1, id);

            ResultSet rs = statement.executeQuery();
            JsonArray star = new JsonArray();

            while (rs.next())
            {
                String name = rs.getString("name");
                String star_id = rs.getString("id");
                String birth_year = rs.getString("birthYear");
                String movie_list = rs.getString("movie_list");

                JsonArray json_movie = new JsonArray();
                String movie_query = "SELECT movies.title , movies.id\n" +
                        "FROM movies, stars, stars_in_movies\n" +
                        "WHERE stars_in_movies.starId = ? and movies.id = stars_in_movies.movieId and stars_in_movies.starId = stars.id;";
                PreparedStatement s =dbcon.prepareStatement(movie_query);
                s.setString(1, id);
                ResultSet r = s.executeQuery();
                while (r.next())
                {
                    JsonObject movie = new JsonObject();
                    movie.addProperty("movie_id", r.getString("id"));
                    movie.addProperty("movie_title", r.getString("title"));
                    json_movie.add(movie);
                }

                JsonObject m = new JsonObject();
                m.addProperty("star_name", name);
                m.addProperty("star_id", star_id);
                m.addProperty("birth_year", birth_year);
                m.add("movie_list", json_movie);

                star.add(m);
            }

            out.write(star.get(0).toString());
            response.setStatus(200);

            rs.close();
            dbcon.close();
            statement.close();
        }
        catch (Exception e)
        {
            JsonObject error = new JsonObject();
            error.addProperty("error", e.getMessage());
            out.write(error.toString());
            response.setStatus(500);
        }
        out.close();
    }
}
