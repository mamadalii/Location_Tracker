package com.example.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.events.EventOnSelectToolbarOptions;
import com.example.fragments.FragMap;
import com.example.fragments.FragResult;
import com.example.polygons.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class ActivityShow extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout layResult, layMap;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private CardView layContainer;
    private static FragMap showMap;

    public static ArrayList<Long> milliArray;
    public static ArrayList<String> namesArray;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 12;

    public FusedLocationProviderClient getmFusedLocationClient() {
        return mFusedLocationClient;
    }

    public void setmFusedLocationClient(FusedLocationProviderClient mFusedLocationClient) {
        this.mFusedLocationClient = mFusedLocationClient;
    }

    FusedLocationProviderClient mFusedLocationClient;

    ImageView imgActionMenu, imgActionSearch, imgActionLocationFound, imgActionRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_nav);

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        return true;
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        layMap = (LinearLayout) findViewById(R.id.layMap);
        layResult = (LinearLayout) findViewById(R.id.layResult);
        layContainer = (CardView) findViewById(R.id.layContainer);
        imgActionRefresh = (ImageView) findViewById(R.id.imgActionRefresh);
        imgActionLocationFound = (ImageView) findViewById(R.id.imgActionLocationFound);
        imgActionSearch = (ImageView) findViewById(R.id.imgActionSearch);
        imgActionMenu = (ImageView) findViewById(R.id.imgActionMenu);
//menu = (R.menu.activity_main_actions);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        layMap.setOnClickListener(this);
        layResult.setOnClickListener(this);

        imgActionSearch.setOnClickListener(this);
        imgActionMenu.setOnClickListener(this);
        imgActionLocationFound.setOnClickListener(this);
        imgActionRefresh.setOnClickListener(this);

        showMap();


    }

    private void showPopupMenu() {
        PopupMenu popup = new PopupMenu(ActivityShow.this, imgActionMenu);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.one:
                        EventBus.getDefault().post(new EventOnSelectToolbarOptions(1));
//                        Toast.makeText(ActivityShow.this, "One", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.two:
                        Toast.makeText(ActivityShow.this, "Two", Toast.LENGTH_SHORT).show();
                        break;

                }
                return true;
            }
        });

        popup.show();
    }

    private void showSearchFragment() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(ActivityShow.this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("TAG", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("TAG", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    private void showMap() {
        Fragment fragment = fragmentManager.findFragmentByTag("showMap");
        if (fragment == null) {
            fragment = new FragMap();
//            fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.layContainer, fragment, "showMap");

        }
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("showMap");
        fragmentTransaction.replace(R.id.layContainer, fragment, "showMap");
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
        layMap.setBackgroundColor(Color.parseColor("#05c2ff"));
        layResult.setBackgroundColor(Color.parseColor("#4369ca"));
        Fragment frag1ment = getSupportFragmentManager().findFragmentByTag("showMap");
        Fragment frag121ment = getSupportFragmentManager().findFragmentByTag("showMap");

    }

    private void showResult() {
        Fragment fragment = fragmentManager.findFragmentByTag("showResult");
        if (fragment == null) {
            fragment = new FragResult();
        }
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layContainer, fragment, "showResult");
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
        layMap.setBackgroundColor(Color.parseColor("#4369ca"));
        layResult.setBackgroundColor(Color.parseColor("#05c2ff"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layResult:
                showResult();
                break;
            case R.id.layMap:
                showMap();
                break;
            case R.id.imgActionMenu:
                showPopupMenu();
                break;
            case R.id.imgActionSearch:
                showSearchFragment();
                break;


        }


    }

}
