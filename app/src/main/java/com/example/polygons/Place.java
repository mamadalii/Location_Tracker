package com.example.polygons;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

public class Place {
    //Each place is a polygon


    GoogleMap googleMap;
    //Fields
    private long milliSeconds = 0L;
    private long startTime;
    //each polygon is made by polygon Options
    private PolygonOptions polygonOptions;

    //Name for each place, get from user
    private String name;


    //store the points of polygon as latlng
    private List<LatLng> placePointsLatLng = new ArrayList<>();

    private Float distanceToUserLocation;

    //Methods

    //Consttructors
    public Place(PolygonOptions polygonOptions, String name, GoogleMap googleMap, List<LatLng> latLongCurrentArray) {
        this.polygonOptions = polygonOptions;
        this.name = name;
        this.googleMap = googleMap;
        placePointsLatLng = latLongCurrentArray;
        googleMap.addPolygon(polygonOptions.clickable(true).fillColor(Color.GREEN)).setTag(name);
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

    //getters
    public long getmilliSeconds() {
        return milliSeconds;
    }

    public void setmilliSeconds(long milliSeconds) {
        this.milliSeconds = milliSeconds;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public PolygonOptions getPolygonOptions() {
        return polygonOptions;
    }

    public String getName() {
        return name;
    }

    public List<LatLng> getplacePointsLatLng() {
        return placePointsLatLng;
    }

//setters

    public Float getDistanceToUserLocation() {
        return distanceToUserLocation;
    }

    public void setDistanceToUserLocation(Location location) {
        LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng nearPoint = NearestPoint(l, placePointsLatLng);

        Location loc1 = new Location(LocationManager.GPS_PROVIDER);
        loc1.setLatitude(nearPoint.latitude);
        loc1.setLongitude(nearPoint.longitude);

        distanceToUserLocation = (float) (distanceBetweenToPoint(loc1, location));
    }


}
