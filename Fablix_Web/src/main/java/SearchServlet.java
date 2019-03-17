import com.google.gson.JsonArray;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@WebServlet(name = "SearchServlet", urlPatterns = "/api/search_movie")
public class SearchServlet extends HttpServlet {
    @Resource (name = "slavedb")
    private DataSource dataSource;


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("SS:" + request.getRequestURI());
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        //System.out.println(request.getRequestURI());
        String title = ((HttpServletRequest)request).getParameter("title");
        String year = ((HttpServletRequest)request).getParameter("year");
        String director = ((HttpServletRequest)request).getParameter("director");
        String star = ((HttpServletRequest)request).getParameter("star");
        String page = ((HttpServletRequest)request).getParameter("page");
        String records = ((HttpServletRequest)request).getParameter("records");
        String sort = ((HttpServletRequest)request).getParameter("sort");
        try{

            Connection dbcon = dataSource.getConnection();
            //System.out.println(title + "|" + year + "|" + director + "|" + star + "|" + page + "|" + records + "|" + sort);

            String select_query  = "SELECT  movies.id, title, `year`, director, rating, GROUP_CONCAT(distinct genres.name SEPARATOR ', ') as gname, GROUP_CONCAT(distinct  stars.name SEPARATOR ',') as sname\n";
            String from_query = "FROM movies left join ratings r on movies.id = r.movieId, genres, genres_in_movies, stars, stars_in_movies\n";
            String where_join = "WHERE movies.id = genres_in_movies.movieId and genres_in_movies.genreId = genres.id and stars_in_movies.movieId = movies.id and stars_in_movies.starId = stars.id\n";
            String title_condition =  "and movies.title like ? \n";
            String year_condition =  "and movies.year = ? \n";
            String directory_condition =  "and movies.director like ? \n";
            String star_condition =   "and stars.name like ? \n";

            String group_clause = "GROUP BY title ";
            String order_clause = get_sort_clause(sort);
            String offset_clause = get_offset_clause(page, records);
            if (year.isEmpty()){year = "%"; year_condition = "and movies.year like ? ";}
            String query = select_query + from_query + where_join + title_condition + year_condition
                    + directory_condition + star_condition + group_clause + order_clause + offset_clause;

            String queryt = "SELECT COUNT(distinct movies.id) as a " + from_query + where_join + title_condition + year_condition
                    + directory_condition + star_condition;

            System.out.println(queryt);

            PreparedStatement statement = dbcon.prepareStatement(query);
            PreparedStatement statement1 = dbcon.prepareStatement(queryt);
            statement.setString(1,"%" + title + "%" );
            statement1.setString(1, "%" + title + "%");
            statement.setString(2,year);
            statement1.setString(2,year);
            statement.setString(3,"%" + director + "%");
            statement1.setString(3,"%" + director + "%");
            statement.setString(4,"%" + star + "%");
            statement1.setString(4, "%" + star + "%");

            ResultSet w = statement1.executeQuery();
            ResultSet resultSet = statement.executeQuery();
            JsonArray movie_list = new JsonArray();

            ArrayList<String> x = new ArrayList<>();
            while (w.next())
            {

                String total = w.getString("a");
                System.out.println(total);
                x.add(total);
            }

            while(resultSet.next())
            {
                String sid = resultSet.getString("id");
                String stitle  = resultSet.getString("title");
                int syear = resultSet.getInt("year");
                String sdirector = resultSet.getString("director");
                float srating = resultSet.getFloat("rating");
                String list_of_genres = resultSet.getString("gname");
                String list_of_stars = resultSet.getString("sname");

                JsonArray json_stars = new JsonArray();
                String star_query = "SELECT distinct stars.name, stars.id\n" +
                        "FROM movies, stars, stars_in_movies\n" +
                        "WHERE movies.id = ? and movies.id = stars_in_movies.movieId and stars_in_movies.starId = stars.id;";
                PreparedStatement s = dbcon.prepareStatement(star_query);
                s.setString(1, sid);
                ResultSet r = s.executeQuery();
                while (r.next())
                {
                    JsonObject sstar = new JsonObject();
                    sstar.addProperty("star_name", r.getString("name"));
                    sstar.addProperty("star_id", r.getString("id"));
                    json_stars.add(sstar);
                }

                JsonObject movie = new JsonObject();
                movie.addProperty("movie_id", sid);
                movie.addProperty("movie_title", stitle);
                movie.addProperty("year", syear);
                movie.addProperty("director", sdirector);
                movie.addProperty("rating", srating);
                movie.addProperty("genre_list", list_of_genres);
                movie.add("star_list", json_stars);

                movie_list.add(movie);

            }
            JsonObject final_result = new JsonObject();
            final_result.add("movielist", movie_list);
            final_result.addProperty("total_number" ,Integer.parseInt(x.get(0)));
//            Integer.parseInt(x.get(0))
            out.write(final_result.toString());
            response.setStatus(200);
            dbcon.close();
            statement.close();
            resultSet.close();
        }
        catch(Exception e){
            response.setStatus(500);
            JsonObject obj = new JsonObject();
            obj.addProperty("error_message", e.getMessage());
            out.write(obj.toString());
            //throw new ServletException(e);

        }

        out.close();
    }

    private int get_total_page(int total_records, int record)
    {
        return total_records/record + 1;
    }
    private String get_offset_clause(String page, String records)
    {
        int p = Integer.parseInt(page);
        int r = Integer.parseInt(records);

        int offset = (p-1)*r;

        return String.format("limit %d,%d", offset, r);
    }

    private String get_sort_clause(String sort)
    {
        if(sort.equals("ta"))
        {
            return "ORDER BY movies.title ";
        }
        else if(sort.equals("td"))
        {
            return "ORDER BY movies.title DESC ";
        }
        else if(sort.equals("ra"))
        {
            return "ORDER BY rating ";
        }
        else if(sort.equals("rd"))
        {
            return "ORDER BY rating DESC ";
        }
        else
        {
            return "";
        }
    }
}
