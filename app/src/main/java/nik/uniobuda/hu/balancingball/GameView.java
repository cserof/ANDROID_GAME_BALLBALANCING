package nik.uniobuda.hu.balancingball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import java.util.ArrayList;
import nik.uniobuda.hu.balancingball.model.Ball;
import nik.uniobuda.hu.balancingball.model.Level;
import nik.uniobuda.hu.balancingball.model.MapElement;
import nik.uniobuda.hu.balancingball.model.Point3D;
import nik.uniobuda.hu.balancingball.util.XmlMapParser;


/**
 * Created by cserof on 11/12/2017.
 */

public class GameView extends SurfaceView implements Runnable {

    static final long targetFps = 25 ;
    static final long gameCyclePeriod = 40;

    Context context;
    Thread gameThread = null;
    SurfaceHolder surfaceHolder;
    volatile boolean playing;
    Canvas canvas;
    Paint paint;
    Level lvl;
    Ball ball;

    long fps;
    private long timeThisFrame;

    int screenWidth;
    int screenHeight;

    public GameView(Context context, Ball ball) {
        super(context);

        this.context = context;
        this.ball = ball;
        surfaceHolder = getHolder();
        paint = new Paint();
        playing = true;
        init();
    }

    private void init() {
        XmlMapParser parser = new XmlMapParser(context);
        lvl = parser.getParsedMap();
        getScreenSize();
        //surfaceHolder.addCallback(new MyCallback());
    }

    //// TODO: 11/16/2017
    //statikus részeket csak egyszer induláskor - de legalább ne számolja ki minden ciklusban
    // https://stackoverflow.com/questions/11490711/android-holder-getsurface-always-return-null
    /*public class MyCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format,
                                   int width, int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            drawLevelBackground();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // and here you need to stop it
        }

    }
*/

    @Override
    public void run() {

        while (playing) {
            long startFrameTime = System.currentTimeMillis();
            update();
            draw();
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            try {
                long sleepTime = gameCyclePeriod - timeThisFrame;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //testing fps kiiratas
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

            drawLevelBackground();
            drawMovingObjects();

            //for testing
            showDebugInfo();

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void showDebugInfo() {
        paint.setTextSize(45);
        canvas.drawText("FPS:" + fps, 20, 40, paint);
        canvas.drawText("x:  " + ball.getPositionX(), 20, 90, paint);
        canvas.drawText("y: " +  ball.getPositionY(), 20, 140, paint);
    }

    private void drawMovingObjects() {
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
    }

    private void drawLevelBackground() {
        for (MapElement element : lvl.getMapElements()) {
            switch (element.getType()) {
                case FINISH :
                    paint.setColor(Color.GREEN);
                    break;
                case START:
                    paint.setColor(Color.BLUE);
                    break;
                case WALL:
                    paint.setColor(Color.LTGRAY);
                    break;
            }
            canvas.drawRect(
                    screenWidth * element.getLeft() / 100,
                    screenHeight * element.getTop() / 100,
                    screenWidth * element.getRight() / 100,
                    screenHeight * element.getBottom() / 100,
                    paint
            );
        }
    }

    private void getScreenSize() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }


    private void update() {
        ball.accelerate();
        ball.roll();
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
