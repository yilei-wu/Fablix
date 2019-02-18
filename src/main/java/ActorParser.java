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

public class ActorParser {
    public static void main(String[] args) {
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try (PrintWriter writer = new PrintWriter(new File("actors.csv"))) {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("src/stanford-movies/actors63.xml");

            StringBuilder sb = new StringBuilder();
            sb.append("name");
            sb.append(',');
            sb.append("year");
            sb.append('\n');

            writer.write(sb.toString());

            Element docEle = dom.getDocumentElement();

            NodeList nl = docEle.getElementsByTagName("actor");
            if(nl != null && nl.getLength() > 0)
            {
                for(int i = 0; i < nl.getLength(); i++)
                {
                    Element each = (Element) nl.item(i);
                    String name = getTextValue(each,"stagename");
                    String year = getTextValue(each,"dob");

                    //if (year.isEmpty()){year = " ";}
                    writer.write(name + "," + year + "\n");
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

//    //List<Element> actors;
//    static Document dom;
//    static File csvfile = new File("src/stanford-movies/actors.csv");
//    static PrintWriter writer = null;
//
//    public static void main(String[] args) throws Exception
//    {
//        csvpre();
//
//        parseXmlFile();
//
//        parseDocument();
//
//        writer.flush();
//        writer.close();
//
//    }
//
//    public static void parseXmlFile()
//    {
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        try
//        {
//            DocumentBuilder db = dbf.newDocumentBuilder();
//
//            dom = db.parse("src/stanford-movies/actors63.xml");
//
//        }catch (ParserConfigurationException pce) {
//            pce.printStackTrace();
//        } catch (SAXException se) {
//            se.printStackTrace();
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
//    }
//
//    public static void parseDocument()
//    {
//        Element docEle = dom.getDocumentElement();
//
//        NodeList nl = docEle.getElementsByTagName("actor");
//        if(nl != null && nl.getLength() > 0)
//        {
//            for(int i = 0; i < nl.getLength(); i++)
//            {
//                Element each = (Element) nl.item(i);
//                intocsv(each);
//            }
//        }
//    }
//
//    public static void csvpre()
//    {
//        try
//        {
//            writer = new PrintWriter(csvfile);
//            writer.write("name, year\n");
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public static void intocsv(Element e)
//    {
//        String name = getTextValue(e,"stagename");
//        String year = getTextValue(e,"dob");
//        writer.write(name + "," + year + "\n");
//    }
//
//    private static String getTextValue(Element ele, String tagName) {
//        String textVal = null;
//        NodeList nl = ele.getElementsByTagName(tagName);
//        if (nl != null && nl.getLength() > 0) {
//            Element el = (Element) nl.item(0);
//            textVal = el.getFirstChild().getNodeValue();
//        }
//
//        return textVal;
//    }
}
