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

    public static String GetIntID(int length) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length ; ++i) {
            Random r = new Random();
            result.append(Character.toChars(r.nextInt(10) + 48));
        }
        return result.toString();
//        return UUID.randomUUID().toString().substring(0, length);
    }

    public static void main(String[] args){
        System.out.println(GetIntID(10));
    }
}
