package nik.uniobuda.hu.balancingball.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import nik.uniobuda.hu.balancingball.view.GameView;
import nik.uniobuda.hu.balancingball.logic.CollisionDetector;
import nik.uniobuda.hu.balancingball.logic.HighScoreController;
import nik.uniobuda.hu.balancingball.logic.SensorController;
import nik.uniobuda.hu.balancingball.logic.Stopwatch;
import nik.uniobuda.hu.balancingball.model.Ball;
import nik.uniobuda.hu.balancingball.model.Level;
import nik.uniobuda.hu.balancingball.util.MapState;
import nik.uniobuda.hu.balancingball.util.XmlLevelParser;

/**
 * Created by cserof on 11/12/2017.
 * Includes and controls the main components of the game.
 */
public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private Ball ball;
    private Level level;
    private SensorController sensor;
    private Stopwatch stopper;
    private CollisionDetector collisionDetector;
    private HighScoreController highScoreContoller;
    private boolean isGameLost;
    private boolean isGameWon;
    private XmlLevelParser xmlMapParser;
    private MapState mapState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String selectedLevelId = getIntent().getExtras().getString("selectedLevelId");
        xmlMapParser = new XmlLevelParser(this);

        stopper = new Stopwatch();
        level = createLevel(selectedLevelId);
        ball = new Ball(level.getStartX(), level.getStartY());
        sensor = new SensorController(this, ball);
        initLevel();
        gameView = new GameView(this);
        setContentView(gameView);
    }

    public MapState getMapState() {
        return mapState;
    }

    public Ball getBall() {
        return ball;
    }

    public Level getLevel() {
        return level;
    }

    public boolean isGameLost() {
        return isGameLost;
    }

    public boolean isGameWon() {
        return isGameWon;
    }

    public void setGameLost(boolean gameLost) {
        isGameLost = gameLost;
    }

    public void setGameWon(boolean gameWon) {
        isGameWon = gameWon;
        if (isGameWon) {
            stopper.freeze();
            addHighScore();
        }
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

    public String getFormattedElapsedTime() {
        return stopper.getFormattedElapsedTime();
    }

    public void changeMapState() {
        if (mapState == MapState.STATE0) {
            mapState = MapState.STATE1;
        }
        else {
            mapState = MapState.STATE0;
        }
    }

    public void nextLevel() {
        String nextLevelId = level.getNextLevelId();
        if (nextLevelId.equals("gameCompleted")) {
            congrats();
        }
        else {
            level = createLevel(nextLevelId);
            restart();
        }
    }

    public void restart() {
        gameView.pause();
        initLevel();
        gameView.resume();
    }

    @Override
    protected void onResume() {
        super.onResume();

        gameView.resume();
        restart();
        sensor.registerSensors();
    }

    @Override
    protected void onPause() {
        super.onPause();

        gameView.pause();
        sensor.unregisterSensors();
    }

    private void initLevel() {
        collisionDetector = new CollisionDetector(this, ball, level);
        highScoreContoller = new HighScoreController(this);
        ball.setToStartPosition(level.getStartX(), level.getStartY());
        mapState = MapState.STATE0;
        isGameLost = false;
        isGameWon = false;
        stopper.startOrReset();
        showLevelMessage();
    }

    private Level createLevel(String levelId) {
        return xmlMapParser.getParsedLevel(levelId);
    }

    private void congrats() {
        Toast toast = Toast.makeText(this, "Awesome! You've completed the game!", Toast.LENGTH_LONG);
        toast.show();
        Intent i = new Intent(GameActivity.this, HighscoreActivity.class);
        startActivity(i);
    }

    public void showLevelMessage() {
        Toast toast = Toast.makeText(this, level.getLevelMsg(), Toast.LENGTH_LONG);
        toast.show();
    }
}
