package nik.uniobuda.hu.balancingball.logic;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;

import nik.uniobuda.hu.balancingball.model.Highscore;
import nik.uniobuda.hu.balancingball.util.TimeFormatter;

/**
 * Created by cserof on 11/25/2017.
 */

public class HighScoreContoller {

    HashMap<String, Highscore> highScores;
    Context context;

    public HighScoreContoller(Context context) {
        this.context = context;
        initHighScores();
    }

    /*
    public List<Highscore> getHighScores() {
        ArrayList<Highscore> list = new ArrayList<Highscore>();
        Iterator it = highScores.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry<String, Highscore> pair = (HashMap.Entry<String, Highscore>)it.next();
            list.add(pair.getValue());
            it.remove();
        }
        return list;
    }
*/
    public void addTime(String levelId, long time) {
        if (!highScores.containsKey(levelId)) {
            highScores.put(levelId, new Highscore(levelId, time));
            String xml = writeToXml();
            saveRecordsToFile(xml);
        }
        else if (time < highScores.get(levelId).getBestTime()) {
            highScores.get(levelId).setBestTime(time);
            String xml = writeToXml();
            saveRecordsToFile(xml);
        }
    }

    //https://www.ibm.com/developerworks/xml/library/x-android/
    private String writeToXml() {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "highScores");

            Iterator it = highScores.entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry<String, Highscore> pair = (HashMap.Entry<String, Highscore>)it.next();

                serializer.startTag("", "highScore");
                serializer.attribute("", "id", pair.getKey());
                serializer.attribute("", "bestTime",String.valueOf(pair.getValue().getBestTime()));
                serializer.endTag("", "highScore");
                it.remove();
            }
            serializer.endTag("", "highScores");
            serializer.endDocument();

            } catch (IOException e) {
                e.printStackTrace();
            }
        return writer.toString();
    }

    private void saveRecordsToFile(String xml) {
        try {
            FileOutputStream fos = context.openFileOutput("highScores", context.MODE_PRIVATE);
            fos.write(xml.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initHighScores() {
        highScores = new HashMap<String, Highscore>();
        String file = openHighScoresFromFile();
        parseHighScores(file);
    }

    private void parseHighScores(String file) {
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(file));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    if (name.equals("highScore")) {
                        Highscore rec = parseHighscore(parser);
                        highScores.put(rec.getLevelId(), rec);
                    }
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Highscore parseHighscore(XmlPullParser parser) {
        String id =  parser.getAttributeValue(null, "id");
        long bestTime = Long.parseLong(parser.getAttributeValue(null, "bestTime"));
        return new Highscore(id, bestTime);
    }

    private String openHighScoresFromFile() {
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = context.openFileInput("highScores");
            byte[] buffer = new byte[1024];
            int length;

            while ((length = fis.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, length));
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String getFormattedBestTime(String id) {
        long time;
        if (highScores.containsKey(id)) {
            time = highScores.get(id).getBestTime();
        }
        else {
            time = 0;
        }
        return TimeFormatter.formatTime(time);
    }
}
