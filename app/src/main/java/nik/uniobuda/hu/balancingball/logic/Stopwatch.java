package nik.uniobuda.hu.balancingball.logic;

import nik.uniobuda.hu.balancingball.util.TimeFormatter;

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
        return TimeFormatter.formatTime(elapsedTime);
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

}
