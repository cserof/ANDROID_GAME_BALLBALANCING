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

    private GameActivity gameContext;
    private Ball ball;
    private Level lvl;

    /**
    * True if there were collision and the ball is still overlapping the wall
    */
    private boolean justCollided;

    /**
     * If there were collision it contains the reference of the wall
     * it helps to check whether the ball is still overlapping the wall
     */
    private MapElement lastCollisionObject;

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

    /**
     * If there were collision and the ball is still overlapping the wall
     * there is no another collision detection
     */
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

    /**
    / Iterates through all the map elements and checks their positions reference to the ball
     / Except ones whose mapState doesn't match the actual state of the game.
     */
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

    /**
     * If the wall is damaging isGameLost is set true
     * otherwise it bounce back - reversing the sign of the velocity vector's x or y coordinate
     * depending on the orientation of collision
     *
     * @param   element   the element ball has collided to
     *                    helps determine the direction of bounding
     */
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

    /**
     * Returns true if ball and the element parameter is intersecting each other
     *
     * @param   element   The element object whose intersection with the ball is to be tested
     * @return Returns true if ball and the element parameter is intersecting each other
     */
    private boolean isCollided(MapElement element) {
        float ballX = ball.getPositionX();
        float ballY = ball.getPositionY();
        float ballRadius = ball.getRadius();

        return element.getRight() >= ballX - ballRadius &&
                element.getLeft() <= ballX + ballRadius &&
                element.getTop() <= ballY + ballRadius &&
                element.getBottom() >= ballY - ballRadius;
    }

    /**
     * Returns true if ball and the element parameter is overlapping at least
     * the portion of overlapForWinning
     *
     * @param   element   The element object whose overlapping to the ball is to be tested
     * @return true if ball and the element parameter is overlapping at least
     * the portion of overlapForWinning
     */
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
