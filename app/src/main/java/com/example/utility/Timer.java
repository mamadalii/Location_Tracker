package com.example.utility;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Handler;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


public class Timer extends AppCompatActivity {

//    TextView textView ;
//
//    Button start ;
//
//    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
//
//    Handler handler;
//
//    int Seconds, Minutes, MilliSeconds ;
//
//    ListView listView ;
//
//    String[] ListElements = new String[] {  };
//
//    List<String> ListElementsArrayList ;
//
//    ArrayAdapter<String> adapter ;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.old_activity_maps);
//
//        textView = findViewById(R.id.textView);
//        start = findViewById(R.id.start);
////        pause = (Button)findViewById(R.id.button2);
////        reset = (Button)findViewById(R.id.button3);
////        lap = (Button)findViewById(R.id.button4) ;
//        listView = (ListView)findViewById(R.id.listview1);
//
//        handler = new Handler() ;
//
//        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));
//
//        adapter = new ArrayAdapter<String>(Timer.this,
//                android.R.layout.simple_list_item_1,
//                ListElementsArrayList
//        );
//
//        listView.setAdapter(adapter);
//
//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//Log.d("Clickalo","shod");
//                StartTime = SystemClock.elapsedRealtime();
//                handler.postDelayed(runnable, 0);
//
////                reset.setEnabled(false);
//
//            }
//        });
//
////        pause.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////                TimeBuff += MillisecondTime;
////
////                handler.removeCallbacks(runnable);
////
////                reset.setEnabled(true);
////
////            }
////        });
//
////        reset.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////                MillisecondTime = 0L ;
////                StartTime = 0L ;
////                TimeBuff = 0L ;
////                UpdateTime = 0L ;
////                Seconds = 0 ;
////                Minutes = 0 ;
////                MilliSeconds = 0 ;
////
////                textView.setText("00:00");
////
////                ListElementsArrayList.clear();
////
////                adapter.notifyDataSetChanged();
////            }
////        });
//
////        lap.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////                ListElementsArrayList.add(textView.getText().toString());
////
////                adapter.notifyDataSetChanged();
////
////            }
////        });
////
//    }
//
//    public Runnable runnable = new Runnable() {
//
//        public void run() {
//
//            MillisecondTime =SystemClock.elapsedRealtime() - StartTime;
//
//            UpdateTime = TimeBuff + MillisecondTime;
//
//            Seconds = (int) (UpdateTime / 1000);
//
//            Minutes = Seconds / 60;
//
//            Seconds = Seconds % 60;
//            String print=""+Seconds;
//            Log.d("secondsalo ",print);
////            MilliSeconds = (int) (UpdateTime % 1000);
//
//            textView.setText("" + Minutes + ":"
//                    + String.format("%02d", Seconds) );
////                    + String.format("%03d", MilliSeconds))
//
//
//            handler.postDelayed(this, 0);
//        }
//
//    };

}