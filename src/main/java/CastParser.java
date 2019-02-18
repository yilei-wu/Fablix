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

        try (PrintWriter writer = new PrintWriter(new File("cast.csv"), "ISO-8859-1")) {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("src/stanford-movies/casts124.xml");

            StringBuilder sb = new StringBuilder();
            sb.append("id");
            sb.append(',');
            sb.append("movie");
            sb.append(",");
            sb.append("star");
            sb.append('\n');

            writer.write(sb.toString());

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
                            id = id.replace("\""," ").replace("\\", " ");
                            movie = movie.replace("\""," ").replace("\\", " ");
                            star = star.replace("\""," ").replace("\\", " ");
                            writer.write("\"" + id + "\",\"" + movie + "\",\"" + star + "\"\n");
                        }
                    }


                    //if (year.isEmpty()){year = " ";}

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