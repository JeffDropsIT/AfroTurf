package com.example.a21__void.afroturf;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

/**
 * Created by ASANDA on 2018/07/23.
 * for Pandaphic
 */
public class SalonObject implements ClusterItem , Serializable {
    private final String name;
    private final LatLng location;
    private double rating;

    public SalonObject(String pName, LatLng pLocation, double pRating){
        this.name = pName;
        this.location = pLocation;
        this.rating = pRating;
    }

    public double getRating() {
        return rating;
    }

    @Override
    public LatLng getPosition() {
        return this.location;
    }

    @Override
    public String getTitle() {
        return this.name;
    }

    @Override
    public String getSnippet() {
        return "snippet";
    }
}
