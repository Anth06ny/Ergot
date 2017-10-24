package ergot.anthony.com.ergot.utils;

/**
 * Created by Anthony on 24/10/2017.
 */

public class Utils {

    public static String longToStringPrice(long price) {
        long euro = price / 100;
        long ct = price % 100;

        String ctString = ct < 10 ? "0" + ct : "" + ct;

        return euro + "." + ctString + " €";
    }
}
