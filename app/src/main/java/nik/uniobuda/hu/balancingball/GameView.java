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


/**
 * Created by cserof on 11/12/2017.
 */

public class GameView extends SurfaceView implements Runnable {

    private static final long gameCyclePeriod = 40;

    private long fps;
    private long timeThisFrame;

    private Level level;
    private Ball ball;
    private CollisionDetector cd;

    private Context context;
    private Thread gameThread = null;
    private SurfaceHolder surfaceHolder;
    private volatile boolean playing;
    private Canvas canvas;
    private Paint paint;

    private float scale;
    private float horizontalOffset;
    private float verticalOffset;
    
    
    //// TODO: 11/20/2017 kiszervezni xml-be 
    private static final float mapWidth = 1125;
    private static final float mapHeight = 2000;

    public GameView(Context context, Ball ball, Level lvl) {
        super(context);

        this.context = context;
        this.ball = ball;
        this.level = lvl;
        this.cd = new CollisionDetector(ball, level);
        surfaceHolder = getHolder();
        paint = new Paint();
        playing = true;
        init();
    }

    private void init() {
        getScreenSize();
        calcScale();
        //surfaceHolder.addCallback(new MyCallback());
    }

    private void calcScale() {
        int screenWidth;
        int screenHeight;

        Point size = getScreenSize();
        screenWidth = size.x;
        screenHeight = size.y;

        float mapRatio = mapWidth / mapHeight;
        float screenRatio = screenWidth / screenHeight;

        verticalOffset = 0;
        horizontalOffset = 0;

        if (mapRatio > screenRatio) {
            scale = screenWidth / mapWidth;
            verticalOffset = (screenHeight - scale*mapHeight)/2;
        }
        else {
            scale = screenWidth / mapHeight;
            horizontalOffset = (screenWidth - scale*mapWidth)/2;
        }
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
        float drawnX = ball.getPositionX()*scale + horizontalOffset;
        float drawnY = ball.getPositionY()*scale + verticalOffset;

        paint.setColor(Color.RED);
        canvas.drawCircle(
                drawnX,
                drawnY,
                ball.getRadius()*scale,
                paint);

        paint.setColor(Color.BLACK);

        ArrayList<Point3D> points = ball.getPoints();
        for (Point3D point : points) {
            if (point.getDisplayedZ() > 0) {
                canvas.drawCircle(
                        drawnX + point.getDisplayedX()*scale,
                        drawnY + point.getDisplayedY()*scale,
                        3,
                        paint);
            }
        }
    }

    private void drawLevelBackground() {
        for (MapElement element : level.getMapElements()) {
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
                    element.getLeft()*scale + horizontalOffset,
                    element.getTop()*scale + verticalOffset,
                    element.getRight()*scale + horizontalOffset,
                    element.getBottom()*scale + verticalOffset,
                    paint
            );
        }
    }

    private Point getScreenSize() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }


    private void update() {
        if (!cd.isJustCollided()) {
            ball.accelerate();
        }
        ball.roll();
        cd.detect();
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
