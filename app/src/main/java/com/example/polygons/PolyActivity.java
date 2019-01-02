package com.example.polygons;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.StaticLayout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static com.example.polygons.R.id.Number_OF_Polygons_Text_View;
import static com.example.polygons.R.id.map;


/**
 * An activity that displays a Google map with polygons to represent areas.
 */

public class PolyActivity extends AppCompatActivity
        implements
        OnMapReadyCallback {


   //fiels
//    private static final int COLOR_BLUE_ARGB = 0xffF9A825;
//    private static int n = 0;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location userLocation;

//    ArrayList<polygon> polygonsObjectsArray = new ArrayList<>();
//    ArrayList<LatLng> optionsLat = new ArrayList<>();

    ArrayList<LatLng> optionsLatReserve = new ArrayList<>();
    ArrayList<Polygon> polyList = new ArrayList<Polygon>();
    ArrayList<Marker> markerList = new ArrayList<>();
    List<List<LatLng>> pointsArr = new ArrayList<>();



//    boolean buttonB = true;
//    boolean canAddPointToPolygon = false;
    String s = "";

    //#fields
    TextView textView;
    Button start;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    Handler handler;
    int Seconds, Minutes, hours, MilliSeconds;


    private static PolygonOptions currentPolygonOptions;
    private static List<PolygonOptions> polygonOptions;
    private GoogleMap mMap;
    private Button btnAlertDialogBuildPolygon;
    private static ArrayList<LatLng> latLongCurrentArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        initView();

        //Check for users location every second
        checkMyLocation();


    }

    private void checkMyLocation() {
        checkHandler = new Handler();
        checkHandler.postDelayed(checkRunnable, 1000);
    }

    private void setup(String title) {
        mMap.clear();
        if (polygonOptions.size() != 0) {
            for (PolygonOptions options : polygonOptions) {
                mMap.addPolygon(options.fillColor(Color.GREEN)).setTag(title);

            }
        }
    currentPolygonOptions = null;
    }

    private void dialogSetup(){
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PolyActivity.this);
        View dialogCreatePolygonView = getLayoutInflater().inflate(R.layout.dialog_create_polygon,null);
        alertBuilder.setView(dialogCreatePolygonView);

        //create one edit text and two buttons for Alert Dialog Layout
        final EditText etAlertDialogPolygonTitle = dialogCreatePolygonView.findViewById(R.id.etPolygonTitle);
        btnAlertDialogBuildPolygon = dialogCreatePolygonView.findViewById(R.id.btnDialogBuild);
        Button btnAlertDialogCancel = dialogCreatePolygonView.findViewById(R.id.btnDialogCancel);
        final AlertDialog dialog = alertBuilder.create();
        dialog.show();
        btnAlertDialogBuildPolygon.setEnabled(false);
        btnAlertDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

