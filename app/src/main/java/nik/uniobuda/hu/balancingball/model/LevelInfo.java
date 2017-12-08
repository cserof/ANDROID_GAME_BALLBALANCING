package nik.uniobuda.hu.balancingball.model;

/**
 * Created by cserof on 11/24/2017.
 * Model of level infos to represent data kind of TOC to levels.
 */

public class LevelInfo {
    private String levelId;
    private String name;

    public LevelInfo(String id, String name) {
        this.levelId = id;
        this.name = name;
    }

    public String getLevelId() {
        return levelId;
    }

    public String getName() {
        return name;
    }
}
