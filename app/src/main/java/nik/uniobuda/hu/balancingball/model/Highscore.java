package nik.uniobuda.hu.balancingball.model;

/**
 * Created by cserof on 11/25/2017.
 * Model of best times for a specific level.
 */

public class Highscore {
    private String levelId;
    private long bestTime;

    public Highscore(String id, long bestTime) {
        this.levelId = id;
        this.bestTime = bestTime;
    }

    public String getLevelId() {
        return levelId;
    }

    public long getBestTime() {
        return bestTime;
    }

    public void setBestTime(long bestTime) {
        this.bestTime = bestTime;
    }


}
