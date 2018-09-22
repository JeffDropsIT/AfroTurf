package com.example.a21__void.afroturf.pkgSalon;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

/**
 * Created by ASANDA on 2018/09/08.
 * for Pandaphic
 */

public class SalonObject implements ClusterItem, Serializable {
    private final String name;
    private final double latitude, longitude;
    private double rating;


    public SalonObject(String pName, LatLng pLocation, double pRating){
        this.name = pName;
        this.latitude = pLocation.latitude;
        this.longitude = pLocation.longitude;
        this.rating = pRating;
    }


    public double getRating() {
        return rating;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(this.latitude, this.longitude);
    }

    @Override
    public String getTitle() {
        return this.name;
    }

    @Override
    public String getSnippet() {
        return "snippet";
    }

    public static SalonObject parse(JsonObject rawJson){
        Log.i("rgn", "parse: " + rawJson.toString());
        JsonElement jName = rawJson.get("name");
        String name = (jName.isJsonNull()) ? "@NULL" : jName.getAsString();
        double rating = rawJson.get("rating").getAsDouble();
        JsonObject location = rawJson.get("location").getAsJsonObject();
        JsonArray coordinates = location.get("coordinates").getAsJsonArray();
        double lati = coordinates.get(0).getAsDouble(),
                longi = coordinates.get(1).getAsDouble();
        return new SalonObject(name, new LatLng(lati, longi), rating);
    }
}

