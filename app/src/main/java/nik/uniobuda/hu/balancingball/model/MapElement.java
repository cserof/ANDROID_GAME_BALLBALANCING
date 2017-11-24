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

    boolean bottomDmg;
    boolean	topDmg;
    boolean	rightDmg;
    boolean	leftDmg;

    public MapElement(float left, float top, float right, float bottom, mapType type, boolean bottomDmg, boolean topDmg, boolean rightDmg, boolean leftDmg) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.type = type;
        this.bottomDmg = bottomDmg;
        this.topDmg = topDmg;
        this.rightDmg = rightDmg;
        this.leftDmg = leftDmg;
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

    public boolean isBottomDmg() {
        return bottomDmg;
    }

    public boolean isTopDmg() {
        return topDmg;
    }

    public boolean isRightDmg() {
        return rightDmg;
    }

    public boolean isLeftDmg() {
        return leftDmg;
    }
}
