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

@WebServlet(name = "DashboardServlet", urlPatterns = "/api/dashboard")
public class DashboardServlet extends HttpServlet {
    @Resource(name = "moviedb")
    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        JsonArray result = new JsonArray();
        ArrayList<String> tables = getTablesMetadata();
        PrintWriter out = response.getWriter();
        for (String each :tables)
        {
            JsonObject table = new JsonObject();
            table.addProperty("name",each);
            table.add("attributes", getTableMetadata(each));
            result.add(table);
        }
        out.write(result.toString());
        out.close();
    }

    public ArrayList<String> getTablesMetadata()
    {
        String table[] = {};
        ArrayList<String> tables = new ArrayList<>();
        try
        {
            Connection dbcon = dataSource.getConnection();
            DatabaseMetaData metaData = dbcon.getMetaData();
            ResultSet resultSet = metaData.getTables(null,null,null, table);
            while (resultSet.next())
            {
                tables.add(resultSet.getString("TABLE_NAME"));
            }
            dbcon.close();
            resultSet.close();
            return tables;

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return tables;
    }

    public JsonArray getTableMetadata(String table_name)
    {
        JsonArray info = new JsonArray();
        try
        {
            Connection connection = dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null,null,table_name,null);
            while (resultSet.next())
            {
                JsonObject each = new JsonObject();
                each.addProperty("name", resultSet.getString("COLUMN_NAME"));
                each.addProperty("type", resultSet.getString("TYPE_NAME"));
                info.add(each);
            }
            connection.close();
            resultSet.close();
            return info;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return info;
    }
}


