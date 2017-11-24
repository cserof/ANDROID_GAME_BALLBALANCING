package nik.uniobuda.hu.balancingball.model;

import android.os.Parcel;
import android.os.Parcelable;

import nik.uniobuda.hu.balancingball.util.mapType;

/**
 * Created by cserof on 11/15/2017.
 */

public class MapElement {
    float left;
    float top;
    float right;
    float bottom;
    mapType type;

    boolean isDamage;

    public MapElement(float left, float top, float right, float bottom, mapType type, boolean isDamage) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.type = type;
        this.isDamage = isDamage;
    }

    public float getLeft() {
        return left;
    }

    public float getTop() {
        return top;
    }

    public float getRight() {
        return right;
    }

    public float getBottom() {
        return bottom;
    }

    public mapType getType() {
        return type;
    }

    public boolean isDamage() {
        return isDamage;
    }
}
