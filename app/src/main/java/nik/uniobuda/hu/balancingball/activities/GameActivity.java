package nik.uniobuda.hu.balancingball.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nik.uniobuda.hu.balancingball.GameView;
import nik.uniobuda.hu.balancingball.logic.SensorController;
import nik.uniobuda.hu.balancingball.model.Ball;
import nik.uniobuda.hu.balancingball.model.Level;
import nik.uniobuda.hu.balancingball.util.XmlLevelParser;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private Ball ball;
    private Level level;
    private SensorController sensor;
    private XmlLevelParser xmlMapParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String selectedLevelId = getIntent().getExtras().getString("selectedLevelId");
        xmlMapParser = new XmlLevelParser(this);

        level = createLevel(selectedLevelId);
        ball = new Ball(level.getStartX(), level.getStartY());
        sensor = new SensorController(this, ball);
        gameView = new GameView(this, ball, level);
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

    public Level createLevel(String levelId) {
        return xmlMapParser.getParsedLevel(levelId);
    }

    public Level nextLevel() {
        String nextLevelId = level.getNextLevelId();
        if (nextLevelId.equals("gameCompleted")) {
            congrats();
        }
        else {
            level = createLevel(nextLevelId);
        }
        return level;
    }

    private void congrats() {
    }
}
