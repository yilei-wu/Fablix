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
            if(!next.equals("cardnumber") && !next.equals("expdate") && !next.equals("firstname") && !next.equals("last_name"))
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
            ResultSet resultSet = statement.executeQuery();
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
                result.addProperty("type", "success");
                //for(String each: movieSet){System.out.println(each);}
                for(String each: movieSet)
                {
                    for(int i = 0; i < Integer.parseInt(request.getParameter(each)); i++)
                    {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date();
                        String insert_query = "INSERT INTO sales(id, customerId, movieId, saleDate) VALUES(?, ?, ?, ? )";

                        PreparedStatement statement1 = dbcon.prepareStatement(insert_query);
                        statement1.setString(1, String.valueOf(getSalesId()));
                        statement1.setString(2, customer_id);
                        statement1.setString(3, each);
                        statement1.setString(4, dateFormat.format(date));
                        System.out.println(statement1);
                        statement1.executeUpdate();
                        System.out.println("hello");
                    }
                }
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
            System.err.println(e);
            JsonObject r = new JsonObject();
            r.addProperty("error_message", e.getMessage());
            out.write(r.toString());
            response.setStatus(500);
        }
        out.close();
    }

    int getSalesId()
    {
        try
        {
            Connection con = dataSource.getConnection();
            String query = "SELECT max(id) as m FROM sales";
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet r = statement.executeQuery();
            while (r.next())
            {
                String nn = r.getString("m");
                con.close();
                statement.close();
                r.close();
                return Integer.parseInt(nn)+1;
            }
        }
        catch (Exception e)
        {
            System.out.println("error in finding sales number");
            return 0;
        }
        return 0;
    }
}
