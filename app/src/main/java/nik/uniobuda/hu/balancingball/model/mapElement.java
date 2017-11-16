package nik.uniobuda.hu.balancingball.model;

import nik.uniobuda.hu.balancingball.util.mapType;

/**
 * Created by cserof on 11/15/2017.
 */

public class mapElement {
    double x;
    double y;
    double height;
    double width;
    mapType type;

    boolean bottomDmg;
    boolean	topDmg;
    boolean	rightDmg;
    boolean	leftDmg;

    public mapElement(double x, double y, double height, double width, mapType type, boolean bottomDmg, boolean topDmg, boolean rightDmg, boolean leftDmg) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.type = type;
        this.bottomDmg = bottomDmg;
        this.topDmg = topDmg;
        this.rightDmg = rightDmg;
        this.leftDmg = leftDmg;
    }
}
