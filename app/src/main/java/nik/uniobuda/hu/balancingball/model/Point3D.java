package nik.uniobuda.hu.balancingball.model;

import nik.uniobuda.hu.balancingball.util.Util;

/**
 * Created by cserof on 10/23/2017.
 */

public class Point3D {

    private double initialX;
    private double initialY;
    private double initialZ;

    private float displayedX;
    private float displayedY;
    private float displayedZ;

    public Point3D(double x, double y, double z) {
        this.initialX = x;
        this.initialY = y;
        this.initialZ = z;
    }

    public float getDisplayedX() {
        return displayedX;
    }

    public float getDisplayedY() {
        return displayedY;
    }

    public float getDisplayedZ() {
        return displayedZ;
    }

    public void calcRotatedCoordinates(double[][] rotationMatrix) {
        double[][] m = {
                {initialX,initialY,initialZ}
        };

        double[][] n =  Util.matrixMultiplication(m, rotationMatrix);
        displayedX = (float) n[0][0];
        displayedY = (float) n[0][1];
        displayedZ = (float) n[0][2];
    }
}
