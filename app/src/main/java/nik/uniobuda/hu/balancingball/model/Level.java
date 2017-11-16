package nik.uniobuda.hu.balancingball.model;

import java.util.ArrayList;

/**
 * Created by cserof on 11/15/2017.
 */

public class Level {
    private int levelNumber;
    private String levelMsg;
    private ArrayList<MapElement> mapElements;

    public Level(int levelNumber, String levelMsg, ArrayList<MapElement> mapElements) {
        this.levelNumber = levelNumber;
        this.levelMsg = levelMsg;
        this.mapElements = mapElements;
    }

    public ArrayList<MapElement> getMapElements() {
        return mapElements;
    }
}

