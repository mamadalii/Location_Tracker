package com.example.polygons;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.view.View.*;

public class Stastistics extends AppCompatActivity {
Button b;
    ListView lvStatistics;
    List<String> ListElementsArrayList;
    ArrayAdapter<String> adapter;
    String[] ListElements = new String[]{};
    ArrayList<Long> milliArray;
    ArrayList<String> nameArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stastistics);

        milliArray = new ArrayList<>();
        nameArray = new ArrayList<>();
        lvStatistics = findViewById(R.id.lvStatistic);

        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));


        adapter = new ArrayAdapter<String>(Stastistics.this,
                android.R.layout.simple_list_item_1,
                ListElementsArrayList
        );

        lvStatistics.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null) {
            milliArray = (ArrayList<Long>) getIntent().getSerializableExtra("milliArray");
            nameArray = (ArrayList<String>) getIntent().getSerializableExtra("namesArray");

        }
        for (int i = 0; i < milliArray.size(); i++) {
            ListElementsArrayList.add(nameArray.get(i) + " : " + "\t" + (int) (milliArray.get(i) / 1000) + "");
            adapter.notifyDataSetChanged();

        }


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

b=findViewById(R.id.return_to);
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Stastistics.this,PolyActivity.class);
                startActivity(intent);
            }
        });

    }

}
