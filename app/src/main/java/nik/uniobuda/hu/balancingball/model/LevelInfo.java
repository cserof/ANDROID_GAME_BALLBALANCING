package nik.uniobuda.hu.balancingball.model;

/**
 * Created by cserof on 11/24/2017.
 */

public class LevelInfo {
    private String id;
    private String name;

    public LevelInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
