package com.example.polygons;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


public class FragMap extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private View rootView;
    static View swiperView;

    TextView tvUserPlace, tvClosePlace, textView;
    private Button btnAlertDialogBuildPolygon, btnReset;
    FloatingActionButton fbtNewPlace, fbtClean, myLocation;

    private static Place userPlace;
    private static MapView mapView;
    private static GoogleMap mMap;
    private static LocationManager locationManager;
    private static Marker lastMarker;
    private static LocationListener locationListener;
    private static Location userLocation;
    private static PolygonOptions currentPolygonOptions;

    private static List<PolygonOptions> polygonOptions;
    private static ArrayList<LatLng> latLongCurrentArray;
    static List<Place> placeList;
    static ArrayList<Marker> markerList = new ArrayList<>();

    static Handler handler, checkHandler, resultHandler;

    static long MillisecondTime, UpdateTime = 0L;
    Boolean active = false;
    private static boolean firstBoot = true;
    static int Seconds, Minutes, hours;
    static int situation = 0;

    static boolean changePlaceBoolean = false;
    private static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 10;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            mapInitializer(inflater, container, savedInstanceState);
            initView();
        }

        return rootView;

    }

    private void mapInitializer(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.frag_map, container, false);
        setHasOptionsMenu(true);

        mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void initView() {
//?
        findViewsByID();

        handler = new Handler();
        handler.postDelayed(TimerRunnable, 2000);


        //?
        myLocation.setOnClickListener(this);
        fbtNewPlace.setOnClickListener(this);
        fbtClean.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        setStartButtonStart();

        if (placeList == null) {
            placeList = new ArrayList<>();
        } else
            //?
            ActivityShow.milliArray = new ArrayList<>();
        ActivityShow.namesArray = new ArrayList<>();


        polygonOptionsNullTester();

        resultHandler = new Handler();

    }

    private void findViewsByID() {
        swiperView = rootView.findViewById(R.id.swipe_btn);
        fbtNewPlace = (FloatingActionButton) rootView.findViewById(R.id.fbtNewPlace);
        fbtClean = (FloatingActionButton) rootView.findViewById(R.id.fbtClean);
        myLocation = (FloatingActionButton) rootView.findViewById(R.id.fbtnMylocation);
        textView = rootView.findViewById(R.id.tv_timer);
        btnReset = rootView.findViewById(R.id.btnReset);
        tvClosePlace = rootView.findViewById(R.id.tv_closest_place);
        tvUserPlace = rootView.findViewById(R.id.tv_current_place);
    }


//    private void pauseClick() {
//        pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                active=false;
////                start.setEnabled(true);
//                handler.removeCallbacks(TimerRunnable);
//                checkHandler.removeCallbacks(checkRunnable);
//                pause.setEnabled(false);
//            }
//        });
//    }

//    private void startClick() {
//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                start.setEnabled(false);
//
//            }
//        });
//    }


    private void polygonOptionsNullTester() {
        //check if there is no previous polyoptions
        if (polygonOptions == null) {
            polygonOptions = new ArrayList<>();
        }
    }

//    private void setStatisticsButtonClick() {
//        statistics = rootView.findViewById(R.id.fbtnStatistics);
//        statistics.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                ActivityShow.milliArray = milliArrayBuilder(placeList);
////                ActivityShow.namesArray = nameArrayBuilder(placeList);
//
//
//                //change the activity to statistics activity
////                Intent intent = new Intent(getContext(), Result.class).putExtra("milliArray", milliArray)
////                        .putExtra("namesArray", namesArray);
////                startActivity(intent);
//            }
//        });
//
//    }

//    private ArrayList<Long> milliArrayBuilder(List<Place> placeList) {
//        if (ActivityShow.milliArray != null)
//            ActivityShow.milliArray.clear();
//        ArrayList<Long> result = new ArrayList<>();
//        for (Place p : placeList) {
//            result.add(p.getmilliSeconds());
//        }
//        return result;
//    }

