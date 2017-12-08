package nik.uniobuda.hu.balancingball.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import nik.uniobuda.hu.balancingball.R;


/**
 * Created by cserof on 11/12/2017.
 * Controls the main menu of the game
 */
public class MainActivity extends AppCompatActivity {

    private TextView textViewStart;
    private TextView textViewHighscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTextViewStart();
        initTextViewHighscores();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void initTextViewStart() {
        textViewStart = (TextView) findViewById(R.id.start);
        textViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLevelsActivity();
            }
        });
    }

    private void startLevelsActivity() {
        Intent i = new Intent(MainActivity.this, LevelsActivity.class);
        startActivity(i);
    }

    private void initTextViewHighscores() {
        textViewHighscore = (TextView) findViewById(R.id.highscore);
        textViewHighscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHighscoreActivity();
            }
        });
    }

    private void startHighscoreActivity() {
        Intent i = new Intent(MainActivity.this, HighscoreActivity.class);
        startActivity(i);
    }

}
