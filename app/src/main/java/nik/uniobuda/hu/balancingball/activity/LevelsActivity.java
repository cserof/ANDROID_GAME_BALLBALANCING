package nik.uniobuda.hu.balancingball.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.List;
import nik.uniobuda.hu.balancingball.adapter.LevelInfosViewAdapter;
import nik.uniobuda.hu.balancingball.R;
import nik.uniobuda.hu.balancingball.model.LevelInfo;
import nik.uniobuda.hu.balancingball.util.XmlLevelParser;

/**
 * Controls the Level picker feature
 * gets all the level ids and names from levelinfo.xml
 * If the level is clicked on it,
 * the gameActivity is called with the specific level id as a parameter.
 * */
public class LevelsActivity extends AppCompatActivity {

    private List<LevelInfo> levelInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        initLevelInfos();
        initLevelInfosView();
    }

    private void initLevelInfosView() {
        final LevelInfosViewAdapter adapter = new LevelInfosViewAdapter(levelInfos);
        ListView list = (ListView) findViewById(R.id.listviewlevels);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LevelInfo selected = (LevelInfo) adapter.getItem(position);
                openSelectedLevel(selected.getId());
            }
        });
    }

    private void openSelectedLevel(String selectedLevelId) {
        Intent i = new Intent(LevelsActivity.this, GameActivity.class);
        i.putExtra("selectedLevelId", selectedLevelId);
        startActivity(i);
    }

    private void initLevelInfos() {
        XmlLevelParser parser = new XmlLevelParser(this);
        levelInfos = parser.getParsedLevelInfos();
    }
}
