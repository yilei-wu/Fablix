import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Pattern;

public class process_log {
    public static void main(String[] x)
    {
        try {
            //System.out.println(System.getProperty("user.dir"));
            File file = new File( System.getProperty("user.dir") + "/src/main/jmerter_log/single5/log.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));


//            long ts = 0;
//            long tj = 0;
//            Pattern pattern = Pattern.compile("ts:*\\p{Alpha}");
//            Pattern pattern1 = Pattern.compile("tj:*");

            long average_servlet = 0;
            long average_jdbc = 0;
            final long CNT = 2642;

            String st;
            String[] l = null;
            while ((st = br.readLine()) != null) {
                if(st.length() >= 1){
                    l = st.split("_");
//                    System.out.println(l[1] + "::::" + l[3]);
                    average_servlet += Long.valueOf(l[1]);
                    average_jdbc += Long.valueOf(l[3]);
                }

//                System.out.println(st.substring(4,14) + " " + st.substring(19,29));
//                System.out.println(st.substring(19,29));
            }

            average_servlet /= CNT;
            average_jdbc /= CNT;

            System.out.println("Average servlet: " + average_servlet);
            System.out.println("Average jdbc: " + average_jdbc);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
