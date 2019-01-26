import com.google.gson.JsonObject;
import sun.text.resources.cldr.pa.FormatData_pa_Arab;

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

@WebServlet(name = "SearchServlet", urlPatterns = "/api/search_movie")
public class SearchServlet extends HttpServlet {
    @Resource (name = "mmoviedb")
    DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try{
            Connection dbcon = dataSource.getConnection();
            String title = 

        }
        catch(Exception e){
            JsonObject obj = new JsonObject();
            obj.addProperty("error_message", e.getMessage());
            out.write(obj.toString());
            out.close();
        }
    }
}
