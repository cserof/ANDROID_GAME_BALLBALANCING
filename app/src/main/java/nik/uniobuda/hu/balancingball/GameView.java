package nik.uniobuda.hu.balancingball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import nik.uniobuda.hu.balancingball.model.Ball;
import nik.uniobuda.hu.balancingball.model.Point3D;
import nik.uniobuda.hu.balancingball.model.Vector2D;

/**
 * Created by cserof on 11/12/2017.
 */

public class GameView extends SurfaceView implements Runnable {
    Thread gameThread = null;
    SurfaceHolder surfaceHolder;
    volatile boolean playing;
    Canvas canvas;
    Paint paint;

    long fps;
    private long timeThisFrame;

    Ball ball;

    final static int startX = 0;
    final static int startY = 0;


    public GameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        paint = new Paint();

        ball = new Ball(startX, startY);
        playing = true;
    }


    @Override
    public void run() {

        while (playing) {
            long startFrameTime = System.currentTimeMillis();
            update();
            draw();
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame > 0) {
                fps = 1000 / timeThisFrame;
            }
        }

    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);

            paint.setColor(Color.RED);
            canvas.drawCircle(ball.getPositionX(), ball.getPositionY(), ball.getRadius(), paint);

            paint.setColor(Color.BLACK);

            ArrayList<Point3D> points = ball.getPoints();
            for (Point3D point : points) {
                if (point.getDisplayedZ() > 0) {
                    canvas.drawCircle(
                            ball.getPositionX() + point.getDisplayedX(),
                            ball.getPositionY() + point.getDisplayedY(),
                            3,
                            paint);
                }
            }

            paint.setTextSize(45);
            canvas.drawText("FPS:" + fps, 20, 40, paint);
            canvas.drawText("x:  " + ball.getPositionX(), 20, 90, paint);
            canvas.drawText("y: " +  ball.getPositionY(), 20, 140, paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void update() {
        ball.roll();
        if (ball.getPositionX()>1000) {
            ball.accelerate(new Vector2D(-1, -1));
        }
        if (ball.getPositionX() < 100) {
            ball.accelerate(new Vector2D(1, 1));
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