//    private ArrayList<String> nameArrayBuilder(List<Place> placeList) {
//        if (ActivityShow.namesArray != null)
//            ActivityShow.namesArray.clear();
//        ArrayList<String> result = new ArrayList<>();
//        for (Place p : placeList) {
//            result.add(p.getName());
//        }
//        return result;
//    }

    private void setStartButtonStart() {
        swiperView.setEnabled(false);
        SwipeButton swipeButton = new SwipeButton(getContext());
        swipeButton.setEnabled(false);

        swipeButton.setOnChangeListener(new SwipeButtonListener() {
            @Override
            public void onActiveChanged() {
                if (!active) {
//                    active = false;
                    checkMyLocation();
                } else {
                    active = false;
                    handler.removeCallbacks(TimerRunnable);
                    checkHandler.removeCallbacks(checkRunnable);
                }
            }
        });
    }

    private void setStartButtonClean() {
        if (lastMarker != null)
            lastMarker.remove();
        currentPolygonOptions = null;
        if (latLongCurrentArray != null)
            latLongCurrentArray.clear();
        mMap.clear();
        setupOldPolygons(mMap);

    }

    private void setStartButtonReset() {

        if (lastMarker != null)
            lastMarker.remove();
        currentPolygonOptions = null;
        if (latLongCurrentArray != null)
            latLongCurrentArray.clear();
        handler.removeCallbacks(TimerRunnable);
        active = true;
        placeList.clear();
        mMap.clear();
    }


    private void setStartFButtonNewPlace() {
        try {
            removerAllMarkers();
            if (latLongCurrentArray.size() > 2)
                polygonMaker(currentPolygonOptions);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removerAllMarkers() {
        for (int i = 0; i < markerList.size(); i++) {
            markerList.get(markerList.size() - i - 1).remove();
        }

    }

    private void polygonMaker(PolygonOptions currentPolygonOptions) {
        polygonOptions.add(currentPolygonOptions);
//        pointsArr.add(latLongCurrentArray);
        dialogSetup();
    }

    private void dialogSetup() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        View dialogCreatePolygonView = getLayoutInflater().inflate(R.layout.dialog_create_polygon, null);
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
                setupNewPolygon(etAlertDialogPolygonTitle.getText().toString());
                dialog.dismiss();
            }
        });

        swiperView.setEnabled(true);
    }

    private void setupNewPolygon(String title) {
        mMap.clear();
        if (latLongCurrentArray.size() > 2) {
            Place newPlace = new Place(currentPolygonOptions, title, mMap, latLongCurrentArray);
            placeList.add(newPlace);
            setupOldPolygons(mMap);
        }

        latLongCurrentArray = null;
        currentPolygonOptions = null;
    }

    @Override
    public void onMapReady(GoogleMap gooogleMap) {
        final GoogleMap googleMap = gooogleMap;
//        if (mMap != null) {
//            return;
//        }
        mMap = gooogleMap;

        setupOldPolygons(mMap);
        moveCameraToUserLocation(mMap);


        if (currentPolygonOptions != null) {
            mMap.addPolygon(currentPolygonOptions);
        }


        if (lastMarker != null) {
            googleMap.addMarker(new MarkerOptions()
                    .position(lastMarker.getPosition())
                    .draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        }
        try {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LocationManager service = (LocationManager)
                getContext().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        userLocation = service.getLastKnownLocation(provider);
//        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
//        userLocation = mMap.getMyLocation();

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//
//
////                    final LatLng latLng = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
////                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
////                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
//
//
//
//            }
//        }, 2000);

        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            Marker m;


            @Override
            public void onLocationChanged(Location location) {
                if (m != null)
                    m.remove();
//                m = googleMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(location.getLatitude(), location.getLongitude())).title("You Are Here")
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon)

                userLocation = location;
                if (firstBoot) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

                    firstBoot = false;
                }
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
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }

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
                setupOldPolygons(mMap);
                mMap.addPolygon(currentPolygonOptions);

                lastMarker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(location.latitude, location.longitude))
                        .draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));


            }

            private void initializePolygonOptions(PolygonOptions currentPolygonOptions) {

            }


        });


        setupOldPolygons(mMap);

        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                for (Place place : placeList) {
                    String s = place.getPolygon().getId();
                    String ss = polygon.getId();
                    if (place.getPolygon().getId().equals(polygon.getId())) {

//                        Place place = getUserPlace(userLocation);
                        MillisecondTime = SystemClock.elapsedRealtime() - place.getStartTime();

                        place.setmilliSeconds(place.getmilliSeconds() + MillisecondTime);
                        UpdateTime = place.getmilliSeconds();
                        place.setStartTime(SystemClock.elapsedRealtime());

                        Seconds = (int) (UpdateTime / 1000);

                        Minutes = Seconds / 60;

                        hours = Minutes / 60;
                        Minutes = Minutes % 60;
                        Seconds = Seconds % 60;

                        Log.v("seconds" + Seconds, "1234");
                        String time = "" + String.format("%02d", hours) + ":" + String.format("%02d", Minutes) + ":"
                                + String.format("%02d", Seconds);


                        Toast.makeText(getContext(), "Here is : " + place.getName() + "\n" + "Time : " + time,
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    private void moveCameraToUserLocation(GoogleMap mMap) {
        try {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            this.mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            Location location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            this.mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            this.mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            //1: World
            //5: Landmass/continent
            //10: City
            //15: Streets
            //20: Buildings
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setupOldPolygons(GoogleMap mMap) {
//        if (polygonOptions.size() != 0) {
//            for (int i=0;i<polygonOptions.size();i++) {
        if (placeList != null) {
            if (placeList.size() != 0) {
                for (int i = 0; i < placeList.size(); i++) {
                    Place place = placeList.get(i);
                    Polygon polygon = mMap.addPolygon(place.getPolygonOptions());
                    polygon.setTag(placeList.get(i).getPolygon().getTag());
                    placeList.get(i).setPolygon(polygon);
                }
            }
        }

    }

    private void checkMyLocation() {
        checkHandler = new Handler();
        checkHandler.postDelayed(checkRunnable, 1000);
    }

    public Place getUserPlace(Location location) {
        try {
            //Convert location to LatLng
            LatLng l = new LatLng(location.getLatitude(), location.getLongitude());

            ArrayList<Float> distancesToPolygons;

            distancesToPolygons = findDistancesToPolygons(location, placeList);
            //tempOptionsLat is used to make a temprory polygon
            ArrayList<LatLng> tempOptionsLat = new ArrayList<>();

            Log.v("" + distancesToPolygons.size(), "astt");
            Log.v("kheili", "" + placeList.size());
            Log.v(distancesToPolygons.toString() + "", "bale");

            Place closestPlace = placeList.get(distancesToPolygons.indexOf(Collections.min(distancesToPolygons)));
            for (Place place : placeList) {
                place.getPolygon().setFillColor(Color.BLUE);
            }
            closestPlace.getPolygon().setFillColor(Color.YELLOW);
            tvClosePlace.setText("Close to :" + closestPlace.getName());


            boolean result = (PolyUtil.containsLocation(l, closestPlace.getplacePointsLatLng(), true));
            distancesToPolygons.clear();

            getPlace(closestPlace, result);
            return closestPlace;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public boolean isUserIn(Location location) {
        try {
            //Convert location to LatLng
            LatLng l = new LatLng(location.getLatitude(), location.getLongitude());

            ArrayList<Float> distancesToPolygons;

            distancesToPolygons = findDistancesToPolygons(location, placeList);
            //tempOptionsLat is used to make a temprory polygon
            ArrayList<LatLng> tempOptionsLat = new ArrayList<>();

            Log.v("" + distancesToPolygons.size(), "astt");
            Log.v("kheili", "" + placeList.size());
            Log.v(distancesToPolygons.toString() + "", "bale");

            Place closestPlace = placeList.get(distancesToPolygons.indexOf(Collections.min(distancesToPolygons)));

//
            tvClosePlace.setText(closestPlace.getName());


            boolean result = (PolyUtil.containsLocation(l, closestPlace.getplacePointsLatLng(), true));
            distancesToPolygons.clear();

            getPlace(closestPlace, result);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private ArrayList<Float> findDistancesToPolygons(Location location, List<Place> placeList) {

        ArrayList<Float> Distances = new ArrayList<>();

        for (int k = 0; k < placeList.size(); k++) {
            Place p = placeList.get(k);
            p.setDistanceToUserLocation(location);
            Distances.add(p.getDistanceToUserLocation());
        }

        return Distances;
    }

    private void getPlace(Place closestPlace, boolean result) {
        if (result == false) {
            if (userPlace != null) {
                //user has moved from a place to outside
                situation = 2;
            }
            userPlace = null;
            changePlaceBoolean = false;

            tvUserPlace.setText("You Are Outside");
        } else {
            if (closestPlace.equals(userPlace)) {
                changePlaceBoolean = false;
            } else if (userPlace == null) {
                //user has moved from outside to a place
                situation = 1;
            } else {
                //user has moved from one place directly to another place
                situation = 0;
            }
            changePlaceBoolean = true;
            userPlace = closestPlace;
            tvUserPlace.setText(userPlace.getName());
        }
    }


    //runnable that runs timer
    public Runnable TimerRunnable = new Runnable() {

        public void run() {
//            Place place = isUserIn(userLocation);
            if (isUserIn(userLocation)) {

//                for (PolygonOptions options:polygonOptions) {
//                    int a = options.getPoints().size();
//                    int b= place.getplacePointsLatLng().size();
//                    if (options.getPoints().size()==place.getplacePointsLatLng().size()){
//
//                    }
//                }
                Place place = getUserPlace(userLocation);
                MillisecondTime = SystemClock.elapsedRealtime() - place.getStartTime();

                place.setmilliSeconds(place.getmilliSeconds() + MillisecondTime);
                UpdateTime = place.getmilliSeconds();
                place.setStartTime(SystemClock.elapsedRealtime());

                Seconds = (int) (UpdateTime / 1000);

                Minutes = Seconds / 60;

                hours = Minutes / 60;
                Minutes = Minutes % 60;
                Seconds = Seconds % 60;

                Log.v("seconds" + Seconds, "1234");
                textView.setText("" + String.format("%02d", hours) + ":" + String.format("%02d", Minutes) + ":"
                        + String.format("%02d", Seconds));


                handler.postDelayed(this, 1000);
            } else {
                if (placeList != null && placeList.size() != 0)
                    for (Place place : placeList) {
                        place.getPolygon().setFillColor(Color.BLUE);
                    }
            }
        }
    };
    public Runnable checkRunnable = new Runnable() {


        public void run() {

            if (!active) {

                if (placeList.size() > 0) {

//                    Place place = isUserIn(userLocation);
                    if (isUserIn(userLocation)) {

//                        for (PolygonOptions options : polygonOptions) {
//                            options.fillColor(Color.parseColor("#589635"));
//
////                            if (options.getPoints().size() == place.getplacePointsLatLng().size()) {
////                                for (int i = 0; i < options.getPoints().size(); i++) {
////                                    double lat = options.getPoints().get(i).latitude;
////                                    double lon = options.getPoints().get(i).longitude;
////
////                                    double lat1 = place.getplacePointsLatLng().get(i).latitude;
////                                    double lon1 = place.getplacePointsLatLng().get(i).longitude;
////                                    if (lat == lat1 && lon == lon1) {
////                                        options.fillColor(Color.parseColor("#ffffff"));
////                                    }
////
////
////                                }
////                            }
//                        }

                        if (situation == 0 || situation == 1) {

                            userPlace.setStartTime(SystemClock.elapsedRealtime());

                            handler.postDelayed(TimerRunnable, 2000);
                        }


                    } else
                        handler.removeCallbacks((TimerRunnable));

                }
                checkHandler.postDelayed(this, 2000);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fbtNewPlace:
                setStartFButtonNewPlace();
                break;
            case R.id.fbtClean:
                setStartButtonClean();
                break;
            case R.id.btnReset:
                setStartButtonReset();
                break;
            case R.id.fbtnMylocation:
                setStartButtonMylocation();


        }

    }

    private void setStartButtonMylocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

//        mMap.getUiSettings().setZoomControlsEnabled(true);

        final LatLng latLng = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
//    public void disableAllSettings(ViewGroup mGroup) {
//
//        int itotal = mGroup.getChildCount();
//        for (int i = 0; i < itotal; i++) {
//            if (mGroup.getChildAt(i) instanceof ViewGroup) {
//                disableAllSettings((ViewGroup) mGroup.getChildAt(i));
//            } else {
//                mGroup.getChildAt(i).setEnabled(false);
//            }
//        }
//    }
}
