package nik.uniobuda.hu.balancingball;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.Xml;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import nik.uniobuda.hu.balancingball.model.Ball;
import nik.uniobuda.hu.balancingball.model.Level;
import nik.uniobuda.hu.balancingball.model.Point3D;
import nik.uniobuda.hu.balancingball.util.mapElement;
import nik.uniobuda.hu.balancingball.util.mapType;


/**
 * Created by cserof on 11/12/2017.
 */

public class GameView extends SurfaceView implements Runnable {

    static final long targetFps = 25 ;
    static final long gameCyclePeriod = 40;

    Thread gameThread = null;
    SurfaceHolder surfaceHolder;
    volatile boolean playing;
    Canvas canvas;
    Paint paint;
    Level lvl;
    Ball ball;

    long fps;
    private long timeThisFrame;

    public GameView(Context context, Ball ball) {
        super(context);

        this.ball = ball;
        surfaceHolder = getHolder();
        paint = new Paint();
        playing = true;
        init();
    }

    private void init() {
        //xml parse

        try {
            xmlParsing();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void xmlParsing() throws XmlPullParserException, IOException {
        XmlResourceParser xrp = getResources().getXml(R.xml.map);
        int eventType = xrp.getEventType();

        int levelNumber = 0;
        String levelMsg = "";
        ArrayList<mapElement> mapElements = new ArrayList<mapElement>();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
                Log.d("BB", "XML parsing...");
            }
            if (eventType == XmlPullParser.START_TAG) {
                String name = xrp.getName();
                if (name.equals("levelNumber")) {
                    levelNumber = Integer.parseInt(xrp.nextText());
                    Log.d("BB", "Name: levelNumber, text: " + levelNumber);
                }
                if (name.equals("levelMsg")) {
                    levelMsg = xrp.nextText();
                    Log.d("BB", "Name: levelMsg, text: " + levelMsg);
                }
                if (name.equals("mapElement")) {

                    double x = Double.parseDouble(xrp.getAttributeValue(null, "x"));
                    double y = Double.parseDouble(xrp.getAttributeValue(null, "y"));
                    double height = Double.parseDouble(xrp.getAttributeValue(null, "height"));
                    double width = Double.parseDouble(xrp.getAttributeValue(null, "width"));
                    String type = xrp.getAttributeValue(null, "type");

                    boolean bottomDmg = xrp.getAttributeBooleanValue(null, "bottomDmg",false);
                    boolean topDmg = xrp.getAttributeBooleanValue(null, "topDmg",false);
                    boolean	rightDmg = xrp.getAttributeBooleanValue(null, "rightDmg",false);
                    boolean	leftDmg = xrp.getAttributeBooleanValue(null, "leftDmg",false);

                    mapType mt = null;
                    switch (type) {
                        case "WALL" :
                            mt = mapType.WALL;
                            break;
                        case "START":
                            mt = mapType.START;
                            break;
                        case "FINISH":
                            mt = mapType.FINISH;
                            break;
                        default:
                            break;
                    }
                    Log.d("BB", "Name: mapElement");
                    mapElements.add(new mapElement(x,y,height, width,mt, bottomDmg, topDmg, rightDmg, leftDmg));
                }
            }
            eventType = xrp.next();
        }
        lvl = new Level(levelNumber, levelMsg, mapElements);
    }


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
