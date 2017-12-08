package nik.uniobuda.hu.balancingball.logic;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import nik.uniobuda.hu.balancingball.model.Ball;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by cserof on 11/15/2017.
 * Gets orientation (azimuth, pitch and roll of the device) based on
 * ACCELEROMETER, MAGNETIC_FIELD sensors and forwards it to Ball.
 * source:
 * https://stackoverflow.com/questions/20339942/get-device-angle-by-using-getorientation-function
 * https://developer.android.com/reference/android/hardware/SensorManager.html#getOrientation(float[], float[])
 */

public class SensorController {

    private SensorManager mSensorManager;
    private Ball ball;

    public SensorController(Activity activity, Ball ball) {
        this.ball = ball;
        mSensorManager = (SensorManager)activity.getSystemService(SENSOR_SERVICE);
    }

    public void registerSensors() {
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mSensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(listener, magnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void unregisterSensors() {
        mSensorManager.unregisterListener(listener);
    }

    private SensorEventListener listener = new SensorEventListener() {

        float[] mGravity;
        float[] mGeomagnetic;
        float[] R = new float[9];
        float[] I = new float[9];
        float[] orientation = new float[3];

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mGravity = event.values;
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                mGeomagnetic = event.values;
            if (mGravity != null && mGeomagnetic != null) {
                boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                if (success) {
                    SensorManager.getOrientation(R, orientation);
                    ball.calculateForceOnTheBall(orientation);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };


}
