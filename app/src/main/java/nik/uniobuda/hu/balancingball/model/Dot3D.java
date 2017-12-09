package nik.uniobuda.hu.balancingball.model;

import nik.uniobuda.hu.balancingball.util.MatrixOperations;

/**
 * Created by cserof on 10/23/2017.
 * Represents the model of rolling dots on the ball
 * axis:
 *   x - horizontal in the pane of the screen
 *   y - vertical in the pane of the screen
 *   z - perpendicular to the pane of the screen
 */

public class Dot3D {

    /**
     * Coordinates of a dot without any rotation.
     * Helps to position dots to each other,
     * to make the desired shape of the 3D like object
     */
    private double[][] initialCoords;

    /**
     * Coordinates of a dot considering rotations.
     */
    private double[][] displayedCoords;

    public Dot3D(float x, float y, float z) {
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
