package nik.uniobuda.hu.balancingball.model;

/**
 * Created by cserof on 10/23/2017.
 */

public class Point3D {

    private double initialX;
    private double initialY;
    private double initialZ;

    private double displayedX;
    private double displayedY;
    private double displayedZ;

    private double[][] rotationMatrix;

    public Point3D(double x, double y, double z) {
        this.initialX = x;
        this.initialY = y;
        this.initialZ = z;

        rotationMatrix = Util.calculateRotationMatrix(0, 0, 0);
    }

    public double getDisplayedX() {
        return displayedX;
    }

    public double getDisplayedY() {
        return displayedY;
    }

    public double getDisplayedZ() {
        return displayedZ;
    }

    public void setRotationMatrix(double[][] rotationMatrix) {
        this.rotationMatrix = rotationMatrix;
        calcRotatedCoordinates();
    }

    private void calcRotatedCoordinates() {
        double[][] m = {
                {initialX,initialY,initialZ}
        };

        double[][] n =  Util.matrixMultiplication(m, rotationMatrix);
        displayedX = n[0][0];
        displayedY = n[0][1];
        displayedZ = n[0][2];
    }
}
