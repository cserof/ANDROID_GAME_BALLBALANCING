package nik.uniobuda.hu.balancingball.model;

import java.util.ArrayList;

/**
 * Created by cserof on 10/23/2017.
 */

public class Ball {

    private static final double radius = 50;
    private ArrayList<Point3D> points = new ArrayList<Point3D>();

    private double positionX;
    private double positionY;

    private double[][] rotationMatrix;

    public Ball(int startX, int startY) {

        positionX = startX;
        positionY = startY;

        createPoints();
        rotationMatrix = Util.calculateRotationMatrix(0, 0, 0);
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public static double getRadius() {
        return radius;
    }

    public ArrayList<Point3D> getPoints() {
        return points;
    }

    public void setRotationMatrix(double[][] rotationMatrix) {
        this.rotationMatrix = Util.matrixMultiplication(this.rotationMatrix,rotationMatrix);
        for (Point3D point : points) {
            point.setRotationMatrix(this.rotationMatrix);
        }
    }

    public void roll(Vector2D directionOfMove) {

        positionX += directionOfMove.getX();
        positionY += directionOfMove.getY();

        double[][] m1 = Util.calculateRotationMatrix(0, 0, directionOfMove.getDirection());
        double[][] m2 = Util.calculateRotationMatrix(0,- directionOfMove.getDistance()/radius, 0);
        double[][] m3 = Util.calculateRotationMatrix(0, 0, -directionOfMove.getDirection());

        setRotationMatrix(Util.matrixMultiplication(Util.matrixMultiplication(m3, m2), m1));
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
}
