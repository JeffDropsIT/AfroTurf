package com.example.a21__void.Modules;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.a21__void.afroturf.HomeActivity;
import com.example.a21__void.afroturf.MySalon;
import com.example.a21__void.afroturf.pkgConnection.DevDesignRequest;
import com.example.a21__void.afroturf.pkgConnection.HttpStatus;
import com.example.a21__void.afroturf.pkgSalon.BlankFragment;
import com.example.a21__void.afroturf.pkgSalon.SalonObject;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ASANDA on 2018/07/28.
 * for Pandaphic
 */
public class SalonsManager {
    private static final String BASE_URL = "https://damp-crag-30377.herokuapp.com/afroturf";
    private static SalonsManager instance;
    private final List<SalonObject> salonObjectsList;
    private final ServerCon serverCon;

    private SalonsManager(Context context){
        this.salonObjectsList = new ArrayList<>();
        this.serverCon = ServerCon.getInstance(context);
    }

    public void fetchSalons(final Location location, final SalonsManagerCallback managerCallback){
        //TODO get salons remotly
        String url = BASE_URL + "/salons/shallow?location=" + location.getLatitude() + ',' + location.getLongitude() + "&radius=15&limit=4";
        serverCon.HTTP(Request.Method.GET, url, 0, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                if(response.statusCode == HttpStatus.OK){
                    JsonParser jsonParser = new JsonParser();
                    try{
                        Log.i("rsb", "onResponse: " + response.data);
                        JsonArray rawSalons = jsonParser.parse(response.data).getAsJsonArray();
                        salonObjectsList.clear();
                        for(int pos = 0; pos < rawSalons.size(); pos++){
                            JsonObject rawSalon = rawSalons.get(pos).getAsJsonObject();
                            salonObjectsList.add(SalonObject.parse(rawSalon));
                        }
                        managerCallback.onFetchSalons(getSalons());

                    }catch (JsonParseException pE){
                        pE.printStackTrace();
                        managerCallback.onFetchSalons(null);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                managerCallback.onFetchSalons(null);
            }
        });

    }

    public SalonObject[] getSalons(){
        return this.salonObjectsList.toArray(new SalonObject[salonObjectsList.size()]);
    }

    public static SalonsManager getInstance(Context context){
        return (instance == null) ? (instance = new SalonsManager(context)) : instance;
    }

    public void fetchSalons( final SalonsManagerCallback managerCallback) {
        String url= ServerCon.BASE_URL + "/afroturf/salons/-a";
        serverCon.HTTP(0, url, 0, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                JsonParser parser = new JsonParser();
                JsonArray salons = parser.parse(response.data).getAsJsonArray();
                salonObjectsList.clear();
                for(int pos = 0; pos < salons.size(); pos++){
                    JsonObject rawSalon = salons.get(pos).getAsJsonObject();
                    salonObjectsList.add(SalonObject.parse(rawSalon));
                }

                if(managerCallback != null)
                    managerCallback.onFetchSalons(getSalons());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    public interface SalonsManagerCallback{
        void onFetchSalons(SalonObject[] salonObjects);
    }
}
