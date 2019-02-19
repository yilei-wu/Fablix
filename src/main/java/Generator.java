import java.util.Random;
import java.util.UUID;

public class Generator {
    public static String GetID(int length) {
//        String result = "";
//        for (int i = 0; i < length ; ++i) {
//            Random category = new Random();
//        }
//        return result;
        return UUID.randomUUID().toString().substring(0, length);
    }

    public static void main(String[] args){
        System.out.println(GetID(10));
    }
}
