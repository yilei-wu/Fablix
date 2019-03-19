import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Pattern;

public class process_log {
    public static void main(String[] x)
    {
        try {
            //System.out.println(System.getProperty("user.dir"));
            File file = new File( System.getProperty("user.dir") + "\\target\\fablix\\log.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));


            long ts = 0;
            long tj = 0;
//            Pattern pattern = Pattern.compile("ts:*\\p{Alpha}");
//            Pattern pattern1 = Pattern.compile("tj:*");
            String st;
            while ((st = br.readLine()) != null )
                if (st.length() >=1)
                {
                    ts += Long.valueOf(st.substring(4,14));
                    tj += Long.valueOf(st.substring(19,29));
                }
                //System.out.println(st.substring(4,14) + " " + st.substring(19,29));
//                System.out.println(st.substring(19,29));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
