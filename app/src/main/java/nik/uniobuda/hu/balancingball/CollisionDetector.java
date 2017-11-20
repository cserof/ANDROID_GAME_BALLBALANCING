package nik.uniobuda.hu.balancingball;

import android.util.Log;

import nik.uniobuda.hu.balancingball.model.Ball;
import nik.uniobuda.hu.balancingball.model.Level;
import nik.uniobuda.hu.balancingball.model.MapElement;
import nik.uniobuda.hu.balancingball.model.Vector2D;
import nik.uniobuda.hu.balancingball.util.mapType;

/**
 * Created by cserof on 11/20/2017.
 */

class CollisionDetector {

    Ball ball;
    Level lvl;
    int lastCollisionTime;
    MapElement lastCollisionObject;

    public CollisionDetector(Ball ball, Level level) {
        this.ball = ball;
        this.lvl = level;
        lastCollisionTime = 0;
        lastCollisionObject = null;
    }

    public void detect() {
        float ballX = ball.getPositionX();
        float ballY = ball.getPositionY();
        float ballRadius = ball.getRadius();

        for (MapElement element : lvl.getMapElements()) {
            if ((!isJustCollided() || lastCollisionObject != element) &&
                    element.getRight() >= ballX - ballRadius &&
                    element.getLeft() <= ballX + ballRadius &&
                    element.getTop() <= ballY + ballRadius &&
                    element.getBottom() >= ballY - ballRadius
                    ) {

                lastCollisionTime = 0;
                lastCollisionObject = element;

                if (element.getType() == mapType.WALL) {
                    ball.bounce(Math.PI/2);
                }
            }
        }
        lastCollisionTime ++;
    }

    public boolean isJustCollided() {
        return lastCollisionTime <= 4;
    }
}
