package nik.uniobuda.hu.balancingball.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by cserof on 11/15/2017.
 */

public class Level implements Parcelable {
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

    protected Level(Parcel in) {
        levelNumber = in.readInt();
        levelMsg = in.readString();
        startX = in.readFloat();
        startY = in.readFloat();
        mapElements = in.createTypedArrayList(MapElement.CREATOR);
    }

    public static final Creator<Level> CREATOR = new Creator<Level>() {
        @Override
        public Level createFromParcel(Parcel in) {
            return new Level(in);
        }

        @Override
        public Level[] newArray(int size) {
            return new Level[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(levelNumber);
        dest.writeString(levelMsg);
        dest.writeFloat(startX);
        dest.writeFloat(startY);
        dest.writeTypedList(mapElements);
    }
}

