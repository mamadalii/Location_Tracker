package com.example.polygons;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

public class Place implements Parcelable {
    //Each place is a polygon


    GoogleMap googleMap;
    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
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
    GoogleMap googleMapReserve;

    private Polygon polygon;

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    protected Place(Parcel in) {
        milliSeconds = in.readLong();
        startTime = in.readLong();
        polygonOptions = in.readParcelable(PolygonOptions.class.getClassLoader());
        name = in.readString();
        placePointsLatLng = in.createTypedArrayList(LatLng.CREATOR);
        if (in.readByte() == 0) {
            distanceToUserLocation = null;
        } else {
            distanceToUserLocation = in.readFloat();
        }
    }


    //Consttructors
    public Place(PolygonOptions polygonOptions, String name, GoogleMap googleMap, List<LatLng> latLongCurrentArray) {
        Log.v(googleMap + "inside place maker", latLongCurrentArray.size() + "");
        this.polygonOptions = polygonOptions;
        this.name = name;
        if (googleMap != null)
            this.googleMap = googleMap;
        else
            this.googleMap = googleMapReserve;

        placePointsLatLng = latLongCurrentArray;
//        googleMap.addPolygon(polygonOptions.clickable(true).fillColor(Color.parseColor("#112299"))).setTag(name);

//        PolygonOptions options = new PolygonOptions();
        polygonOptions.clickable(true);
        polygonOptions.fillColor(Color.parseColor("#112299"));
        this.polygon = googleMap.addPolygon(polygonOptions);
        this.polygon.setTag(name);
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

    public GoogleMap getGoogleMap() {
        return googleMap;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(milliSeconds);
        dest.writeLong(startTime);
        dest.writeParcelable(polygonOptions, flags);
        dest.writeString(name);
//        dest.writeValue(googleMap);
        dest.writeTypedList(placePointsLatLng);
        if (distanceToUserLocation == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(distanceToUserLocation);
        }
    }
}
