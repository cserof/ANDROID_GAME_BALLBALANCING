package nik.uniobuda.hu.balancingball.util;

/**
 * Created by cserof on 12/5/2017.
 */

public class TimeFormatter {

    public static String formatTime(long elapsedTime) {
        String min = twoDigitFormat(String.valueOf(elapsedTime / 60000));
        String sec = twoDigitFormat(String.valueOf((elapsedTime / 1000) % 60));
        String hundreths = twoDigitFormat(String.valueOf((elapsedTime / 10) % 100));
        return min + ":" + sec + ":" + hundreths;
    }

    private static String twoDigitFormat(String string) {
        if (string.length() == 1) {
            string = "0".concat(string);
        }
        return string;
    }
}
