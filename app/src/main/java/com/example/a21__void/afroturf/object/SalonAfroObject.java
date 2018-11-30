package com.example.a21__void.afroturf.object;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.maps.android.clustering.ClusterItem;
import com.ixcoda.Time;

import java.io.Serializable;


/**
 * Created by ASANDA on 2018/09/27.
 * for Pandaphic
 */
public class SalonAfroObject extends AfroObject implements Serializable {
    private String name, salonId;
    private float rating = 3;

    public Location location;
    public String startTime, endTime;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getUID() {
        return this.salonId;
    }

    public float getRating(){
        return this.rating;
    }

    @Override
    public void set(JsonParser parser, String json) {
        JsonObject salon = parser.parse(json).getAsJsonObject();
        this.name = salon.get("name").getAsString();
        this.salonId = salon.get("salonId").getAsString();
        this.rating = salon.get("rating").getAsFloat();
        this.startTime = salon.get("startTime").getAsString();
        this.endTime = salon.get("endTime").getAsString();

        JsonObject rawLocation = salon.get("location").getAsJsonObject();
        JsonArray coordinates = rawLocation.get("coordinates").getAsJsonArray();
        Location location = new Location();
        location.address = rawLocation.get("address").getAsString();
        location.latitude = coordinates.get(0).getAsFloat();
        location.longitude = coordinates.get(1).getAsFloat();
        this.location = location;
    }

    @Override
    public String get() {
        JsonObject salon = new JsonObject();
        salon.addProperty("name", this.name);
        salon.addProperty("salonId", this.salonId);
        salon.addProperty("rating", this.rating);
        salon.addProperty("startTime", this.startTime);
        salon.addProperty("endTime", this.endTime);

        JsonObject location = new JsonObject();
        location.addProperty("address", this.location.address);
        JsonArray coordinates = new JsonArray();
        coordinates.add(this.location.latitude);
        coordinates.add(this.location.longitude);
        location.add("coordinates", coordinates);

        salon.add("location", location);
        return salon.toString();
    }

    public static class Location implements Serializable{
        public String address;
        public float latitude, longitude;
    }

    public static class UIHandler extends AfroObject.UIHandler {
        private final TextView txtName, txtLocation;
        private final CardView cardView;

        public UIHandler(View itemView) {
            super(itemView);
            this.txtName = itemView.findViewById(R.id.txt_name);
            this.txtLocation=  itemView.findViewById(R.id.txt_location);
            this.cardView = itemView.findViewById(R.id.crd_salon);
        }

        @Override
        public void bind(AfroObject afroObject, int position) {
            SalonAfroObject salonAfroObject = (SalonAfroObject)afroObject;
            txtName.setText(salonAfroObject.name);
            txtLocation.setText(salonAfroObject.location.address);
        }

        @Override
        public Class<? extends AfroObject> getObjectClass() {
            return SalonAfroObject.class;
        }

        public CardView getCardView() {
            return cardView;
        }
    }

    public static class SalonClusterItem implements ClusterItem {
        private final SalonAfroObject salonAfroObject;

        public SalonClusterItem(SalonAfroObject pSalonAfroObject){
            this.salonAfroObject = pSalonAfroObject;
        }

        @Override
        public LatLng getPosition() {
            return new LatLng(salonAfroObject.location.latitude, salonAfroObject.location.longitude);
        }

        @Override
        public String getTitle() {
            return salonAfroObject.name;
        }

        @Override
        public String getSnippet() {
            return salonAfroObject.location.address;
        }

        public SalonAfroObject getSalonAfroObject(){ return this.salonAfroObject; }
    }
}
