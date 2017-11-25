package nik.uniobuda.hu.balancingball.util;

/**
 * Created by cserof on 11/25/2017.
 */

public class Record {
    String id;
    long bestTime;

    public Record(String id, long bestTime) {
        this.id = id;
        this.bestTime = bestTime;
    }

    public String getId() {
        return id;
    }

    public long getBestTime() {
        return bestTime;
    }

    public void setBestTime(long bestTime) {
        this.bestTime = bestTime;
    }


}
