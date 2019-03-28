package com.liwq.thelinechart;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LineChartView chartView;
    private String[] yValues = new String[]{"71", "86", "99", "68", "20", "-1", "100"};
    private String[] xAisx = new String[]{"速度", "射门", "传球", "盘带", "防守", "力量", "力量"};
    private String[] yAisx = new String[]{"125", "100", "75", "50", "25", "0"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        chartView = findViewById(R.id.chartView);
        chartView.setData(xAisx, yAisx, xAisx, yValues);
        ArrayList<Float> floats = new ArrayList<>();
        floats.add(70.9F);
        floats.add(86F);
        floats.add(99F);
        floats.add(68F);
        floats.add(20F);
        PlayerLineView mPlayerLineView = (PlayerLineView) findViewById(R.id.playerLine);
        mPlayerLineView.initData(floats, "PLAYER");
    }

}
