package nik.uniobuda.hu.balancingball.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import nik.uniobuda.hu.balancingball.R;
import nik.uniobuda.hu.balancingball.logic.HighScoreContoller;
import nik.uniobuda.hu.balancingball.model.LevelInfo;
import nik.uniobuda.hu.balancingball.util.XmlLevelParser;

public class HighscoreActivity extends AppCompatActivity {

    private List<LevelInfo> levelInfos;
    private List<String> highscoreListItems = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        initLevelInfos();
        getHighScores();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listitem_highscores, highscoreListItems);
        ListView list = (ListView) findViewById(R.id.listviewhighscores);
        list.setAdapter(adapter);
    }

    private void getHighScores() {
        int maxLength = getLevelNameMaxLength();
        HighScoreContoller highScoreContoller = new HighScoreContoller(this);
        for (LevelInfo level : levelInfos) {
            int nameLength = maxLength - level.getName().length();
            String padding = "";
            for (int i = 0; i < nameLength; i++) {
                padding += " ";
            }
            highscoreListItems.add(
                    level.getName() +
                            padding +
                            " - " +
                            highScoreContoller.getFormattedBestTime(level.getId())
            );
        }
    }

    private int getLevelNameMaxLength() {
        int max = 0;
        for (LevelInfo level : levelInfos) {
            if (level.getName().length() > max) {
                max = level.getName().length();
            }
        }
        return max;
    }

    private void initLevelInfos() {
        XmlLevelParser parser = new XmlLevelParser(this);
        levelInfos = parser.getParsedLevelInfos();
    }
}
