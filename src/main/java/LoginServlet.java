import com.google.gson.JsonObject;

import javax.annotation.Resource;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    @Resource(name = "moviedb")
    private static final long serialVersionUID = 1L;
    private DataSource datasource;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        PrintWriter out = response.getWriter();

        if (is_user(username, password)) {
            String sessionId = ((HttpServletRequest) request).getSession().getId();
            Long lastAccessTime = ((HttpServletRequest) request).getSession().getLastAccessedTime();
            request.getSession().setAttribute("user", new User(username));

            JsonObject responseJsonObject = new JsonObject();
            responseJsonObject.addProperty("status", "success");
            responseJsonObject.addProperty("message", "success");
            responseJsonObject.addProperty("session_id", sessionId);
            responseJsonObject.addProperty("lastaccesstime", lastAccessTime);

            out.write(responseJsonObject.toString());
        } else {
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
    }

    boolean is_user(String username, String password)//return ture if the username/password pair is valid
    {
        try {
            String query = "SELECT password FROM customers WHERE email = ?";
            Connection dbcon = datasource.getConnection();
            PreparedStatement statement = dbcon.prepareStatement(query);
            statement.setString(1,username);
            ResultSet r = statement.executeQuery();
            String rr = r.getString("password");
            if(rr == password){return true;}
            else{return false;}
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}