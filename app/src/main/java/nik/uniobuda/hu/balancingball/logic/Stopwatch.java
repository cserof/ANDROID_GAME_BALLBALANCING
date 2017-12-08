package nik.uniobuda.hu.balancingball.logic;

import nik.uniobuda.hu.balancingball.util.TimeFormatter;

/**
 * Created by cserof on 11/25/2017.
 * Implements a stopwatch
 * It provides the time between start time and actual time in milliseconds
 * If the stopper was freezed, it provides time between start time and the time of freezing.
 * It can provide the same times in mm:ss:ff format (ff :  hundredths of a second).
 */

public class Stopwatch {

    private long startTime;
    private boolean freezed = false;
    private long freezedTime;

    public void startOrReset() {
        freezed = false;
        startTime = System.currentTimeMillis();
    }

    public String getFormattedElapsedTime() {
        return TimeFormatter.formatTime(getElapsedTime());
    }

    public long getElapsedTime() {
        long elapsedTime;
        if (freezed) {
            elapsedTime = freezedTime;
        }
        else {
            elapsedTime = System.currentTimeMillis() - startTime;
        }
        return elapsedTime;
    }

    public void freeze() {
        freezedTime = getElapsedTime();
        freezed = true;
    }

}
