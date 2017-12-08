package nik.uniobuda.hu.balancingball.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;

import nik.uniobuda.hu.balancingball.R;
import nik.uniobuda.hu.balancingball.activity.GameActivity;
import nik.uniobuda.hu.balancingball.model.MapElement;
import nik.uniobuda.hu.balancingball.model.Point3D;
import nik.uniobuda.hu.balancingball.model.StateDependentElement;
import nik.uniobuda.hu.balancingball.util.Palette;


/**
 * Created by cserof on 11/12/2017.
 * gameView:
 * https://forum.xda-developers.com/android/software/tutorial-create-running-game-animation-t3473191
 */

public class GameView extends SurfaceView implements Runnable {

    private static final long gameCyclePeriod = 40;

    private long fps;

    private GameActivity gameContext;
    private Thread gameThread = null;
    private SurfaceHolder surfaceHolder;
    private volatile boolean playing;
    private Canvas canvas;

    private Paint fillPaint;
    private Paint strokePaint;
    private Rect timerTextBounds;

    private int viewHeight;
    private int viewWidth;

    private float scale;
    private float horizontalOffset;
    private float verticalOffset;

    private Palette palette;

    public GameView(Context context) {
        super(context);

        this.gameContext = (GameActivity) context;
        surfaceHolder = getHolder();
        palette = new Palette(context);
        init();
    }

    @Override
    public void run() {

        while (playing) {
            long startFrameTime = System.currentTimeMillis();
            update();
            draw();
            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            try {
                long sleepTime = gameCyclePeriod - timeThisFrame;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (gameContext.isGameLost() || gameContext.isGameWon()) {
                playing = false;
            }
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        calcScale(w, h);
    }

    private void init() {
        initPaints();
        gameContext.startOrResetStopper();
        playing = true;
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playing) {
                    if (gameContext.isGameWon()) {
                        gameContext.nextLevel();
                    }
                    gameContext.restart();
                }
                else {
                    gameContext.changeMapState();
                }
            }
        });
        gameContext.showLevelMessage();
    }

    private void initPaints() {
        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(palette.getColorStroke());
    }

    private void calcScale(int w, int h) {
        float mapWidth = gameContext.getLevel().getWidth();
        float mapHeight = gameContext.getLevel().getHeight();

        float mapRatio = mapWidth / mapHeight;
        float screenRatio = w/h;

        verticalOffset = 0;
        horizontalOffset = 0;

        if (mapRatio > screenRatio) {
            scale = w / mapWidth;
            verticalOffset = (h - scale*mapHeight)/2;
        }
        else {
            scale = w / mapHeight;
            horizontalOffset = (h - scale*mapWidth)/2;
        }
    }

    private void update() {
        if (!gameContext.isBallJustCollided()) {
            gameContext.getBall().accelerate();
        }
        gameContext.getBall().roll();
        gameContext.detectCollisions();
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(palette.getColorBackground());

            drawLevelBackground();
            drawMovingObjects();
            drawElapsedTime();

            if (gameContext.isGameLost()) {
                String lost = getResources().getString(R.string.lost);
                drawEndGameMessage(lost);
            }
            else if (gameContext.isGameWon()) {
                String won = getResources().getString(R.string.won);
                drawEndGameMessage(won);
            }

            Log.d("BB", "Drawing: Hi from thread: " +  Thread.currentThread().getName());
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawElapsedTime() {
        String caption = gameContext.getFormattedElapsedTime();
        float fontsize = 24; // sp
        float fontsizeInPixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, fontsize, getResources().getDisplayMetrics());
        timerTextBounds = new Rect();
        fillPaint.setTextSize(fontsizeInPixel);
        fillPaint.getTextBounds(caption, 0, caption.length()-1, timerTextBounds );
        int offset = timerTextBounds.bottom - timerTextBounds.top;
        canvas.drawText(caption, 0, offset, fillPaint);
    }

    private void showDebugInfo() {
        fillPaint.setTextSize(45);
        canvas.drawText("FPS:" + fps, 20, 40, fillPaint);
        canvas.drawText("x:  " + gameContext.getBall().getPositionX(), 20, 90, fillPaint);
        canvas.drawText("y: " +  gameContext.getBall().getPositionY(), 20, 140, fillPaint);
    }

    private void drawMovingObjects() {
        float drawnX = gameContext.getBall().getPositionX()*scale + horizontalOffset;
        float drawnY = gameContext.getBall().getPositionY()*scale + verticalOffset;

        fillPaint.setColor(palette.getColorBall());
        canvas.drawCircle(
                drawnX,
                drawnY,
                gameContext.getBall().getRadius()*scale,
                fillPaint);
        canvas.drawCircle(
                drawnX,
                drawnY,
                gameContext.getBall().getRadius()*scale,
                strokePaint);

        fillPaint.setColor(palette.getColorDotOnBall());

        ArrayList<Point3D> points = gameContext.getBall().getPoints();
        for (Point3D point : points) {
            if (point.getDisplayedZ() > 0) {
                canvas.drawCircle(
                        drawnX + point.getDisplayedX()*scale,
                        drawnY + point.getDisplayedY()*scale,
                        gameContext.getBall().getDotSize()*scale,
                        fillPaint);
            }
        }
    }

    private void drawLevelBackground() {
        for (MapElement element : gameContext.getLevel().getMapElements()) {
            if (
                    !(element instanceof StateDependentElement) ||
                    ((StateDependentElement) element).getState() == gameContext.getMapState()
                    ) {
                switch (element.getType()) {
                    case FINISH:
                        fillPaint.setColor(palette.getColorFinish());
                        break;
                    case START:
                        fillPaint.setColor(palette.getColorStart());
                        break;
                    case WALL:
                        if (element.isDamage()) {
                            fillPaint.setColor(palette.getColorDamagingWall());
                        } else {
                            fillPaint.setColor(palette.getColorWall());
                        }
                        break;
                }
                canvas.drawRect(
                        element.getLeft() * scale + horizontalOffset,
                        element.getTop() * scale + verticalOffset,
                        element.getRight() * scale + horizontalOffset,
                        element.getBottom() * scale + verticalOffset,
                        fillPaint
                );
                canvas.drawRect(
                        element.getLeft() * scale + horizontalOffset,
                        element.getTop() * scale + verticalOffset,
                        element.getRight() * scale + horizontalOffset,
                        element.getBottom() * scale + verticalOffset,
                        strokePaint
                );
            }
        }
    }

    private void drawEndGameMessage(String message) {
        fillPaint.setColor(palette.getColorText());
        fillPaint.setTextSize(viewHeight/12);
        int xPos = (viewWidth / 2);
        int yPos = (int) ((viewHeight / 2) - ((fillPaint.descent() + fillPaint.ascent()) / 2)) ;
        fillPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(message, xPos, yPos, fillPaint);
        fillPaint.setTextAlign(Paint.Align.LEFT);
    }
}
