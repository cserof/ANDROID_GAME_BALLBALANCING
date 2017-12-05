package nik.uniobuda.hu.balancingball.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nik.uniobuda.hu.balancingball.GameView;
import nik.uniobuda.hu.balancingball.logic.CollisionDetector;
import nik.uniobuda.hu.balancingball.logic.HighScoreContoller;
import nik.uniobuda.hu.balancingball.logic.SensorController;
import nik.uniobuda.hu.balancingball.logic.Stopwatch;
import nik.uniobuda.hu.balancingball.model.Ball;
import nik.uniobuda.hu.balancingball.model.Level;
import nik.uniobuda.hu.balancingball.util.XmlLevelParser;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private Ball ball;
    private Level level;
    private SensorController sensor;
    private Stopwatch stopper;
    private CollisionDetector collisionDetector;
    private HighScoreContoller highScoreContoller;
    private XmlLevelParser xmlMapParser;

    public Ball getBall() {
        return ball;
    }

    public Level getLevel() {
        return level;
    }

    public boolean isGameLost() {
        return collisionDetector.isGameLost();
    }

    public boolean isGameWon() {
        return collisionDetector.isGameWon();
    }

    public boolean isBallJustCollided() {
        return collisionDetector.isJustCollided();
    }

    public void detectCollisions() {
        collisionDetector.detect();
    }

    public void addHighScore() {
        highScoreContoller.addTime(level.getId(), stopper.getElapsedTime());
    }

    public void startOrResetStopper() {
        stopper.startOrReset();
    }

    public String getFormattedElapsedTime() {
        return stopper.getFormattedElapsedTime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String selectedLevelId = getIntent().getExtras().getString("selectedLevelId");
        xmlMapParser = new XmlLevelParser(this);

        level = createLevel(selectedLevelId);
        ball = new Ball(level.getStartX(), level.getStartY());
        sensor = new SensorController(this, ball);
        collisionDetector = new CollisionDetector(ball, level);
        highScoreContoller = new HighScoreContoller(this);
        stopper = new Stopwatch();
        gameView = new GameView(this);
        setContentView(gameView);
    }

    public void nextLevel() {
        String nextLevelId = level.getNextLevelId();
        if (nextLevelId.equals("gameCompleted")) {
            congrats();
        }
        else {
            level = createLevel(nextLevelId);
        }
    }

    public void restart() {
        gameView.pause();
        ball.setToStartPosition(level.getStartX(), level.getStartY());
        stopper.startOrReset();
        collisionDetector = new CollisionDetector(ball, level);
        highScoreContoller = new HighScoreContoller(this);
        gameView.resume();
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

    private Level createLevel(String levelId) {
        return xmlMapParser.getParsedLevel(levelId);
    }

    private void congrats() {
    }
}
