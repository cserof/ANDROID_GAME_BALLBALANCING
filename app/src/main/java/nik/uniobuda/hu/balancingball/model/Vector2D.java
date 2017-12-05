package nik.uniobuda.hu.balancingball.model;

/**
 * Created by cserof on 10/23/2017.
 */

public class Vector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDirection() {
        double direction;
        if (x==0 && y > 0) {
            direction = 3*Math.PI/2;
        }
        else if (x == 0 && y < 0) {
            direction = Math.PI/2;
        }
        else if (x > 0){
            direction =  - Math.atan(y/x);
        }
        else {
            direction = Math.PI - Math.atan(y/x);
        }

        return direction;
    }

    public double getDistance() {
        return Math.sqrt(x*x+y*y);
    }

    public void add(Vector2D v) {
        this.x += v.getX();
        this.y += v.getY();
    }

}
