package nik.uniobuda.hu.balancingball.model;

import android.content.Context;
import android.content.res.XmlResourceParser;
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

import nik.uniobuda.hu.balancingball.util.Record;

/**
 * Created by cserof on 11/25/2017.
 */

public class RecordContoller {

    HashMap<String, Record> records;
    Context context;

    public RecordContoller(Context context) {
        this.context = context;
        initRecords();
    }

    public void addTime(String levelId, long time) {
        if (!records.containsKey(levelId)) {
            records.put(levelId, new Record(levelId, time));
            String xml = writeToXml();
            saveRecordsToFile(xml);
        }
        else if (time < records.get(levelId).getBestTime()) {
            records.get(levelId).setBestTime(time);
            String xml = writeToXml();
            saveRecordsToFile(xml);
        }
    }

    private String writeToXml() {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "records");

            Iterator it = records.entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry<String, Record> pair = (HashMap.Entry<String, Record>)it.next();

                serializer.startTag("", "record");
                serializer.attribute("", "id", pair.getKey());
                serializer.attribute("", "bestTime",String.valueOf(pair.getValue().getBestTime()));
                serializer.endTag("", "record");
                it.remove();
            }
            serializer.endTag("", "records");
            serializer.endDocument();

            } catch (IOException e) {
                e.printStackTrace();
            }
        return writer.toString();
    }

    private void saveRecordsToFile(String xml) {
        try {
            FileOutputStream fos = context.openFileOutput("records", context.MODE_PRIVATE);
            fos.write(xml.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initRecords() {
        records = new HashMap<String, Record>();
        String file = openRecordsFromFile();
        parseRecords(file);
    }

    private void parseRecords(String file) {
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(file));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    if (name.equals("record")) {
                        Record rec = parseRecord(parser);
                        records.put(rec.getId(), rec);
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

    private Record parseRecord(XmlPullParser parser) {
        String id =  parser.getAttributeValue(null, "id");
        long bestTime = Long.parseLong(parser.getAttributeValue(null, "bestTime"));
        return new Record(id, bestTime);
    }

    private String openRecordsFromFile() {
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = context.openFileInput("records");
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

    public long getRecord(String id) {
        long time;
        if (records.containsKey(id)) {
            time = records.get(id).getBestTime();
        }
        else {
            time = 0;
        }
        return time;
    }
}
