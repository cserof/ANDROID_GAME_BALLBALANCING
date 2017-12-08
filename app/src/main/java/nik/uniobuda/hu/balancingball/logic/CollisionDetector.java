package nik.uniobuda.hu.balancingball.logic;

import android.content.Context;
import android.util.Log;

import nik.uniobuda.hu.balancingball.activity.GameActivity;
import nik.uniobuda.hu.balancingball.model.Ball;
import nik.uniobuda.hu.balancingball.model.Level;
import nik.uniobuda.hu.balancingball.model.MapElement;
import nik.uniobuda.hu.balancingball.model.StateDependentElement;

/**
 * Created by cserof on 11/20/2017.
 */

public class CollisionDetector {

    private Ball ball;
    private Level lvl;
    private MapElement lastCollisionObject;
    private boolean justCollided;
    private GameActivity gameContext;

    public CollisionDetector(Context context, Ball ball, Level level) {
        this.gameContext = (GameActivity) context;
        this.ball = ball;
        this.lvl = level;
        lastCollisionObject = null;
        justCollided = false;

    }

    public void detect() {
        Log.d("BB", "Collision detection: Hi from thread: " +  Thread.currentThread().getName());
        if (isJustCollided()) {
            if (!isCollided(lastCollisionObject)) {
                justCollided = false;
            }
        }
        else {
            iterateElementsToDetect();
        }
    }

    private void iterateElementsToDetect() {
        for (MapElement element : lvl.getMapElements()) {
            if (!(element instanceof StateDependentElement) ||
                    ((StateDependentElement) element).getState() == gameContext.getMapState()
                    ) {
                switch (element.getType()) {
                    case WALL:
                        if (isCollided(element)) {
                            collisionOnWall(element);
                        }
                        break;
                    case FINISH:
                        if (isIncluded(element)) {
                            gameContext.setGameWon(true);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void collisionOnWall(MapElement element) {
        justCollided = true;
        lastCollisionObject = element;

        if (element.isDamage()) {
            gameContext.setGameLost(true);
        }
        else {
            boolean isHorizontalCollision = ball.getPositionX() > element.getLeft() &&
                    ball.getPositionX() < element.getRight();

            if (isHorizontalCollision) {
                ball.bounceOnHorizontal();
            }
            else {
                ball.bounceOnVertical();
            }
        }
    }

    public boolean isJustCollided() {
        return justCollided;
    }

    private boolean isCollided(MapElement element) {
        float ballX = ball.getPositionX();
        float ballY = ball.getPositionY();
        float ballRadius = ball.getRadius();

        return element.getRight() >= ballX - ballRadius &&
                element.getLeft() <= ballX + ballRadius &&
                element.getTop() <= ballY + ballRadius &&
                element.getBottom() >= ballY - ballRadius;
    }

    private boolean isIncluded(MapElement element) {
        float ballX = ball.getPositionX();
        float ballY = ball.getPositionY();
        float ballRadius = ball.getRadius();

        float overlapForWinning = 0.6f;

        return element.getRight() >= ballX + ballRadius*overlapForWinning &&
                element.getLeft() <= ballX - ballRadius*overlapForWinning &&
                element.getTop() <= ballY - ballRadius*overlapForWinning &&
                element.getBottom() >= ballY + ballRadius*overlapForWinning;
    }
}
