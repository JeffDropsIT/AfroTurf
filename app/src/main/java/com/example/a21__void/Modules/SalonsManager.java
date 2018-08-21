package com.example.a21__void.Modules;

import android.location.Location;
import android.os.Handler;

import com.example.a21__void.afroturf.HomeActivity;
import com.example.a21__void.afroturf.MySalon;
import com.example.a21__void.afroturf.SalonObject;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ASANDA on 2018/07/28.
 * for Pandaphic
 */
public class SalonsManager {
    private static SalonsManager instance;
    private final List<SalonObject> salonObjects;

    private SalonsManager(){
        this.salonObjects = new ArrayList<>();
    }

    public void fetchSalons(final Location location, final SalonsManagerCallback managerCallback){
        //TODO get salons remotly
        //ServerCon.GET
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Random randomGenerator = new Random();

                for (int i = 0; i < 10; i++) {

                    double offset = i / 100d;
                    double mul1 = randomGenerator.nextBoolean() ? -1 : 1, mul2 = randomGenerator.nextBoolean() ? -1 : 1, mul3 = randomGenerator.nextBoolean() ? -1:1;

                    double nextLatitude = (location.getLatitude() + offset * mul1);
                    double nextLongitude = (location.getLongitude() + offset * mul2);

                    SalonObject salonObject = new SalonObject("Hard Coded Salon", new LatLng(nextLatitude, nextLongitude), randomGenerator.nextInt(5));
                    SalonsManager.this.salonObjects.add(salonObject);
                }

                managerCallback.onFetchSalons(SalonsManager.this.getSalons());
            }
        }, 2000);
    }

    public SalonObject[] getSalons(){
        return this.salonObjects.toArray(new SalonObject[salonObjects.size()]);
    }

    public static SalonsManager getInstance(){
        return (instance == null) ? (instance = new SalonsManager()) : instance;
    }

    public interface SalonsManagerCallback{
        void onFetchSalons(SalonObject[] salonObjects);
    }
}
