package com.example.a21__void.afroturf.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by ASANDA on 2019/09/14.
 * for Pandaphic
 */
public class LocationManager {
    public static final int LOCATION_TYPE_GPS = 0, LOCATION_TYPE_NETWORK = 1;
    private static final String KEY_LAST_LOCATION = "key.last.location"
                                , LOCATION_SEPARATOR = ":";
    private static LocationManager instance;

    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final Context context;
    private final SharedPreferences sharedPreferences;

    private SalonAfroObject.Location currentLocation;

    private LocationManager(Context pContext){
        this.context = pContext;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(pContext);
        this.sharedPreferences = pContext.getSharedPreferences(pContext.getPackageName(), Context.MODE_PRIVATE);

        String lastLocation = sharedPreferences.getString(KEY_LAST_LOCATION, null);
        if(lastLocation != null){
            String[] parts = lastLocation.split(LOCATION_SEPARATOR);
            this.currentLocation = new SalonAfroObject.Location(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]));
        }
    }

    public SalonAfroObject.Location getDeviceLocation(LocationListener callback){
        if(ContextCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            this.fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null)
                                LocationManager.this.setCurrentLocation(new SalonAfroObject.Location((float)location.getLatitude(), (float)location.getLongitude()));

                            if(callback != null)
                                callback.onGetLocation(currentLocation, location != null);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(callback != null)
                                callback.onGetLocation(currentLocation, false);
                        }
                    });
        }else{
            if(callback != null)
                callback.onGetLocation(currentLocation, false);
        }

        return this.currentLocation;
    }

    private void setCurrentLocation(SalonAfroObject.Location location){
        this.currentLocation = location;
        this.sharedPreferences.edit().putString(KEY_LAST_LOCATION, location.latitude + LOCATION_SEPARATOR + location.longitude).apply();
    }

    public SalonAfroObject.Location getCurrentLocation() {
        return null;//currentLocation;
    }

    public static LocationManager getInstance(Context context){
        return instance != null ? instance : (instance = new LocationManager(context));
    }

    public interface LocationListener{
        void onGetLocation(SalonAfroObject.Location location, boolean current);
        void onError();
    }
}
