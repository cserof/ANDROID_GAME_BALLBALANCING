package nik.uniobuda.hu.balancingball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import nik.uniobuda.hu.balancingball.model.Ball;


public class MainActivity extends AppCompatActivity {

    private final static int startX = 0;
    private final static int startY = 0;

    private GameView gameView;
    private Ball ball;
    private SensorController sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        ball = new Ball(startX, startY);
        gameView = new GameView(this, ball);
        setContentView(gameView);
        sensor = new SensorController(this, ball);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
        sensor.registerSensors();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
        sensor.unregisterSensors();
    }

}
