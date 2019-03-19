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

@WebServlet(name = "InsertMovieServlet", urlPatterns = "/api/insert_movie")

public class InsertMovieServlet extends HttpServlet {
    @Resource(name = "moviedb")
    private DataSource dataSource;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String title = request.getParameter("title");
        String year = request.getParameter("year");
        String director = request.getParameter("director");
        String star = request.getParameter("star");
        String genre = request.getParameter("genre");

        try
        {
            Connection dbcon = dataSource.getConnection();
            String script = "{CALL INSERT_MOVIE(?,?,?,?,?)}";
            PreparedStatement statement = dbcon.prepareStatement(script);
            statement.setString(1,title);
            statement.setString(2,year);
            statement.setString(3,director);
            statement.setString(4,star);
            statement.setString(5,genre);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                JsonObject r = new JsonObject();
                String res = resultSet.getString("res");
                r.addProperty("status", res);
                out.write(r.toString());
                response.setStatus(200);
                resultSet.close();
                dbcon.close();
                return;
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
            JsonObject r = new JsonObject();
            r.addProperty("error_message", e.getMessage());
            out.write(r.toString());
            response.setStatus(500);
        }
        out.close();
    }
}
