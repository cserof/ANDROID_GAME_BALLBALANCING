package nik.uniobuda.hu.balancingball.model;

import android.os.Parcel;
import android.os.Parcelable;

import nik.uniobuda.hu.balancingball.util.mapType;

/**
 * Created by cserof on 11/15/2017.
 */

public class MapElement implements Parcelable {
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

    protected MapElement(Parcel in) {
        left = in.readFloat();
        top = in.readFloat();
        right = in.readFloat();
        bottom = in.readFloat();
        type = mapType.valueOf(in.readString());
        bottomDmg = in.readByte() != 0;
        topDmg = in.readByte() != 0;
        rightDmg = in.readByte() != 0;
        leftDmg = in.readByte() != 0;
    }

    public static final Creator<MapElement> CREATOR = new Creator<MapElement>() {
        @Override
        public MapElement createFromParcel(Parcel in) {
            return new MapElement(in);
        }

        @Override
        public MapElement[] newArray(int size) {
            return new MapElement[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(left);
        dest.writeFloat(top);
        dest.writeFloat(right);
        dest.writeFloat(bottom);
        dest.writeString(type.name());
        dest.writeByte((byte) (bottomDmg ? 1 : 0));
        dest.writeByte((byte) (topDmg ? 1 : 0));
        dest.writeByte((byte) (rightDmg ? 1 : 0));
        dest.writeByte((byte) (leftDmg ? 1 : 0));
    }
}
