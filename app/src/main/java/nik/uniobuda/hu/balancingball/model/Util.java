package nik.uniobuda.hu.balancingball.model;

/**
 * Created by cserof on 10/23/2017.
 */

public class Util {

    public static double[][] calculateRotationMatrix(double x, double y, double z) {
        double[][] m = {
                {Math.cos(y)*Math.cos(z),    Math.sin(x)*Math.sin(y)*Math.cos(z)-Math.cos(x)*Math.sin(z),    Math.sin(x)*Math.sin(z)+Math.cos(x)*Math.sin(y)*Math.cos(z)    },
                {Math.cos(y)*Math.sin(z),    Math.cos(x)*Math.cos(z) + Math.sin(x)*Math.sin(y)*Math.sin(z),  Math.cos(x)*Math.sin(y)*Math.sin(z)-Math.sin(x)*Math.cos(z)    },
                {-Math.sin(y),               Math.sin(x)*Math.cos(y),                                        Math.cos(x)*Math.cos(y)                                        }
        };
        return m;
    }

    //c = a*b
    public static double[][] matrixMultiplication(double[][] a, double[][] b) {
        int aRows = a.length;
        int aColumns = a[0].length;
        int bColumns = b[0].length;

        double[][] c = new double[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                for (int k = 0; k < aColumns; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }

        return c;
    }
}
