package nik.uniobuda.hu.balancingball.model;

import java.util.ArrayList;

/**
 * Created by cserof on 11/15/2017.
 * Model of a level to represent data describing the map and necessary information to handle levels of the game.
 */

public class Level {
    private String id;
    private String levelMsg;
    private String nextLevelId;
    private float startX;
    private float startY;
    private float width;
    private float height;
    private ArrayList<MapElement> mapElements;

    public Level(String id, String levelMsg, String nextLevelId, ArrayList<MapElement> mapElements, float startX, float startY, float width, float height) {
        this.id = id;
        this.levelMsg = levelMsg;
        this.nextLevelId = nextLevelId;
        this.mapElements = mapElements;
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
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

    public String getId() {
        return id;
    }

    public String getLevelMsg() {
        return levelMsg;
    }

    public String getNextLevelId() {
        return nextLevelId;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}

