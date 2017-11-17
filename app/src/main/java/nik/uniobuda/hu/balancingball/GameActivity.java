package nik.uniobuda.hu.balancingball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nik.uniobuda.hu.balancingball.logic.SensorController;
import nik.uniobuda.hu.balancingball.model.Ball;
import nik.uniobuda.hu.balancingball.model.Level;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private Ball ball;
    private Level lvl;
    private SensorController sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lvl = getIntent().getExtras().getParcelable("selectedLevel");
        ball = new Ball(lvl.getStartX(), lvl.getStartY());
        sensor = new SensorController(this, ball);

        gameView = new GameView(this, ball, lvl);
        setContentView(gameView);
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
