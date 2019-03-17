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
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name = "InsertStarServlet", urlPatterns = "/api/insert_star")
public class InsertStarServlet extends HttpServlet {
    @Resource(name = "masterdb")
    private DataSource dataSource;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String byear = request.getParameter("year");
        if (byear.isEmpty()) byear = null;

        try
        {
            Connection dbcon = dataSource.getConnection();
            String script = "{CALL INSERT_STAR(?,?)}";
            PreparedStatement statement = dbcon.prepareStatement(script);
            statement.setString(1,name);
            statement.setString(2,byear);

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
    }

}
