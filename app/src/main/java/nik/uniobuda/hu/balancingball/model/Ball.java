package nik.uniobuda.hu.balancingball.model;

import java.util.ArrayList;

import nik.uniobuda.hu.balancingball.logic.Physics;
import nik.uniobuda.hu.balancingball.util.Util;

/**
 * Created by cserof on 10/23/2017.
 */

public class Ball {

    private static final float radius = 50;
    private ArrayList<Point3D> points = new ArrayList<Point3D>();

    private float positionX;
    private float positionY;

    private Vector2D velocity;
    private Vector2D acceleration;

    private double[][] rotationMatrix;

    public Ball(float startX, float startY) {

        positionX = startX;
        positionY = startY;

        velocity = new Vector2D(1, 1);
        acceleration = new Vector2D(0, 0);

        createPoints();
        rotationMatrix = Util.calculateRotationMatrix(0, 0, 0);
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public static float getRadius() {
        return radius;
    }

    public ArrayList<Point3D> getPoints() {
        return points;
    }

    public void calcRotationMatrix(double[][] rotationMatrix) {
        this.rotationMatrix = Util.matrixMultiplication(this.rotationMatrix,rotationMatrix);
        for (Point3D point : points) {
            point.calcRotatedCoordinates(this.rotationMatrix);
        }
    }


    public void accelerate() {
        velocity.add(acceleration);
    }

    public void roll() {

        positionX += velocity.getX();
        positionY += velocity.getY();

        double[][] m1 = Util.calculateRotationMatrix(0, 0, velocity.getDirection());
        double[][] m2 = Util.calculateRotationMatrix(0,- velocity.getDistance()/radius, 0);
        double[][] m3 = Util.calculateRotationMatrix(0, 0, -velocity.getDirection());

        calcRotationMatrix(Util.matrixMultiplication(Util.matrixMultiplication(m3, m2), m1));
    }

    private void createPoints()
    {
        points.add(new Point3D(-radius, 0, 0));
        points.add(new Point3D(radius , 0, 0));
        points.add(new Point3D(0, -radius, 0));
        points.add(new Point3D(0, radius, 0));
        points.add(new Point3D(0, 0, -radius));
        points.add(new Point3D(0, 0, radius));
    }

    public Vector2D getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2D acceleration) {
        this.acceleration = acceleration;
    }

    public void calculateForceOnTheBall(float[] orientation) {
        double azimut = orientation[0];
        double pitch = orientation[1];
        double roll = orientation[2];

        setAcceleration(Physics.getAccelerationVector(-pitch, roll));
    }
}
