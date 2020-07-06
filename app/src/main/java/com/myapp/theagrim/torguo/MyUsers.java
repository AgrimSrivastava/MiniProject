package com.myapp.theagrim.torguo;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.Date;

public class MyUsers {

    String lo;
    String lat;

    public MyUsers(){

    }


    public MyUsers(String lat, String lo) {
        this.lat = lat;
        this.lo = lo;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLo() {
        return lo;
    }

    public void setLo(String lo) {
        this.lo = lo;
    }
}
