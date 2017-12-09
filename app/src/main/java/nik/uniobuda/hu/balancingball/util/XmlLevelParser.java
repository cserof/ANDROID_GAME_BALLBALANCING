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
 * Reading Level and LevelInfo xml files and creating equivalent game objects.
 * xml parsing with XmlPullParser based on:
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lvl;
    }

    /**
     * Reads a specific xml resource of a map and creates the Level object
     * @param selectedLevelId level to be parsed - the name of the xml resource must be the same
     * @return Level object equivalent to the parsed xml
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Level xmlParsingLevel(String selectedLevelId) throws XmlPullParserException, IOException {
        int resourceId = context.getResources().getIdentifier(selectedLevelId, "xml", context.getPackageName());
        XmlResourceParser xrp = context.getResources().getXml(resourceId);
        int eventType = xrp.getEventType();

        String id = null;
        float startX = 0;
        float startY = 0;
        float width = 0;
        float height = 0;
        String levelMsg = null;
        String nextLevelId = null;
        ArrayList<MapElement> mapElements = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String name = xrp.getName();
                if (name.equals("id")) {
                    id = String.valueOf(xrp.nextText());
                }
                else if (name.equals("levelMsg")) {
                    levelMsg = xrp.nextText();
                }
                else if (name.equals("nextLevelId")) {
                    nextLevelId = xrp.nextText();
                }
                else if (name.equals("startX")) {
                    startX = Float.parseFloat(xrp.nextText());
                }
                else if (name.equals("startY")) {
                    startY = Float.parseFloat(xrp.nextText());
                }
                else if (name.equals("width")) {
                    width = Float.parseFloat(xrp.nextText());
                }
                else if (name.equals("height")) {
                    height = Float.parseFloat(xrp.nextText());
                }
                else if (name.equals("mapElements")) {
                    mapElements = parseLevelElements(xrp);
                }
                else if (name.equals("stateDependentElements")) {
                    mapElements.add(parseStateDependentElement(xrp));
                }
            }
            eventType = xrp.next();
        }
        return new Level(id, levelMsg, nextLevelId, mapElements, startX, startY, width, height);
    }

    /**
     * Reads xml resource of level descriptions from resource "level_info.xml" and creates a list of LevelInfo objects.
     * containing information about all the levels.
     * @return List of LevelInfo objects containing information about all the levels (ids and names).
     * @throws XmlPullParserException
     * @throws IOException
     */
    private List<LevelInfo> xmlParsingLevelInfos() throws XmlPullParserException, IOException {
        XmlResourceParser xrp = context.getResources().getXml(R.xml.level_info);
        int eventType = xrp.getEventType();

        List<LevelInfo> levelInfos = new ArrayList<>();

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

    /**
     * Parse a Level item of level_info.xml and creates the equivalent LevelInfo object.
     * @param xrp PullParser reference - actual state of the parsing process
     * @return LevelInfo object containing information about a specific level (ids and names).
     * @throws XmlPullParserException
     * @throws IOException
     */
    private LevelInfo parseInfo(XmlResourceParser xrp) throws XmlPullParserException, IOException {
        String id =  xrp.getAttributeValue(null, "levelId");
        String levelName = xrp.getAttributeValue(null, "name");

        return new LevelInfo(id, levelName);
    }

    /**
     *
     * @param xrp PullParser reference - actual state of the parsing process
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    private ArrayList<MapElement> parseLevelElements(XmlResourceParser xrp) throws XmlPullParserException, IOException {
        ArrayList<MapElement> mapElements = new ArrayList<>();
        int eventType = xrp.getEventType();
        boolean done = false;
        while (!done) {
            if (eventType == XmlPullParser.START_TAG) {
                String name = xrp.getName();
                if (name.equals("mapElement")) {
                    mapElements.add(parseStateIndependentElement(xrp));
                }
                else if (name.equals("stateDependentElement")) {
                    mapElements.add(parseStateDependentElement(xrp));
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (xrp.getName().equals("mapElements")) {
                    done = true;
                }
            }
            eventType = xrp.next();
        }
        return mapElements;
    }

    /**
     *
     * @param xrp PullParser reference - actual state of the parsing process
     * @return
     */
    private MapElement parseStateIndependentElement(XmlResourceParser xrp) {
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

    /**
     *
     * @param xrp PullParser reference - actual state of the parsing process
     * @return
     */
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
