package nik.uniobuda.hu.balancingball.util;

/**
 * Created by cserof on 12/5/2017.
 * formats a given time to mm:ss:ff format (ff :  hundredths of a second)
 */
public class TimeFormatter {

    /**
     * Formats a given time in ms to mm:ss:ff String format
     * @param   elapsedTime   time interval in milliseconds
     * @return same times in mm:ss:ff format (ff :  hundredths of a second)
     */
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
