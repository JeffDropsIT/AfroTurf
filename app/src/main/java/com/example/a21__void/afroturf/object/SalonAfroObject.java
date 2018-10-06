package com.example.a21__void.afroturf.object;

import android.view.View;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonParser;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by ASANDA on 2018/09/27.
 * for Pandaphic
 */
public class SalonAfroObject extends AfroObject {
    private String name, salonId;
    public Location location;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getUID() {
        return this.salonId;
    }

    @Override
    public void set(JsonParser parser, String json) {

    }

    public static class Location{
        private String address;
        public float latitude, longitude;
    }

    public static class UIHandler extends AfroObject.UIHandler {
        private final TextView txtName, txtLocation;

        public UIHandler(View itemView) {
            super(itemView);
            this.txtName = itemView.findViewById(R.id.txt_name);
            this.txtLocation=  itemView.findViewById(R.id.txt_location);
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
    }

    public class SalonClusterItem implements ClusterItem {
        private final SalonAfroObject salonAfroObject;

        SalonClusterItem(SalonAfroObject pSalonAfroObject){
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
    }
}