//        check the edit text in Alert Dialog layout for the polygon title

       etAlertDialogPolygonTitle.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               if (s.toString().equals("")) {
                   btnAlertDialogBuildPolygon.setEnabled(false);
               } else {
                   btnAlertDialogBuildPolygon.setEnabled(true);
               }
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

       btnAlertDialogBuildPolygon.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               setup(etAlertDialogPolygonTitle.getText().toString());
               dialog.dismiss();
           }
       });


    }

    private void initView() {
        //check if there is no previous polyoptions

        if (polygonOptions == null) {
            polygonOptions = new ArrayList<>();
        }


        textView = findViewById(R.id.textView);


        handler = new Handler();

        //Start Button Click Listener
        startButtonClick();

    }

    private void startButtonClick() {
        start = findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change the activity to statistics activity
                Intent intent = new Intent(PolyActivity.this, Stastistics.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);


            }
        }
    }

    public void buttonFunction(View view) {
Log.v("polyoptions length ",""+polygonOptions.size());

        removerAllMarkers();

//        mMap.addPolygon(currentPolygonOptions);

        polygonMaker(currentPolygonOptions);

//        polyList.add(polygon1);
//        pointsArr.add(optionsLat);
//        optionsLatReserve.addAll(optionsLat);
//        EditText e = findViewById(R.id.place_tag_edit_text);
//        s = e.getText().toString();
//        polygon p = new polygon(optionsLat, s);
//        polygonsObjectsArray.add(p);
//        polygon1.setTag(s);
//        TextView number_of_polygons_text_view = findViewById(R.id.Number_OF_Polygons_Text_View);
//        String number_of_polygons = String.valueOf(buttonCount);
//        number_of_polygons_text_view.setText(number_of_polygons);
//        optionsLat.clear();
//        e.getText().clear();
//        buttonB = false;


    }

    private void polygonMaker(PolygonOptions currentPolygonOptions) {
        polygonOptions.add(currentPolygonOptions);
        pointsArr.add(latLongCurrentArray);
        dialogSetup();
    }

    private void removerAllMarkers() {
        for (int i = 0; i < markerList.size(); i++) {
            markerList.get(markerList.size() - i - 1).remove();
        }

    }

    @Override
    public void onMapReady(GoogleMap gooogleMap) {
        builMap(gooogleMap);
        final GoogleMap googleMap = gooogleMap;
        mMap = gooogleMap;

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            Marker m;

            @Override
            public void onLocationChanged(Location location) {
//                if (m != null) {
//                    m.remove();
//                }


                m = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude())).title("You Are Here")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon)));

                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));

                userLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };
        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        }
//        public void clickShapeButton(){
//            createShape();
//        }


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {


            @Override
            public void onMapClick(LatLng location) {

                //it works only when polygonoptions is null

                if (currentPolygonOptions == null) {
                    currentPolygonOptions = new PolygonOptions();
                }

                currentPolygonOptions.add(location);

                if (latLongCurrentArray == null) {
                    latLongCurrentArray = new ArrayList<>();
                }
                latLongCurrentArray.add(location);

                //biuld the temperory polygon on the map
                mMap.clear();
                if (polygonOptions.size() != 0) {
                    for (PolygonOptions options : polygonOptions) {
                        mMap.addPolygon(options.fillColor(Color.GREEN)).setTag("title");

                    }
                }
                mMap.addPolygon(currentPolygonOptions);

//                currentPolygonOptions = null;
                Log.v("Running AddPolygon","inside onMapClickListen");
                // Add polygons to indicate areas on the map.
//                googleMap.addPolygon(new PolygonOptions()
//                        .clickable(true)
//                        .add(
//                                new LatLng(-27.457, 153.040),
//                                new LatLng(-33.852, 151.211),
//                                new LatLng(-37.813, 144.962),
//                                new LatLng(-34.928, 138.599)));
//                // Store a data object with the polygon, used here to indicate an arbitrary type.
////                polygon1.setTag("alpha");

                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.latitude, location.longitude))
                        .draggable(true));


            }

            private void initializePolygonOptions(PolygonOptions currentPolygonOptions) {

            }


        });


       setup("a");

mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
    @Override
    public void onPolygonClick(Polygon polygon) {
    }
});

    }

    private void builMap(GoogleMap gooogleMap) {
    }


    public boolean getPoint(Location location) {
        //Convert location to LatLng
        LatLng l = new LatLng(location.getLatitude(), location.getLongitude());

        ArrayList<LatLng> nearestOfEachPolygon = new ArrayList<>();
        ArrayList<Float> Distances = new ArrayList<>();

    //pointsArr.size = number of Polygons
    //return the closet point of each polygon to user location

        for (int k = 0; k < pointsArr.size(); k++) {
            nearestOfEachPolygon.add(NearestPoint(l, pointsArr.get(k)));
        }

        //build an arrayList of Distances of each polygon to user
        for (int k = 0; k < nearestOfEachPolygon.size(); k++) {
            //Convert LatLng to Location
            Location loc1 = new Location(LocationManager.GPS_PROVIDER);
            loc1.setLatitude(nearestOfEachPolygon.get(k).latitude);
            loc1.setLongitude((nearestOfEachPolygon.get(k).longitude));
//add Distances of each point of neeaestOfEachPolygon to users location
            Distances.add((float) (distanceBetweenToPoint(loc1, location)));
        }


        ArrayList<LatLng> tempOptionsLat = new ArrayList<>();
        tempOptionsLat.addAll(pointsArr.get(Distances.indexOf(Collections.min(Distances))));

        TextView textView = findViewById(R.id.result_text_view);

//        LatLng l = new LatLng(-38,14);
        boolean result = (PolyUtil.containsLocation(l, tempOptionsLat, true));
        textView.setText(String.valueOf(result));
        optionsLatReserve.clear();
        nearestOfEachPolygon.clear();
        Distances.clear();
        tempOptionsLat.clear();
        return result;
    }

    public float distanceBetweenToPoint(Location loc1, Location loc2) {
        float distanceInMeters = loc1.distanceTo(loc2);
        return distanceInMeters;

    }

    private LatLng NearestPoint(LatLng test, List<LatLng> target) {
        double distance = -1;
        LatLng minimumDistancePoint = test;

        if (test == null || target == null) {
            return minimumDistancePoint;
        }

        for (int i = 0; i < target.size(); i++) {
            LatLng point = target.get(i);

            int segmentPoint = i + 1;
            if (segmentPoint >= target.size()) {
                segmentPoint = 0;
            }

            double currentDistance = PolyUtil.distanceToLine(test, point, target.get(segmentPoint));
            if (distance == -1 || currentDistance < distance) {
                distance = currentDistance;
                minimumDistancePoint = findNearestPoint(test, point, target.get(segmentPoint));
            }
        }

        return minimumDistancePoint;
    }

    /**
     * Based on `distanceToLine` method from
     * https://github.com/googlemaps/android-maps-utils/blob/master/library/src/com/google/maps/android/PolyUtil.java
     */
    //Belongs to Impelementation of finding closest point of list to a point(NearestPoint Method)
    private LatLng findNearestPoint(final LatLng p, final LatLng start, final LatLng end) {
        if (start.equals(end)) {
            return start;
        }

        final double s0lat = Math.toRadians(p.latitude);
        final double s0lng = Math.toRadians(p.longitude);
        final double s1lat = Math.toRadians(start.latitude);
        final double s1lng = Math.toRadians(start.longitude);
        final double s2lat = Math.toRadians(end.latitude);
        final double s2lng = Math.toRadians(end.longitude);

        double s2s1lat = s2lat - s1lat;
        double s2s1lng = s2lng - s1lng;
        final double u = ((s0lat - s1lat) * s2s1lat + (s0lng - s1lng) * s2s1lng)
                / (s2s1lat * s2s1lat + s2s1lng * s2s1lng);
        if (u <= 0) {
            return start;
        }
        if (u >= 1) {
            return end;
        }

        return new LatLng(start.latitude + (u * (end.latitude - start.latitude)),
                start.longitude + (u * (end.longitude - start.longitude)));


    }


    //runnable that runs timer
    public Runnable startRunnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.elapsedRealtime() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            hours = Minutes / 60;
            Minutes = Minutes % 60;
            Seconds = Seconds % 60;


            textView.setText("" + String.format("%02d", hours) + ":" + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds));


            handler.postDelayed(this, 0);
        }

    };


    //Check the location and Start the timer if user is in a polygon
    Handler checkHandler;
    public Runnable checkRunnable = new Runnable() {


        public void run() {

            checkHandler.postDelayed(checkRunnable, 0);
            if (polyList.size() > 0) {

                if (getPoint(userLocation)) {
                    StartTime = SystemClock.elapsedRealtime();
                    handler.postDelayed(startRunnable, 0);

                }

            }
        }
    };


}

