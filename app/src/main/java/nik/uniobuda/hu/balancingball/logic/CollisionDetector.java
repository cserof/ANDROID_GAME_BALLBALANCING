package nik.uniobuda.hu.balancingball.logic;

import android.content.Context;
import nik.uniobuda.hu.balancingball.activity.GameActivity;
import nik.uniobuda.hu.balancingball.model.Ball;
import nik.uniobuda.hu.balancingball.model.Level;
import nik.uniobuda.hu.balancingball.model.MapElement;
import nik.uniobuda.hu.balancingball.model.StateDependentElement;

/**
 * Created by cserof on 11/20/2017.
 * Implements simple collision detection
 * iterates through list of mapelements and checks collision on them
 */

public class CollisionDetector {

    private Ball ball;
    private Level lvl;

    private boolean justCollided;

    //if there were collision it contains the reference of the wall
    // it helps to check whether the ball is still overlapping the wall
    private MapElement lastCollisionObject;
    private GameActivity gameContext;

    public CollisionDetector(Context context, Ball ball, Level level) {
        this.gameContext = (GameActivity) context;
        this.ball = ball;
        this.lvl = level;
        lastCollisionObject = null;
        justCollided = false;
    }

    public boolean isJustCollided() {
        return justCollided;
    }

    // if there were collision and the ball is still overlapping the wall
    // there is no another collision detection
    public void detect() {
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

    //if the wall is damaging isGameLost is set true
    //otherwise it bounce back - reversing the sign of the velocity vector's x or y coordinate
    //depending on the orientation of collision
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

    private boolean isCollided(MapElement element) {
        float ballX = ball.getPositionX();
        float ballY = ball.getPositionY();
        float ballRadius = ball.getRadius();

        return element.getRight() >= ballX - ballRadius &&
                element.getLeft() <= ballX + ballRadius &&
                element.getTop() <= ballY + ballRadius &&
                element.getBottom() >= ballY - ballRadius;
    }

    //ball and the element parameter is overlapping at least
    //the portion of overlapForWinning
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
