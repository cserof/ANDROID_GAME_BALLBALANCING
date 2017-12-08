package nik.uniobuda.hu.balancingball.model;

import java.util.ArrayList;

import nik.uniobuda.hu.balancingball.util.MatrixOperations;

/**
 * Created by cserof on 10/23/2017.
 */

public class Ball {

    private final float radius = 50;
    private final float dotSize = 3;
    private final float coefficientOfFriction = 0.99f;
    private final ArrayList<Dot3D> dots = new ArrayList<Dot3D>();

    private float positionX;
    private float positionY;

    private Vector2D velocity;
    private Vector2D acceleration;

    private double[][] rotationMatrix;

    public Ball(float startX, float startY) {
        setToStartPosition(startX, startY);
        createPoints();
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public float getRadius() {
        return radius;
    }

    public float getDotSize() {
        return dotSize;
    }

    public ArrayList<Dot3D> getDots() {
        return dots;
    }


    private void setNextRotationMatrix(double[][] rotationMatrix) {
        this.rotationMatrix = MatrixOperations.matrixMultiplication(this.rotationMatrix,rotationMatrix);
        for (Dot3D point : dots) {
            point.calcRotatedCoordinates(this.rotationMatrix);
        }
    }


    public void accelerate() {
        velocity.add(acceleration);
        friction();
    }

    private void friction() {
        this.velocity.setX(velocity.getX()*coefficientOfFriction);
        this.velocity.setY(velocity.getY()*coefficientOfFriction);
    }

    public void roll() {

        positionX += velocity.getX();
        positionY += velocity.getY();

        double[][] m1 = MatrixOperations.calculateRotationMatrix(0, 0, velocity.getDirection());
        double[][] m2 = MatrixOperations.calculateRotationMatrix(0,- velocity.getDistance()/radius, 0);
        double[][] m3 = MatrixOperations.calculateRotationMatrix(0, 0, -velocity.getDirection());

        setNextRotationMatrix(MatrixOperations.matrixMultiplication(MatrixOperations.matrixMultiplication(m3, m2), m1));
    }

    public void calculateForceOnTheBall(float[] orientation) {
        //double azimut = orientation[0];
        double pitch = orientation[1];
        double roll = orientation[2];

        calculateAcceleration(-pitch, roll);
    }

    private void calculateAcceleration(double pitch, double roll) {
        this.acceleration.setX(Math.sin(roll));
        this.acceleration.setY(Math.sin(pitch));
    }

    //reversing the sign of velocity vector's X coordinate
    public void bounceOnVertical() {
        velocity.setX(-velocity.getX());
    }

    //reversing the sign of velocity vector's Y coordinate
    public void bounceOnHorizontal() {
        velocity.setY(-velocity.getY());
    }

    private void createPoints()
    {
        dots.add(new Dot3D(-radius, 0, 0));
        dots.add(new Dot3D(radius , 0, 0));
        dots.add(new Dot3D(0, -radius, 0));
        dots.add(new Dot3D(0, radius, 0));
        dots.add(new Dot3D(0, 0, -radius));
        dots.add(new Dot3D(0, 0, radius));
    }

    public void setToStartPosition(float startX, float startY) {
        positionX = startX;
        positionY = startY;

        velocity = new Vector2D(0.1, 0.1);
        acceleration = new Vector2D(0, 0);

        rotationMatrix = MatrixOperations.calculateRotationMatrix(0, 0, 0);
    }
}
