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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MovieParser {
    public static void main(String[] args) {
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try (PrintWriter writer = new PrintWriter(new File("movies.csv"))) {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("src/stanford-movies/mains243.xml");

            StringBuilder sb = new StringBuilder();
            sb.append("did");
            sb.append(',');
            sb.append("directors");
            sb.append(',');
            sb.append("title");
            sb.append(',');
            sb.append("mid");
            sb.append(',');
            sb.append("year");
            sb.append(',');
            sb.append("genres");
            sb.append('\n');

            writer.write(sb.toString());

            Element docEle = dom.getDocumentElement();

//            NodeList nl =  docEle.getElementsByTagName("movies");
//            System.out.println(nl.getLength());
//            Element allmovie = (Element) nl.item(0);

            NodeList movies = docEle.getElementsByTagName("directorfilms");
            if(movies != null && movies.getLength() > 0)
            {
                for(int i = 0; i < movies.getLength(); i++)
                {
                    Element element = (Element) movies.item(i);// the elements contains all
                    //movies directed by the director
                    String did = getTextValue(element, "dirid");
                    String dname = getTextValue(element, "dirname");
                    NodeList dmovies = element.getElementsByTagName("films");
                    if(dmovies != null && dmovies.getLength() > 0)
                    {
                        for(int j = 0;  j< dmovies.getLength(); j++)
                        {
                            Element amovie = (Element) dmovies.item(j);
                            NodeList aa = amovie.getElementsByTagName("film");
                            if(aa != null && aa.getLength() > 0)
                            {
                                for(int q = 0; q < aa.getLength(); q++)
                                {
                                    Element movie = (Element) aa.item(q);
                                    String mid = getTextValue(movie,"fid");
                                    String title = getTextValue(movie,"t");
                                    String year = getTextValue(movie,"year");
                                    StringBuilder allgenres = new StringBuilder();
                                    NodeList genres = movie.getElementsByTagName("cats");
                                    {System.out.println(genres.getLength());}
                                    if(genres != null && genres.getLength() > 0)
                                    {
                                        for(int k = 0; k < genres.getLength(); k++)
                                        {
                                            Element g = (Element) genres.item(k);
                                            String gg = getTextValue(g,"cat");
                                            allgenres.append(gg + "\\");
                                        }
                                    }
                                    did = did.replace("\""," ").replace("\\", " ");
                                    dname = dname.replace("\""," ").replace("\\", " ");
                                    mid = mid.replace("\""," ").replace("\\", " ");
                                    title = title.replace("\""," ").replace("\\", " ");
                                    year = year.replace("\""," ").replace("\\", " ");
                                    writer.write("\"" + did + "\",\"" + dname + "\",\"" + mid + "\",\"" + title + "\",\"" + year + "\",");
                                    allgenres.append('\n');
                                    String genress = allgenres.toString().replace("\""," ").replace("\\", " ");
                                    writer.write("\"" + genress + "\"");
                                }
                            }

                        }
                    }
                }
            }
            //writer.flush();
            writer.close();
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
                textVal = "";
            }

        }
        //if (textVal.isEmpty()){return "null";}
        return textVal;
    }
}