package com.example.a21__void.afroturf;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class GoogleMapManager {
    private static GoogleMapManager instance;

    private GoogleMapManager(){}

    public void registerMapCallback(SupportMapFragment mapFragment, final MapCallback mapCallback){
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mapCallback.onMapReady(googleMap);
            }
        });
    }

    public static GoogleMapManager getInstance(){
        return (instance == null)? (instance = new GoogleMapManager()) : instance;
    }


    public interface MapCallback {
        void onMapReady(GoogleMap googleMap);
    }
}
