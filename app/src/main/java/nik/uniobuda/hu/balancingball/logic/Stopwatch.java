package nik.uniobuda.hu.balancingball.logic;

/**
 * Created by cserof on 11/25/2017.
 */

public class Stopwatch {

    private long startTime;

    public void startOrReset() {
        startTime = System.currentTimeMillis();
    }

    public String getFormattedElapsedTime() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        String min = twoDigitFormat(String.valueOf(elapsedTime / 60000));
        String sec = twoDigitFormat(String.valueOf((elapsedTime / 1000) % 60));
        String hundreths = twoDigitFormat(String.valueOf((elapsedTime / 10) % 100));
        return min + ":" + sec + ":" + hundreths;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    private String twoDigitFormat(String string) {
        if (string.length() == 1) {
            string = "0".concat(string);
        }
        return string;
    }

}
