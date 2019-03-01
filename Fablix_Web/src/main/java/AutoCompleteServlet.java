import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "AutoCompleteServlet", urlPatterns = "api/auto_complete")
public class AutoCompleteServlet extends HttpServlet {
    @Resource(name = "moviedb")
    private DataSource dataSource;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String keyword = request.getParameter("query");
        try
        {
            JsonArray res  = new JsonArray();
            Connection con = dataSource.getConnection();
            //String keyword = request.getParameter("query");
            String query = "SELECT id, title from movies where match(title) AGAINST ( ? in BOOLEAN MODE) LIMIT 10";
            String[] kk = keyword.split("\\s+");
            StringBuilder kkk = new StringBuilder();
            for(String each : kk)
            {
                kkk.append("+" + each + "*");
            }
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1,kkk.toString());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                JsonObject movie = new JsonObject();
                String id = resultSet.getString("id");
                String movie_title = resultSet.getString("title");
                movie.addProperty("id", id);
                movie.addProperty("title", movie_title);
                res.add(movie);
            }
            out.write(res.toString());
            response.setStatus(200);
            con.close();
            resultSet.close();
            out.close();
        }
        catch (Exception e)
        {
            JsonObject res = new JsonObject();
            res.addProperty("error_message", e.getMessage());
            response.setStatus(500);
            out.write(res.toString());
            out.close();
        }
    }
}
