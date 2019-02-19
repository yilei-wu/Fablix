import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CastParser {
    public static void main(String[] args) {
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try (PrintWriter writer = new PrintWriter(new File("star.csv"));
        PrintWriter star_in_movie = new PrintWriter(new File("star_in_movie.csv"))) {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("src/stanford-movies/casts124.xml");

            StringBuilder sb = new StringBuilder();
            StringBuilder ssb = new StringBuilder();
            sb.append("movie_id");
            sb.append(',');
            sb.append("star_id");
            sb.append('\n');

            ssb.append("star_id").append(',').append("star_name").append('\n');

            writer.write(ssb.toString());
            star_in_movie.write(sb.toString());

            Element docEle = dom.getDocumentElement();

            NodeList nl = docEle.getElementsByTagName("filmc");
            if (nl != null && nl.getLength() > 0) {
                for (int i = 0; i < nl.getLength(); i++) {
                    Element each = (Element) nl.item(i);//
                    NodeList nll  = each.getElementsByTagName("m");
                    if(nll != null && nll.getLength() > 0)
                    {
                        for(int j = 0; j < nll.getLength(); j ++ )
                        {
                            Element each_movie = (Element) nll.item(j);
                            String id = getTextValue(each_movie, "f");
                            String movie = getTextValue(each_movie, "t");
                            String star = getTextValue(each_movie,"a");
                            String star_id = Generator.GetHashID(star, 10);
                            //id = id.replace("\""," ").replace("\\", " ");
                            //movie = movie.replace("\""," ").replace("\\", " ");
                            //star = star.replace("\""," ").replace("\\", " ");
                            writer.write("\"" + star_id + "\",\"" + star + "\"\n");
                            star_in_movie.write("\"" + id + "\",\"" + star_id + "\"\n");

                            if(id=="null"){System.out.println("casts124.xml : movie_id is missing");}
                            if(movie=="null"){System.out.println("casts124.xml : movie_title is missing");}
                            if(star=="null"){System.out.println("casts124.xml : star_name is missing");}
                            if(star_id=="null"){System.out.println("casts124.xml : movie_id is missing");}
                        }
                    }


                    //if (year.isEmpty()){year = " ";}

                }
            }
            //writer.flush();
            writer.close();
            star_in_movie.close();
            System.out.println("done!");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static String getTextValue(Element ele, String tagName) {
        String textVal = "null";
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            try{
                textVal = el.getFirstChild().getNodeValue();}
            catch (NullPointerException n)
            {
                textVal = "null";
            }

        }
        //if (textVal.isEmpty()){return "null";}
        return textVal;
    }
}