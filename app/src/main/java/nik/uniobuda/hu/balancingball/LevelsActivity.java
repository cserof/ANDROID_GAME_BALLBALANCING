package nik.uniobuda.hu.balancingball;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.List;
import nik.uniobuda.hu.balancingball.model.Level;
import nik.uniobuda.hu.balancingball.util.XmlMapParser;

public class LevelsActivity extends AppCompatActivity {

    List<Level> levels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        initLevels();

        final LevelsViewAdapter adapter = new LevelsViewAdapter(levels);
        ListView list = (ListView) findViewById(R.id.listview);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Level selected = (Level) adapter.getItem(position);
                openSelectedLevel(selected);
            }
        });
    }

    private void openSelectedLevel(Level selected) {
        Intent i = new Intent(LevelsActivity.this, GameActivity.class);
        i.putExtra("selectedLevel", selected);
        startActivity(i);
    }

    private void initLevels() {
        XmlMapParser parser = new XmlMapParser(this);
        levels = parser.getParsedMap();
    }
}
