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
import java.util.Enumeration;


@WebServlet(name = "PurchaseServlet", urlPatterns = "api/purchase/")
public class PurchaseServlet extends HttpServlet {
    @Resource(name = "moviedb")
    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String card_number = request.getParameter("cardnumber");
        String exp_date = request.getParameter("expdate");
        String first_name = request.getParameter("firstname");
        String last_name = request.getParameter("last_name");
        Enumeration<String> keySet = request.getParameterNames();
        ArrayList<String> movieSet = new ArrayList<>();


        while(keySet.hasMoreElements())
        {
            String next = keySet.nextElement();
            if(next != "cardnumber" && next != "expdate" && next != "firstname" && next != "lastname")
            {
                movieSet.add(next);
            }
        }
        try
        {
            String query = "select * from creditcards\n" +
                    "where id = ? and firstName = ? and lastName = ? and expiration = ?;";
            PreparedStatement statement =
        }
        catch (Exception e)
        {
            JsonObject r = new JsonObject();
            r.addProperty("error_message", );
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
