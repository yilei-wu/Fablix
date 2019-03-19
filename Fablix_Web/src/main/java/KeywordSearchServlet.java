import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static java.awt.Event.ESCAPE;

@WebServlet(name = "KeywordSearchServlet", urlPatterns = "/api/keyword_search")
public class  KeywordSearchServlet extends HttpServlet
{
    @Resource(name = "slavedb")
    DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        long ts_start = System.nanoTime();
        long tj = 0;
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String keyword = request.getParameter("keyword");
        String page = request.getParameter("page");
        String records = request.getParameter("records");
        String sort = request.getParameter("sort");

        try
        {

            Connection dbcon = dataSource.getConnection();

//            ArrayList<String> match_title = new ArrayList<>();
//            String fts_query = "SELECT entree FROM titlefts WHERE MATCH(entree) AGAINST(? IN BOOLEAN MODE)";
//            PreparedStatement fts_statement = dbcon.prepareStatement(fts_query);
//            fts_statement.setString(1, keyword);
//            ResultSet rfts = fts_statement.executeQuery();
//            while(rfts.next())
//            {
//                match_title.add(rfts.getString("entree"));
//            }


            String select_query  = "SELECT  movies.id, title, `year`, director, rating, GROUP_CONCAT(distinct genres.name SEPARATOR ', ') as gname, GROUP_CONCAT(distinct  stars.name SEPARATOR ',') as sname";
            String from_query = " FROM movies left join ratings r on movies.id = r.movieId, genres, genres_in_movies, stars, stars_in_movies";
            String where_join = " WHERE movies.id = genres_in_movies.movieId and genres_in_movies.genreId = genres.id and stars_in_movies.movieId = movies.id and stars_in_movies.starId = stars.id";
            String title_condition = " AND MATCH(movies.title) AGAINST(? IN BOOLEAN MODE) " ;
            String group_clause = " GROUP BY id";
            String order_clause = get_sort_clause(sort);
            String offset_clause = get_offset_clause(page, records);

            String query = select_query + from_query + where_join + title_condition + group_clause + order_clause + offset_clause;
            String queryt = "Select count(distinct movies.id) as a " + from_query + where_join + title_condition;
//            String fuzzy_query = "SELECT * FROM [" + query + "] WHERE ed(title,?) <= 3";
//            String fuzzy_count = "SELECT COUNT(*) AS a FROM " + fuzzy_query;
            PreparedStatement statement = dbcon.prepareStatement(query);
            PreparedStatement statement1 = dbcon.prepareStatement(queryt);
            String[] con = keyword.split("\\s+");
            String f = "";
            for(String each: con)
            {
                f += ("+" + each + "* ");
            }
            statement.setString(1, f );
            //statement.setString(2, keyword);
            statement1.setString(1 , f );
            //statement1.setString(2, keyword);
            long tj_start = System.nanoTime();
            ResultSet resultSet = statement.executeQuery();
            ResultSet w = statement1.executeQuery();
            long tj1 = System.nanoTime();
            tj += (tj1 - tj_start);
            //System.out.println(query);

            JsonArray movie_list = new JsonArray();
            ArrayList<String> x = new ArrayList<>();


            while (w.next())
            {

                String total = w.getString("a");
                //System.out.println(total);
                x.add(total);
            }
            long tj_end = 0;
            long tj2 = 0;
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
                tj2 = System.nanoTime();
                ResultSet r = s.executeQuery();
                tj_end = System.nanoTime();
                tj += (tj_end- tj2);
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
            final_result.addProperty("total_number" , Integer.parseInt(x.get(0)));
            out.write(final_result.toString());
            response.setStatus(200);

            long ts_end = System.nanoTime();
            //long tj = tj_end - tj_start;
            long ts = ts_end - ts_start;


            String context_path = getServletContext().getRealPath("/");
            String log_path = context_path + "log.txt";
            System.out.println(log_path);
            File log = new File(log_path);
            log.createNewFile();
            try(FileOutputStream fileOutputStream = new FileOutputStream(log,true))
            {
                String str = "\nts:_" + ts + "_tj:_" + tj + "_";
                fileOutputStream.write(str.getBytes());
            }

//            if(!log.exists())
//            {
//
//            }
//            else
//            {
//                FileWriter writer = new FileWriter(log);
//                writer.write("ts: " + ts + "tj: " + tj);
//            }

            dbcon.close();
            resultSet.close();
            statement.close();

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

    private String get_sort_clause(String sort) {
        if (sort.equals("ta")) {
            return " ORDER BY movies.title ";
        } else if (sort.equals("td")) {
            return " ORDER BY movies.title DESC ";
        } else if (sort.equals("ra")) {
            return " ORDER BY rating ";
        } else if (sort.equals("rd")) {
            return " ORDER BY rating DESC ";
        } else {
            return "";
        }
    }

//    private int get_total_page(int total_records, int record)
//    {
//        return total_records/record + 1;
//    }


    private String get_offset_clause(String page, String records)
    {
        int p = Integer.parseInt(page);
        int r = Integer.parseInt(records);

        int offset = (p-1)*r;

        return String.format(" limit %d,%d", offset, r);
    }
}