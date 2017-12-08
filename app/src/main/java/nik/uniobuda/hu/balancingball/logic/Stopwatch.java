package nik.uniobuda.hu.balancingball.logic;

import nik.uniobuda.hu.balancingball.util.TimeFormatter;

/**
 * Created by cserof on 11/25/2017.
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
