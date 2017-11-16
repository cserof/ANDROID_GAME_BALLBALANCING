package nik.uniobuda.hu.balancingball.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import nik.uniobuda.hu.balancingball.R;
import nik.uniobuda.hu.balancingball.model.Level;
import nik.uniobuda.hu.balancingball.model.MapElement;

import static nik.uniobuda.hu.balancingball.util.mapType.FINISH;
import static nik.uniobuda.hu.balancingball.util.mapType.START;
import static nik.uniobuda.hu.balancingball.util.mapType.WALL;

/**
 * Created by cserof on 11/16/2017.
 */

public class XmlMapParser {

    Context context;

    public XmlMapParser(Context context) {
        this.context = context;
    }

    public Level getParsedMap() {
        Level lvl = null;
        try {
            lvl = xmlParsing();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lvl;
    }

    private Level xmlParsing() throws XmlPullParserException, IOException {
        XmlResourceParser xrp = context.getResources().getXml(R.xml.map);
        int eventType = xrp.getEventType();

        int levelNumber = 0;
        String levelMsg = "";
        ArrayList<MapElement> mapElements = new ArrayList<MapElement>();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String name = xrp.getName();
                if (name.equals("levelNumber")) {
                    levelNumber = Integer.parseInt(xrp.nextText());
                }
                if (name.equals("levelMsg")) {
                    levelMsg = xrp.nextText();
                }
                if (name.equals("mapElement")) {
                    mapElements.add(parseMapElement(xrp));
                }
            }
            eventType = xrp.next();
        }
        Level lvl = new Level(levelNumber, levelMsg, mapElements);
        return lvl;
    }

    private MapElement parseMapElement(XmlResourceParser xrp) {
        float left = Float.parseFloat(xrp.getAttributeValue(null, "left"));
        float top = Float.parseFloat(xrp.getAttributeValue(null, "top"));
        float right = Float.parseFloat(xrp.getAttributeValue(null, "right"));
        float bottom = Float.parseFloat(xrp.getAttributeValue(null, "bottom"));
        String type = xrp.getAttributeValue(null, "type");

        boolean bottomDmg = xrp.getAttributeBooleanValue(null, "bottomDmg",false);
        boolean topDmg = xrp.getAttributeBooleanValue(null, "topDmg",false);
        boolean	rightDmg = xrp.getAttributeBooleanValue(null, "rightDmg",false);
        boolean	leftDmg = xrp.getAttributeBooleanValue(null, "leftDmg",false);

        mapType mt = null;
        switch (type) {
            case "WALL" :
                mt = WALL;
                break;
            case "START":
                mt = START;
                break;
            case "FINISH":
                mt = FINISH;
                break;
            default:
                break;
        }
        return new MapElement(left,top,right, bottom,mt, bottomDmg, topDmg, rightDmg, leftDmg);
    }
}
