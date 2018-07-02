package com.example.a21__void.afroturf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Myact extends AppCompatActivity implements OnMapReadyCallback {

    private static Myact instance;
    GoogleMap mMap;
    private double longitude, latitude;

    public void init(double longi,  double lat){
        this.longitude = longi;
        this.latitude = lat;

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.i("ASD", "onReceive: "+latitude +"| "+longitude);
        LatLng sydney = new LatLng(longitude, latitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // Add a marker in Sydney, Australia, and move the camera.

    }
    public static Myact getInstance(){
        if(instance == null)
            instance = new Myact();

        return instance;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("ASD", "onReceiveResult: "+latitude +"| "+longitude);
    }
}
