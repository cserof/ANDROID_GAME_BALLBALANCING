package nik.uniobuda.hu.balancingball.util;

import android.content.Context;
import android.content.res.XmlResourceParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nik.uniobuda.hu.balancingball.R;
import nik.uniobuda.hu.balancingball.model.Level;
import nik.uniobuda.hu.balancingball.model.LevelInfo;
import nik.uniobuda.hu.balancingball.model.MapElement;
import nik.uniobuda.hu.balancingball.model.StateDependentElement;

import static nik.uniobuda.hu.balancingball.util.MapElementType.FINISH;
import static nik.uniobuda.hu.balancingball.util.MapElementType.START;
import static nik.uniobuda.hu.balancingball.util.MapElementType.WALL;

/**
 * Created by cserof on 11/16/2017.
 * xml parsing:
 * https://www.ibm.com/developerworks/xml/library/x-android/
 */

public class XmlLevelParser {

    private Context context;

    public XmlLevelParser(Context context) {
        this.context = context;
    }

    public List<LevelInfo> getParsedLevelInfos() {
        List<LevelInfo> levelInfos = null;
        try {
            levelInfos = xmlParsingLevelInfos();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return levelInfos;
    }

    public Level getParsedLevel(String selectedLevelId) {
        Level lvl = null;
        try {
            lvl = xmlParsingLevel(selectedLevelId);
        } catch (XmlPullParserException e) {
            //todo szint nem elérhető hibát adni usernek
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lvl;
    }

    private Level xmlParsingLevel(String selectedLevelId) throws XmlPullParserException, IOException {
        int resourceId = context.getResources().getIdentifier(selectedLevelId, "xml", context.getPackageName());
        XmlResourceParser xrp = context.getResources().getXml(resourceId);
        int eventType = xrp.getEventType();

        String id = "";
        float startX = 0;
        float startY = 0;
        float width = 0;
        float height = 0;
        String levelMsg = "";
        String nextLevelId = "";
        ArrayList<MapElement> mapElements = new ArrayList<MapElement>();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String name = xrp.getName();
                if (name.equals("id")) {
                    id = String.valueOf(xrp.nextText());
                }
                if (name.equals("levelMsg")) {
                    levelMsg = xrp.nextText();
                }
                if (name.equals("nextLevelId")) {
                    nextLevelId = xrp.nextText();
                }
                if (name.equals("startX")) {
                    startX = Float.parseFloat(xrp.nextText());
                }
                if (name.equals("startY")) {
                    startY = Float.parseFloat(xrp.nextText());
                }
                if (name.equals("width")) {
                    width = Float.parseFloat(xrp.nextText());
                }
                if (name.equals("height")) {
                    height = Float.parseFloat(xrp.nextText());
                }
                if (name.equals("mapElement")) {
                    mapElements.add(parseLevelElement(xrp));
                }
                if (name.equals("stateDependentElements")) {
                    mapElements.add(parseStateDependentElement(xrp));
                }
            }
            eventType = xrp.next();
        }
        Level lvl = new Level(id, levelMsg, nextLevelId, mapElements, startX, startY, width, height);
        return lvl;
    }

    private List<LevelInfo> xmlParsingLevelInfos() throws XmlPullParserException, IOException {
        XmlResourceParser xrp = context.getResources().getXml(R.xml.level_info);
        int eventType = xrp.getEventType();

        List<LevelInfo> levelInfos = new ArrayList<LevelInfo>();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String name = xrp.getName();
                if (name.equals("level")) {
                    levelInfos.add(parseInfo(xrp));
                }
            }
            eventType = xrp.next();
        }
        return levelInfos;
    }

    private LevelInfo parseInfo(XmlResourceParser xrp) throws XmlPullParserException, IOException {
        String id =  xrp.getAttributeValue(null, "id");
        String levelName = xrp.getAttributeValue(null, "name");

        LevelInfo lvlInfo = new LevelInfo(id, levelName);
        return lvlInfo;
    }

    private MapElement parseLevelElement(XmlResourceParser xrp) {
        float left = Float.parseFloat(xrp.getAttributeValue(null, "left"));
        float top = Float.parseFloat(xrp.getAttributeValue(null, "top"));
        float right = Float.parseFloat(xrp.getAttributeValue(null, "right"));
        float bottom = Float.parseFloat(xrp.getAttributeValue(null, "bottom"));
        String type = xrp.getAttributeValue(null, "type");

        boolean isDamage = xrp.getAttributeBooleanValue(null, "isDamage",false);

        MapElementType mt = null;
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
        return new MapElement(left,top,right, bottom,mt, isDamage);
    }

    private StateDependentElement parseStateDependentElement(XmlResourceParser xrp) {
        float left = Float.parseFloat(xrp.getAttributeValue(null, "left"));
        float top = Float.parseFloat(xrp.getAttributeValue(null, "top"));
        float right = Float.parseFloat(xrp.getAttributeValue(null, "right"));
        float bottom = Float.parseFloat(xrp.getAttributeValue(null, "bottom"));
        String type = xrp.getAttributeValue(null, "type");
        String state = xrp.getAttributeValue(null, "state");
        boolean isDamage = xrp.getAttributeBooleanValue(null, "isDamage",false);

        MapElementType mt = null;
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

        MapState ms = null;
        switch (state) {
            case "STATE0":
                ms = MapState.STATE0;
                break;
            case "STATE1":
                ms = MapState.STATE1;
                break;
            default:
                break;
        }
        return new StateDependentElement(left,top,right, bottom,mt, isDamage, ms);
    }
}
