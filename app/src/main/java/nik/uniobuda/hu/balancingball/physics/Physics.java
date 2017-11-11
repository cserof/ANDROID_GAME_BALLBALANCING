package nik.uniobuda.hu.balancingball.physics;

import nik.uniobuda.hu.balancingball.model.Vector2D;

/**
 * Created by cserof on 11/11/2017.
 */

public class Physics {

    public static Vector2D getAccelerationVector(double pitch, double roll) {

        double a = Math.sin(roll);
        double b = Math.sin(pitch);

        Vector2D tiltVector = new Vector2D(a, b);
        return tiltVector;
    }


}
