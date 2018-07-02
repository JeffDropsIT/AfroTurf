package com.example.a21__void.afroturf;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


class MyLocationListiner implements LocationListener{

    public static Double longitude;
    public static Double latitude;
    public static final String TAG = "asd";
    private final Context context;

    private String update;
    private String cityName;

    private Geocoder geocoder;

    List<Address> addresses;

    private Myact myAct = Myact.getInstance();

    public MyLocationListiner(Context context){
        this.context = context;
    }
    @Override
    public void onLocationChanged(Location loc) {

        longitude = loc.getLongitude();
        //Log.v(TAG, longitude);
        latitude =  loc.getLatitude();

        myAct.init(longitude, latitude);
       // Log.v(TAG, latitude);

        Intent intent = new Intent();
        intent.putExtra("lat", latitude);
        intent.putExtra("long", longitude);
        context.startActivity(intent);


        update = latitude+" lat | long "+longitude;

        geocoder = new Geocoder(context, Locale.getDefault());


        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        update = longitude + "\n" + latitude;

        Toast.makeText(context, update, Toast.LENGTH_SHORT).show();





    }

    public String getUpdate() {
        return update;
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
}

