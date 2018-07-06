package com.example.a21__void.afroturf;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MySalon implements ClusterItem {

    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;

    public MySalon(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
        mSnippet = null;
        mTitle = null;
    }
    public MySalon(double lat, double lng, String mTitle, String mSnippet) {
        this.mTitle = mTitle;
        this.mSnippet = mSnippet;
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }
}
