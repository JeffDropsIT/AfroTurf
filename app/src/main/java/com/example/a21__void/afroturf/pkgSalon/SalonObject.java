package com.example.a21__void.afroturf.pkgSalon;

import android.content.Intent;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.pkgCommon.HolderTemplate;
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
    private final String name, address;
    private final double latitude, longitude;
    private double rating;
    public final int startHour, startMin, endHour, endMin;


    public SalonObject(String pName, String pAddress, LatLng pLocation, double pRating, int startHour, int startMin, int endHour, int endMin){
        this.name = pName;
        this.latitude = pLocation.latitude;
        this.longitude = pLocation.longitude;
        this.rating = pRating;
        this.address = pAddress;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
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
        JsonElement jName = rawJson.get("name");
        String name = (jName.isJsonNull()) ? "@NULL" : jName.getAsString(),
                address;
        double rating = rawJson.get("rating").getAsDouble();
        JsonObject location = rawJson.get("location").getAsJsonObject();
        JsonArray coordinates = location.get("coordinates").getAsJsonArray();
        address = location.get("address").getAsString();
        double lati = coordinates.get(0).getAsDouble(),
                longi = coordinates.get(1).getAsDouble();
        String startTime = rawJson.get("startTime").getAsString(),
                endTime= rawJson.get("endTime").getAsString();

        String[] startPart = startTime.split(":"),
                endPart = endTime.split(":");
        return new SalonObject(name,  address, new LatLng(lati, longi), rating, Integer.parseInt(startPart[0]), Integer.parseInt(startPart[1]), Integer.parseInt(endPart[0]), Integer.parseInt(endPart[1]));
    }
//
//    public static class SalonObjectTemplate extends HolderTemplate<SalonObject> {
//        private TextView txtName, txtLoc;
//
//        public SalonObjectTemplate(ViewGroup itemView) {
//            super(itemView);
//            this.txtName = itemView.findViewById(R.id.txtName);
//            this.txtLoc=  itemView.findViewById(R.id.txtLocation);
//        }
//
//        @Override
//        public void bind(SalonObject data, int pos) {
//            txtName.setText(data.name);
//            txtLoc.setText(data.address);
//        }
//    }
}

