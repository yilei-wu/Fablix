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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;


@WebServlet(name = "PurchaseServlet", urlPatterns = "/api/purchase")
public class PurchaseServlet extends HttpServlet {
    @Resource(name = "moviedb")
    private DataSource dataSource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        //process all the parameters here
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String card_number = ((HttpServletRequest)request).getParameter("cardnumber");
        String exp_date = ((HttpServletRequest)request).getParameter("expdate");
        String first_name = ((HttpServletRequest)request).getParameter("firstname");
        String last_name = ((HttpServletRequest)request).getParameter("last_name");
        String customer_id = ((HttpServletRequest)request).getParameter("customer_id");
        Enumeration<String> keySet = ((HttpServletRequest)request).getParameterNames();
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
            Connection dbcon = dataSource.getConnection();
            String query = "select * from creditcards\n" +
                    "where id = ? and firstName = ? and lastName = ? and expiration = ?;";
            PreparedStatement statement = dbcon.prepareStatement(query);
            statement.setString(1, card_number);
            statement.setString(4, exp_date);
            statement.setString(2, first_name);
            statement.setString(3, last_name);
            System.out.println(statement);
            ResultSet resultSet = statement.executeQuery();
            System.out.println(resultSet.toString());
            JsonObject jsonObject = new JsonObject();
            JsonObject result = new JsonObject();
            while (resultSet.next())
            {
                jsonObject.addProperty("yes", resultSet.getString("id"));
            }

            if(jsonObject.size() == 0)
            {
                result.addProperty("type", "failure");
            }
            else if(jsonObject.size() != 0)
            {
                System.out.println("asdasdas" + movieSet.toString());
                result.addProperty("type", "success");
//                for(String each: movieSet)
//                {
//                    System.out.println("hello");
//                    for(int i = 0; i < Integer.parseInt(request.getParameter(each)); i++)
//                    {
//                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                        Date date = new Date();
//                        String insert_query = "INSERT INTO sales(customerld, movield, saleDate) VALUES(?, ?, ? )";
//
//                        PreparedStatement statement1 = dbcon.prepareStatement(insert_query);
//                        statement.setString(1, customer_id);
//                        statement.setString(2, each);
//                        statement.setString(3, dateFormat.format(date));
//                        statement1.executeQuery();
//                        System.out.println("hello");
//                    }
//                }
            }

            System.out.println(result.toString());
            out.write(result.toString());

            dbcon.close();
            statement.close();
            resultSet.close();
            response.setStatus(200);
        }
        catch (Exception e)
        {
            JsonObject r = new JsonObject();
            r.addProperty("error_message", e.getMessage());
            out.write(r.toString());
            response.setStatus(500);
        }
        out.close();
    }
}
