package nik.uniobuda.hu.balancingball.logic;

import nik.uniobuda.hu.balancingball.model.Ball;
import nik.uniobuda.hu.balancingball.model.Level;
import nik.uniobuda.hu.balancingball.model.MapElement;
import nik.uniobuda.hu.balancingball.util.mapType;

/**
 * Created by cserof on 11/20/2017.
 */

public class CollisionDetector {

    private Ball ball;
    private Level lvl;
    private MapElement lastCollisionObject;
    private boolean justCollided;

    public CollisionDetector(Ball ball, Level level) {
        this.ball = ball;
        this.lvl = level;
        lastCollisionObject = null;
    }

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
            if (isCollided(element)) {
                switch (element.getType()) {
                    case WALL:
                        collisionOnWall(element);
                }
            }
        }
    }

    private void collisionOnWall(MapElement element) {
        justCollided = true;
        lastCollisionObject = element;

        if (element.isDamage()) {
            ball.setDamaged(true);
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
}
