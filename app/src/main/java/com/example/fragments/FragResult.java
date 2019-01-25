package com.example.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.activities.ActivityShow;
import com.example.models.Place;
import com.example.polygons.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.fragments.FragMap.placeList;


public class FragResult extends Fragment {

    private View rootView;

    Button b;
    //    FloatingActionButton fbtnMap;
    ListView lvStatistics;
    List<String> ListElementsArrayList;
    ArrayAdapter<String> adapter;
    String[] ListElements = new String[]{};
    FloatingActionButton fbtnStart;
    FloatingActionButton fbtnPause;
    static long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    int Seconds, Minutes, hours;

    public static FragResult newInstance() {
        FragResult fragment = new FragResult();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.frag_result, container, false);
        initView();


        return rootView;
    }

    private ArrayList<Long> milliArrayBuilder(List<Place> placeList) {
        if (ActivityShow.milliArray != null)
            ActivityShow.milliArray.clear();
        ArrayList<Long> result = new ArrayList<>();
        for (Place p : placeList) {
            result.add(p.getmilliSeconds());
        }
        return result;
    }

    private ArrayList<String> nameArrayBuilder(List<Place> placeList) {
        if (ActivityShow.namesArray != null)
            ActivityShow.namesArray.clear();
        ArrayList<String> result = new ArrayList<>();
        for (Place p : placeList) {
            result.add(p.getName());
        }
        return result;
    }

    private void initView() {
//        fbtnMap = rootView.findViewById(R.id.fbtnMap);
        lvStatistics = rootView.findViewById(R.id.lvStatistic);
        ActivityShow.milliArray = milliArrayBuilder(placeList);
        ActivityShow.namesArray = nameArrayBuilder(placeList);

        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));


        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                ListElementsArrayList
        );

        lvStatistics.setAdapter(adapter);

        for (int i = 0; i < ActivityShow.milliArray.size(); i++) {
            UpdateTime = ActivityShow.milliArray.get(i);
//            p.setStartTime(SystemClock.elapsedRealtime());

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            hours = Minutes / 60;
            Minutes = Minutes % 60;
            Seconds = Seconds % 60;
            String s = String.format("%02d", hours) + ":" + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds);

            ListElementsArrayList.add(ActivityShow.namesArray.get(i) + " : " + "\t" + s);
            adapter.notifyDataSetChanged();

        }
    }

}
