import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
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

@WebServlet(name = "GenreServlet", urlPatterns = "/api/all_genres")
public class GenreServlet extends HttpServlet {

//    @Resource(name = "slavedb")
//    DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try
        {
            Context initCtx = new InitialContext();

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                out.println("envCtx is NULL");


            DataSource dataSource = (DataSource) envCtx.lookup(Generator.get_source_name());
            Connection dbcon = dataSource.getConnection();
//            Connection dbcon = dataSource.getConnection();
            String query = "Select distinct(genres.name) as a From genres";
            PreparedStatement statement = dbcon.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            JsonArray j = new JsonArray();

            while (resultSet.next())
            {
                j.add(resultSet.getString("a"));
            }
            out.write(j.toString());
            response.setStatus(200);

            dbcon.close();
            statement.close();
            resultSet.close();
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
