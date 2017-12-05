package nik.uniobuda.hu.balancingball.util;

import android.content.Context;

import nik.uniobuda.hu.balancingball.R;

/**
 * Created by cserof on 12/5/2017.
 */

public class Palette {

    private Context context;

    private int colorStroke;
    private int colorBall;
    private int colorWall;
    private int colorDamagingWall;
    private int colorFinish;
    private int colorStart;
    private int colorBackground;
    private int colorText;
    private int colorDotOnBall;

    public Palette(Context context) {
        this.context = context;
        initColors();
    }

    public int getColorStroke() {
        return colorStroke;
    }

    public int getColorBall() {
        return colorBall;
    }

    public int getColorWall() {
        return colorWall;
    }

    public int getColorDamagingWall() {
        return colorDamagingWall;
    }

    public int getColorFinish() {
        return colorFinish;
    }

    public int getColorStart() {
        return colorStart;
    }

    public int getColorBackground() {
        return colorBackground;
    }

    public int getColorText() {
        return colorText;
    }

    public int getColorDotOnBall() {
        return colorDotOnBall;
    }

    private void initColors() {
        colorStroke = context.getResources().getColor(R.color.colorStroke);
        colorBall = context.getResources().getColor(R.color.colorBall);
        colorWall = context.getResources().getColor(R.color.colorWall);
        colorDamagingWall = context.getResources().getColor(R.color.colorDamagingWall);
        colorFinish = context.getResources().getColor(R.color.colorFinish);
        colorStart = context.getResources().getColor(R.color.colorStart);
        colorBackground = context.getResources().getColor(R.color.colorBackground);
        colorText = context.getResources().getColor(R.color.colorInGameText);
        colorDotOnBall = context.getResources().getColor(R.color.colorDotOnBall);
    }
}
