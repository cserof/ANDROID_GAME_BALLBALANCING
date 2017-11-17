package nik.uniobuda.hu.balancingball.model;

import java.util.ArrayList;

/**
 * Created by cserof on 11/15/2017.
 */

public class Level {
    private int levelNumber;
    private String levelMsg;
    private float startX;
    private float startY;
    private ArrayList<MapElement> mapElements;

    public Level(int levelNumber, String levelMsg, ArrayList<MapElement> mapElements, float startX, float startY) {
        this.levelNumber = levelNumber;
        this.levelMsg = levelMsg;
        this.mapElements = mapElements;
        this.startX = startX;
        this.startY = startY;
    }

    public ArrayList<MapElement> getMapElements() {
        return mapElements;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public String getLevelMsg() {
        return levelMsg;
    }
}

