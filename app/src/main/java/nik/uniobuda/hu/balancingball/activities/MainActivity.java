package nik.uniobuda.hu.balancingball.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import nik.uniobuda.hu.balancingball.R;


public class MainActivity extends AppCompatActivity {

    TextView textViewStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTextViewStart();
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

}
