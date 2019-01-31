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
import java.util.ArrayList;

@WebServlet(name = "GnereBrowsingServlet", urlPatterns = "/api/genre_browse")
public class GnereBrowsingServlet extends HttpServlet {
    @Resource(name = "moviedb")
    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try
        {
            Connection dbcon = dataSource.getConnection();
            String genre = request.getParameter("genre");
            String sort = request.getParameter("sort");
            String page = request.getParameter("page");
            String records = request.getParameter("records");


            String select_query  = "SELECT  movies.id, title, `year`, director, rating, GROUP_CONCAT(distinct genres.name SEPARATOR ', ') as gname, GROUP_CONCAT(distinct  stars.name SEPARATOR ',') as sname";
            String from_query = "FROM movies left join ratings r on movies.id = r.movieId, genres, genres_in_movies, stars, stars_in_movies";
            String where_join = "WHERE movies.id = genres_in_movies.movieId and genres_in_movies.genreId = genres.id and stars_in_movies.movieId = movies.id and stars_in_movies.starId = stars.id";
            String genre_condition = "AND genres.name = " + genre;
            String sort_clause = get_sort_clause(sort);
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

    private String get_sort_clause(String sort)
    {
        return "";
    }

    private String get_offst_clause(String page, String records)
    {
        return "";
    }
}
