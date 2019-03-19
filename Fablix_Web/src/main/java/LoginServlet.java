import com.google.gson.JsonObject;
import org.jasypt.util.password.StrongPasswordEncryptor;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Resource(name = "moviedb")
    private DataSource datasource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = ((HttpServletRequest)request).getParameter("username");
        String password = ((HttpServletRequest)request).getParameter("password");
        PrintWriter out = response.getWriter();

        if (verifyCredentials(username, password)) {
            String sessionId = (request).getSession().getId();
            Long lastAccessTime = (request).getSession().getLastAccessedTime();
            request.getSession().setAttribute("user", new User(username));
            request.getSession().setAttribute("userid", getUserId(username));
            request.getSession().setAttribute("type", "user");
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");
            responseJsonObject.addProperty("session_id", sessionId);
            responseJsonObject.addProperty("lastaccesstime", lastAccessTime);

            String userAgent = request.getHeader("User-Agent");

            if(!userAgent.contains("Android"))
            {
                try
                {
                    RecaptchaVerifyUtils.verify(request.getParameter("g-recaptcha-response"));
                }
                catch (Exception e)
                {
                    JsonObject r = new JsonObject();
                    r.addProperty("status", "fail");
                    r.addProperty("message", "recaptcha is not satisfied");
                    out.write(r.toString());
                    out.close();
                    return;
                }
                //out.write(responseJsonObject.toString());
            }
            out.write(responseJsonObject.toString());
        }
        else
            {
            // Login fails
            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "fail");
//            if (!username.equals("anteater")) {
//                responseJsonObject.addProperty("message", "user " + username + " doesn't exist");
//            } else {
//                responseJsonObject.addProperty("message", "incorrect password");
//            }
            responseJsonObject.addProperty("message", "user doesn't exist or incorrect password");
            out.write(responseJsonObject.toString());
        }

        out.close();
    }


    String getUserId(String username)
    {
        Connection con = null;
        try
        {
            con = datasource.getConnection();
            String query = "Select id from customers where email = ? ";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next())
            {
                String id = resultSet.getString("id");
                con.close();
                statement.close();
                resultSet.close();
                return id;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return "";
        }
        finally {
            try {
                con.close();
            }catch (Exception e)
            {}
        }
        return "";
    }

    boolean verifyCredentials(String email, String password){
        Connection connection = null;
        try {
            connection = datasource.getConnection();


            String query = " SELECT password from customers where email = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();

            boolean success = false;
            if (rs.next()) {
                // get the encrypted password from the database
                String encryptedPassword = rs.getString("password");

                // use the same encryptor to compare the user input password with encrypted password stored in DB
                System.out.println(encryptedPassword+ " "+ password);
                success = new StrongPasswordEncryptor().checkPassword(password, encryptedPassword);
            }

            rs.close();
            statement.close();
            connection.close();

            System.out.println("verify " + email + " - " + password);

            return success;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        finally {
            try{
                connection.close();
            }catch (Exception e)
            {}
        }
        return false;
    }
}