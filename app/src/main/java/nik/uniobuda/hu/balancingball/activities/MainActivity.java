package nik.uniobuda.hu.balancingball.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import nik.uniobuda.hu.balancingball.R;


public class MainActivity extends AppCompatActivity {

    TextView textViewStart;
    TextView textViewHighscore;

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
                Intent i = new Intent(MainActivity.this, LevelsActivity.class);
                startActivity(i);
            }
        });
    }

    private void initTextViewHighscores() {
        textViewHighscore = (TextView) findViewById(R.id.highscore);
        textViewHighscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HighscoreActivity.class);
                startActivity(i);
            }
        });
    }

}
