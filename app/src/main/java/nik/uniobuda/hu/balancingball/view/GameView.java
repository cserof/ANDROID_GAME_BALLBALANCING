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
import nik.uniobuda.hu.balancingball.model.Dot3D;
import nik.uniobuda.hu.balancingball.model.StateDependentElement;
import nik.uniobuda.hu.balancingball.util.Palette;


/**
 * Created by cserof on 11/12/2017.
 * Game cycle and visualization
 * source:
 * https://forum.xda-developers.com/android/software/tutorial-create-running-game-animation-t3473191
 */

public class GameView extends SurfaceView implements Runnable {

    /**
    * 1 game period interval in ms
    * fps = 1000/gameCycLePeriod
     */
    private static final long gameCyclePeriod = 40;

    private GameActivity gameContext;
    private Thread gameThread = null;
    private SurfaceHolder surfaceHolder;
    private volatile boolean playing;
    private Canvas canvas;

    private Paint fillPaint;
    private Paint strokePaint;

    /**
    * rectangle with the exact size the elapsed time text
    * helps to position it
    * inverse coordinates - it's increasing to the top
     */
    private Rect timerTextBounds;

    private int viewHeight;
    private int viewWidth;

    /**
    * ratio of view size and the size of the map
    * it's used to scale down the objects of the map
    * smaller one from ratio of heights or widths
    * it ensures to fit any screen
     */
    private float scale;
    private float horizontalOffset;
    private float verticalOffset;

    /**
    * gets applied colors from xml and store them
     */
    private Palette palette;

    public GameView(Context context) {
        super(context);

        this.gameContext = (GameActivity) context;
        surfaceHolder = getHolder();
        palette = new Palette(context);
        initPaints();
        setOnClickListeners();
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

    private void setOnClickListeners() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playing) {
                    if (gameContext.isGameWon()) {
                        gameContext.nextLevel();
                    }
                    else {
                        gameContext.restart();
                    }
                    playing = true;
                }
                else {
                    gameContext.changeMapState();
                }
            }
        });
    }

    private void initPaints() {
        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(palette.getColorStroke());
    }


    /**
    * Calculates the ratio of view size and the size of the map
    * it's used to scale down the objects of the map
    * smaller one from ratio of heights or widths
    * it ensures to fit any screen
     */
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

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    /**
    * Draws the elapsed time in mm:ss:ff format to the very top left corner of the view
     */
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

    /**
    * Drawing the ball inculuding dots
     */
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

        ArrayList<Dot3D> points = gameContext.getBall().getDots();
        for (Dot3D point : points) {
            if (point.getDisplayedZ() > 0) {
                canvas.drawCircle(
                        drawnX + point.getDisplayedX()*scale,
                        drawnY + point.getDisplayedY()*scale,
                        gameContext.getBall().getDotSize()*scale,
                        fillPaint);
            }
        }
    }

    /**
    * Draws the elements of the map.
    * mapState indenpendent ones and ones with mapState equals to the actual state of the game
     */
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

    /**
    * Draws the message parameter into the center of the screen
     */
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
