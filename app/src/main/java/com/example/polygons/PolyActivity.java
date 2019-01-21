//package com.example.polygons;
//
//
//import java.util.List;
//import java.util.ArrayList;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.SystemClock;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Build;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.Polygon;
//import com.google.android.gms.maps.model.PolygonOptions;
//import com.google.maps.android.PolyUtil;
//import java.util.Collections;
//import static com.example.polygons.R.id.map;
//
//
///**
// * An activity that displays a Google map with polygons to represent areas.
// */
//
//public class PolyActivity extends AppCompatActivity
//        implements
//        OnMapReadyCallback {
//
//    int situation = 0;
//
//    private LocationListener locationListener;
//    private Location userLocation;
//
//    static List<Place> placeList;
//
//
//    ArrayList<Marker> markerList = new ArrayList<>();
//    FloatingActionButton statistics;
//    FloatingActionButton clean;
//    Button reset;
//    Button pause;
//    TextView textView;
//    /**
//     * Based on `distanceToLine` method from
//     * https://github.com/googlemaps/android-maps-utils/blob/master/library/src/com/google/maps/android/PolyUtil.java
//     */
//    //Belongs to Impelementation of finding closest point of list to a point(NearestPoint Method)
//
//
//    //runnable that runs timer
//    public Runnable TimerRunnable = new Runnable() {
//
//        public void run() {
//            if (isUserIn(userLocation)) {
//
//                MillisecondTime = SystemClock.elapsedRealtime() - userPlace.getStartTime();
//
//                userPlace.setmilliSeconds(userPlace.getmilliSeconds() + MillisecondTime);
//                UpdateTime = userPlace.getmilliSeconds();
//                userPlace.setStartTime(SystemClock.elapsedRealtime());
//
//                Seconds = (int) (UpdateTime / 1000);
//
//                Minutes = Seconds / 60;
//
//                hours = Minutes / 60;
//                Minutes = Minutes % 60;
//                Seconds = Seconds % 60;
//
//                Log.v("seconds" + Seconds, "1234");
//                textView.setText("" + String.format("%02d", hours) + ":" + String.format("%02d", Minutes) + ":"
//                        + String.format("%02d", Seconds));
//
//
//                handler.postDelayed(this, 1000);
//            }
//        }
//    };
//    public Runnable checkRunnable = new Runnable() {
//
//
//        public void run() {
//            if (!start.isEnabled()) {
//                if (placeList.size() > 0) {
//
//
//                    if (isUserIn(userLocation)) {
//
//                        tvResult.setText("True");
//                        if (situation == 0 || situation == 1) {
//
//                            userPlace.setStartTime(SystemClock.elapsedRealtime());
//
//                            handler.postDelayed(TimerRunnable, 2000);
//                        }
//
//
//                    } else
//                        handler.removeCallbacks((TimerRunnable));
//                    tvResult.setText("false");
//
//                }
//                checkHandler.postDelayed(this, 2000);
//            }
//        }
//    };
//    //used for transfering times to statistics
//    ArrayList<Long> milliArray;
//    Button start;
//    Handler resultHandler;
//    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
//    Handler handler;
//    int Seconds, Minutes, hours;
//    Place userPlace;
//    TextView tvResult;
//    boolean changePlaceBoolean = false;
//    TextView tvUserPlace;
//    TextView tvClosePlace;
//    FloatingActionButton fbtnNewPlace;
//
//    private static PolygonOptions currentPolygonOptions;
//    private static List<PolygonOptions> polygonOptions;
//    private GoogleMap mMap;
//    private Button btnAlertDialogBuildPolygon;
//    private static ArrayList<LatLng> latLongCurrentArray;
//    Handler checkHandler;
//    //used for transfering names to statistics
//    ArrayList<String> namesArray;
//    private LocationManager locationManager;
//    private static Marker lastMarker;
//
//    private void checkMyLocation() {
//        checkHandler = new Handler();
//        checkHandler.postDelayed(checkRunnable, 1000);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null) {
//
//            ArrayList<Place> placeArray = new ArrayList<Place>();
//
//            placeArray = savedInstanceState.getParcelableArrayList("placeArray");
//            placeList = placeArray;
//            Log.v("inside " + placeList.size(), "onrestore" + placeList.size());
//        }
//        setContentView(R.layout.old_activity_maps);
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(map);
//        mapFragment.getMapAsync(this);
//        textView = findViewById(R.id.tv_timer);
//        Log.v("inside ", "oncreate ");
//        if (placeList == null) {
//            placeList = new ArrayList<>();
//        } else
//            Log.v("inside size", "" + placeList.size());
////        setupOldPolygons(mMap);
//        milliArray = new ArrayList<>();
//        namesArray = new ArrayList<>();
//        handler = new Handler();
//        initialiaze();
//        Log.v("place numbers : " + placeList.size(), "onCreate");
//
//        //check if there is no previous polyoptions
//        polygonOptionsNullTester();
//        //Start Button Click Listener
//        setStatisticsButtonClick();
////        setStartButtonStart();
//        setStartButtonClean();
//        setStartButtonReset();
////        setStartButtonPause();
//        setStartFButtonNewPlace();
//        //Check for users location every second
//        checkMyLocation();
//        resultHandler = new Handler();
//        tvClosePlace = findViewById(R.id.tv_closest_place);
//        tvUserPlace = findViewById(R.id.tv_current_place);
//    }
//
//    private void setStartFButtonNewPlace() {
//        fbtnNewPlace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                removerAllMarkers();
//
////        mMap.addPolygon(currentPolygonOptions);
//
//                if (latLongCurrentArray.size() > 2)
//                    polygonMaker(currentPolygonOptions);
//            }
//        });
//    }
//
//    private void setStartButtonReset() {
//        reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (lastMarker != null)
//                    lastMarker.remove();
//                currentPolygonOptions = null;
//                if (latLongCurrentArray != null)
//                    latLongCurrentArray.clear();
//                handler.removeCallbacks(TimerRunnable);
//                start.setEnabled(true);
//                placeList.clear();
//                mMap.clear();
//
//            }
//        });
//    }
//
//    public void setStartButtonPause() {
//                checkHandler.removeCallbacks(checkRunnable);
//                handler.removeCallbacks(TimerRunnable);
//                start.setEnabled(true);
//
//    }
//
//    private void setStartButtonClean() {
//        clean.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (lastMarker != null)
//                    lastMarker.remove();
//                currentPolygonOptions = null;
//                if (latLongCurrentArray != null)
//                    latLongCurrentArray.clear();
//                mMap.clear();
//                setupOldPolygons(mMap);
//            }
//        });
//
//    }
//
//    private void initialiaze() {
//        clean = findViewById(R.id.fbtnClean);
//        reset = findViewById(R.id.btnReset);
//        pause = findViewById(R.id.btnPause);
//        start = findViewById(R.id.btnStart);
//        fbtnNewPlace = findViewById(R.id.fbtnNewPlace);
//        pause.setEnabled(false);
//    }
//
//    public void setStartButtonStart() {
//
//                StartTime = SystemClock.elapsedRealtime();
//                handler.postDelayed(checkRunnable, 1000);
////                start.setEnabled(false);
////                pause.setEnabled(true);
//
//    }
//
//    private void setupNewPolygon(String title) {
//        mMap.clear();
//////        if (polygonOptions.size() != 0) {
//////            for (int i=0;i<polygonOptions.size();i++) {
////if(placeList.size()!=0){
////        for (int i=0;i<placeList.size();i++) {
////            Place ad = new Place(placeList.get(i).getPolygonOptions(),title,mMap,latLongCurrentArray);
//////                mMap.addPolygon(options.fillColor(Color.GREEN)).setTag(title);
////
////        }}
//        if (latLongCurrentArray.size() > 2) {
//            Place newPlace = new Place(currentPolygonOptions, title, mMap, latLongCurrentArray);
//            placeList.add(newPlace);
//            setupOldPolygons(mMap);
//        }
//
//        //                mMap.addPolygon(options.fillColor(Color.GREEN)).setTag(title);
//
////            }
//
//        latLongCurrentArray = null;
//        currentPolygonOptions = null;
//    }
//
//    public void setupOldPolygons(GoogleMap mMap) {
////        if (polygonOptions.size() != 0) {
////            for (int i=0;i<polygonOptions.size();i++) {
//        if (placeList != null) {
//            if (placeList.size() != 0) {
//                for (Place p : placeList) {
//                    Place oldPlace = new Place(p.getPolygonOptions(), p.getName(), mMap, p.getplacePointsLatLng());
////                mMap.addPolygon(options.fillColor(Color.GREEN)).setTag(title);
//
//                }
//            }
//        }
//
//    }
//
//    private void dialogSetup() {
//        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PolyActivity.this);
//        View dialogCreatePolygonView = getLayoutInflater().inflate(R.layout.dialog_create_polygon, null);
//        alertBuilder.setView(dialogCreatePolygonView);
//
//        //create one edit text and two buttons for Alert Dialog Layout
//        final EditText etAlertDialogPolygonTitle = dialogCreatePolygonView.findViewById(R.id.etPolygonTitle);
//        btnAlertDialogBuildPolygon = dialogCreatePolygonView.findViewById(R.id.btnDialogBuild);
//        Button btnAlertDialogCancel = dialogCreatePolygonView.findViewById(R.id.btnDialogCancel);
//        final AlertDialog dialog = alertBuilder.create();
//        dialog.show();
//        btnAlertDialogBuildPolygon.setEnabled(false);
//        btnAlertDialogCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
////        check the edit text in Alert Dialog layout for the polygon title
//
//        etAlertDialogPolygonTitle.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().equals("")) {
//                    btnAlertDialogBuildPolygon.setEnabled(false);
//                } else {
//                    btnAlertDialogBuildPolygon.setEnabled(true);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        btnAlertDialogBuildPolygon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setupNewPolygon(etAlertDialogPolygonTitle.getText().toString());
//                dialog.dismiss();
//            }
//        });
//
//
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
//
//
//            }
//        }
//    }
//
//    private void polygonOptionsNullTester() {
//
//        //check if there is no previous polyoptions
//        if (polygonOptions == null) {
//            polygonOptions = new ArrayList<>();
//        }
//    }
//
//    private ArrayList<Long> milliArrayBuilder(List<Place> placeList) {
//        if (milliArray != null)
//            milliArray.clear();
//        ArrayList<Long> result = new ArrayList<>();
//        for (Place p : placeList) {
//            result.add(p.getmilliSeconds());
//        }
//        return result;
//    }
//
//    private ArrayList<String> nameArrayBuilder(List<Place> placeList) {
//        if (namesArray != null)
//            namesArray.clear();
//        ArrayList<String> result = new ArrayList<>();
//        for (Place p : placeList) {
//            result.add(p.getName());
//        }
//        return result;
//    }
//    private void setStatisticsButtonClick() {
//        statistics = findViewById(R.id.fbtnStatistics);
//        statistics.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                milliArray = milliArrayBuilder(placeList);
//                namesArray = nameArrayBuilder(placeList);
//
//
//                //change the activity to statistics activity
//                Intent intent = new Intent(PolyActivity.this, Result.class).putExtra("milliArray", milliArray)
//                        .putExtra("namesArray", namesArray);
//                startActivity(intent);
//            }
//        });
//
//    }
//
//    private void removerAllMarkers() {
//        for (int i = 0; i < markerList.size(); i++) {
//            markerList.get(markerList.size() - i - 1).remove();
//        }
//
//    }
//
//
////    public void buttonFunction(View view) {
////        Log.v("polyoptions length ", "" + polygonOptions.size());
////
////
////
////
////    }
//
////    private void builMap(GoogleMap gooogleMap) {
////    }
//
//    private void polygonMaker(PolygonOptions currentPolygonOptions) {
//        polygonOptions.add(currentPolygonOptions);
////        pointsArr.add(latLongCurrentArray);
//        dialogSetup();
//    }
//
//    @Override
//    public void onMapReady(GoogleMap gooogleMap) {
//
//        final GoogleMap googleMap = gooogleMap;
//        mMap = gooogleMap;
//
//        setupOldPolygons(mMap);
//        if (currentPolygonOptions != null) {
//            mMap.addPolygon(currentPolygonOptions);
//        }
//        if (lastMarker != null) {
//            googleMap.addMarker(new MarkerOptions()
//                    .position(lastMarker.getPosition())
//                    .draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
//        }
//
//        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
//        locationListener = new LocationListener() {
//            Marker m;
//
//            @Override
//            public void onLocationChanged(Location location) {
//                if (m != null)
//                    m.remove();
//                m = googleMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(location.getLatitude(), location.getLongitude())).title("You Are Here")
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
////                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon)
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
//
//                userLocation = location;
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//            }
//
//        };
//
//
//        if (Build.VERSION.SDK_INT < 23) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
//
//        } else {
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//
//            } else {
//                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
//
//            }
//        }
////        public void clickShapeButton(){
////            createShape();
////        }
//
//
//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//
//
//            @Override
//            public void onMapClick(LatLng location) {
//
//                //it works only when polygonoptions is null
//
//                if (currentPolygonOptions == null) {
//                    currentPolygonOptions = new PolygonOptions();
//                }
//
//                currentPolygonOptions.add(location);
//
//                if (latLongCurrentArray == null) {
//                    latLongCurrentArray = new ArrayList<>();
//                }
//                latLongCurrentArray.add(location);
//
//                //biuld the temperory polygon on the map
//
//                mMap.clear();
//                setupOldPolygons(mMap);
////                if (polygonOptions.size() != 0) {
////                    for (PolygonOptions options : polygonOptions) {
////                        mMap.addPolygon(options.fillColor(Color.GREEN)).setTag("title");
////
////                    }
////                }
//                mMap.addPolygon(currentPolygonOptions);
//
////                currentPolygonOptions = null;
////                Log.v("Running AddPolygon", "inside onMapClickListen");
//                // Add polygons to indicate areas on the map.
////                googleMap.addPolygon(new PolygonOptions()
////                        .clickable(true)
////                        .add(
////                                new LatLng(-27.457, 153.040),
////                                new LatLng(-33.852, 151.211),
////                                new LatLng(-37.813, 144.962),
////                                new LatLng(-34.928, 138.599)));
////                // Store a data object with the polygon, used here to indicate an arbitrary type.
//////                polygon1.setTag("alpha");
//
//                lastMarker = googleMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(location.latitude, location.longitude))
//                        .draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
//
//
//            }
//
//            private void initializePolygonOptions(PolygonOptions currentPolygonOptions) {
//
//            }
//
//
//        });
//
//
//        setupOldPolygons(mMap);
//
//        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
//            @Override
//            public void onPolygonClick(Polygon polygon) {
//                Toast.makeText(PolyActivity.this, "Here is : " + polygon.getTag().toString(),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//
//    public boolean isUserIn(Location location) {
//        //Convert location to LatLng
//        LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
//
//        ArrayList<Float> distancesToPolygons;
//
//        distancesToPolygons = findDistancesToPolygons(location, placeList);
//        //tempOptionsLat is used to make a temprory polygon
//        ArrayList<LatLng> tempOptionsLat = new ArrayList<>();
//
//        Log.v("" + distancesToPolygons.size(), "astt");
//        Log.v("kheili", "" + placeList.size());
//        Log.v(distancesToPolygons.toString() + "", "bale");
//
//        Place closestPlace = placeList.get(distancesToPolygons.indexOf(Collections.min(distancesToPolygons)));
//
//        tvClosePlace.setText(closestPlace.getName());
//
//
//        boolean result = (PolyUtil.containsLocation(l, closestPlace.getplacePointsLatLng(), true));
//        distancesToPolygons.clear();
//
//        getPlace(closestPlace, result);
//
//
//        return result;
//    }
//
//    private void getPlace(Place closestPlace, boolean result) {
//        if (result == false) {
//            if (userPlace != null) {
//                //user has moved from a place to outside
//                situation = 2;
//            }
//            userPlace = null;
//            changePlaceBoolean = false;
//
//            tvUserPlace.setText("Nowhere");
//        } else {
//            if (closestPlace.equals(userPlace)) {
//                changePlaceBoolean = false;
//            } else if (userPlace == null) {
//                //user has moved from outside to a place
//                situation = 1;
//            } else {
//                //user has moved from one place directly to another place
//                situation = 0;
//            }
//                changePlaceBoolean = true;
//            userPlace = closestPlace;
//            tvUserPlace.setText(userPlace.getName());
//        }
//    }
//
//
//    //Check the location and Start the timer if user is in a polygon
//
//    private ArrayList<Float> findDistancesToPolygons(Location location, List<Place> placeList) {
//
//        ArrayList<Float> Distances = new ArrayList<>();
//
//        for (int k = 0; k < placeList.size(); k++) {
//            Place p = placeList.get(k);
//            p.setDistanceToUserLocation(location);
//            Distances.add(p.getDistanceToUserLocation());
//        }
//
//        return Distances;
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//
////        // Save UI state changes to the savedInstanceState.
//        savedInstanceState.putBoolean("MyBoolean", true);
//        ArrayList<Place> placeArray = new ArrayList<Place>(placeList);
//        savedInstanceState.putParcelableArrayList("placeArray", placeArray);
////        // etc.
//        Log.v("inside " + placeArray.size(), "savedInstance");
//    }
////    @Override
////    public void onRestoreInstanceState(Bundle savedInstanceState) {
////        super.onRestoreInstanceState(savedInstanceState);
////        // Restore UI state from the savedInstanceState.
////        // This bundle has also been passed to onCreate.
////        boolean myBoolean = savedInstanceState.getBoolean("MyBoolean");
////        ArrayList<Place> placeArray = new ArrayList<Place>();
////
////       placeArray= savedInstanceState.getParcelableArrayList("placeArray");
////       placeList=placeArray;
////       Log.v("inside ","onrestore"+placeList.size());
////
////    }
//}
//
