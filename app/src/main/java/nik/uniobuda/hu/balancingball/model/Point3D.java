package nik.uniobuda.hu.balancingball.model;

import nik.uniobuda.hu.balancingball.util.MatrixOperations;

/**
 * Created by cserof on 10/23/2017.
 */

public class Point3D {

    private double[][] initialCoords;
    private double[][] displayedCoords;

    public Point3D(float x, float y, float z) {
        this.initialCoords = new double[1][3];

        initialCoords[0][0] = x;
        initialCoords[0][1] = y;
        initialCoords[0][2] = z;
    }

    public float getDisplayedX() {
        return (float) displayedCoords[0][0];
    }

    public float getDisplayedY() {
        return (float) displayedCoords[0][1];
    }

    public float getDisplayedZ() {
        return (float) displayedCoords[0][2];
    }

    public void calcRotatedCoordinates(double[][] rotationMatrix) {
        displayedCoords =  MatrixOperations.matrixMultiplication(initialCoords, rotationMatrix);
    }
}
