package nik.uniobuda.hu.balancingball.model;

import java.util.ArrayList;

import nik.uniobuda.hu.balancingball.util.MatrixOperations;

/**
 * Created by cserof on 10/23/2017.
 * Representing the model of the ball and it's physic
 */

public class Ball {

    /**
     * Physical size of the ball's radius.
     * It should be refer to the size of the map.
     */
    private final static float radius = 50;

    /**
     * Physical size of the dots' radius on the ball.
     * It should be refer to the size of the map.
     */
    private final static float dotSize = 3;

    /**
     * Length of velocity vector is multiplied by this ratio in every game cycle.
     */
    private final static float coefficientOfFriction = 0.99f;
    private final ArrayList<Dot3D> dots = new ArrayList<>();

    private float positionX;
    private float positionY;

    /**
     * Next X, Y coordinate of the ball is calculated by
     * adding velocity to them in every game cycle.
     */
    private Vector2D velocity;

    /**
     * Next velocity is calculated by
     * adding acceleration to it in every game cycle.
     */
    private Vector2D acceleration;

    /**
     * rotationMatrix
     * all the dots on the ball are rotated by this
     */
    private double[][] rotationMatrix;

    public Ball(float startX, float startY) {
        setToStartPosition(startX, startY);
        createDots();
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


    /**
     * Calculate the next and the actual rotation matrix's multiplication and
     * sets the ball's rotation matrix to it and
     * iterates through the dots on the ball
     * to calculate their rotated positions.
     * @param rotationMatrix
     */
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

    /**
     * Sets the ball's position to a specific x and y coordinate.
     *
     * @param   startX   next wanted X coordinate of the ball
     * @param   startY   next wanted Y coordinate of the ball
     */
    public void setToStartPosition(float startX, float startY) {
        positionX = startX;
        positionY = startY;

        velocity = new Vector2D(0.1, 0.1);
        acceleration = new Vector2D(0, 0);

        rotationMatrix = MatrixOperations.calculateRotationMatrix(0, 0, 0);
    }

    /**
     * Length of velocity vector is multiplied by coefficientOfFriction in every game cycle.
     */
    private void friction() {
        this.velocity.setX(velocity.getX()*coefficientOfFriction);
        this.velocity.setY(velocity.getY()*coefficientOfFriction);
    }

    /**
     * Sets the ball's next position and calculates the necessary rotations
     * based on the motion.
     */
    public void roll() {
        positionX += velocity.getX();
        positionY += velocity.getY();

        double[][] m1 = MatrixOperations.calculateRotationMatrix(0, 0, velocity.getDirection());
        double[][] m2 = MatrixOperations.calculateRotationMatrix(0,- velocity.getDistance()/radius, 0);
        double[][] m3 = MatrixOperations.calculateRotationMatrix(0, 0, -velocity.getDirection());

        setNextRotationMatrix(MatrixOperations.matrixMultiplication(MatrixOperations.matrixMultiplication(m3, m2), m1));
    }


    /**
     * Calculates the 2 angle of tilt based on the received orientation
     * @param orientation angle of tilts (azimut, pitch, roll)
     */
    public void calculateForceOnTheBall(float[] orientation) {
        //double azimut = orientation[0];
        double pitch = orientation[1];
        double roll = orientation[2];

        calculateAcceleration(-pitch, roll);
    }

    /**
     * Sets the acceleration based on the given tilts.
     * @param pitch tilt angle of axis x
     * @param roll tilt angle of axis y
     */
    private void calculateAcceleration(double pitch, double roll) {
        this.acceleration.setX(Math.sin(roll));
        this.acceleration.setY(Math.sin(pitch));
    }

    /**
     * Reversing the sign of velocity vector's X coordinate
     */
    public void bounceOnVertical() {
        velocity.setX(-velocity.getX());
    }

    /**
     * Reversing the sign of velocity vector's Y coordinate
     */
    public void bounceOnHorizontal() {
        velocity.setY(-velocity.getY());
    }

    /**
     * Sets the initial Points of the dots
     */
    private void createDots()
    {
        dots.add(new Dot3D(-radius, 0, 0));
        dots.add(new Dot3D(radius , 0, 0));
        dots.add(new Dot3D(0, -radius, 0));
        dots.add(new Dot3D(0, radius, 0));
        dots.add(new Dot3D(0, 0, -radius));
        dots.add(new Dot3D(0, 0, radius));
    }
}
