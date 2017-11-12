package nik.uniobuda.hu.balancingball;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import nik.uniobuda.hu.balancingball.model.Vector2D;
import nik.uniobuda.hu.balancingball.physics.Physics;

public class MainActivity extends AppCompatActivity {

    /*private SensorManager mSensorManager;


    TextView textViewX;
    TextView textViewY;
    TextView textViewZ;
    */

    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        gameView = new GameView(this);
        setContentView(gameView);

        /*
        textViewX = (TextView) findViewById(R.id.x);
        textViewY = (TextView) findViewById(R.id.y);
        textViewZ = (TextView) findViewById(R.id.z);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        Log.e("BB", "OnCreate, sensor: " + mSensorManager.toString());
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
        /*
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mSensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(listener, magnetometer, SensorManager.SENSOR_DELAY_UI);
        Log.e("BB", "OnResume, acc: " + accelerometer.toString() + "magn: " + magnetometer.toString());
        */
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();

        /*
        mSensorManager.unregisterListener(listener);
        Log.e("BB", "onPause, leiratkozott mindenki");
        */
    }
/*
    private SensorEventListener listener = new SensorEventListener() {

        float[] mGravity;
        float[] mGeomagnetic;

        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.e("BB", "onSensorChanged, : " + event.sensor + " " + event.values);
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mGravity = event.values;
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                mGeomagnetic = event.values;
            if (mGravity != null && mGeomagnetic != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                if (success) {
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);

                    setTexts(orientation);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private void setTexts(float[] orientation) {

        double azimut = orientation[0];
        double pitch = orientation[1];
        double roll = orientation[2];

        Vector2D v = Physics.getAccelerationVector(pitch, roll);

        textViewX.setText("tilt vector: " + v.getX() + ", " + v.getY() + "  : " + Math.toDegrees(v.getDirection()));
        textViewY.setText("pitch: " + Math.round(180*pitch/Math.PI));
        textViewZ.setText("roll: " + Math.round(180*roll/Math.PI));

    }
    */
}
