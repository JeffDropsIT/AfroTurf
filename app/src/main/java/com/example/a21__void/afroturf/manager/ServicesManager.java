package com.example.a21__void.afroturf.manager;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.a21__void.Modules.ServerCon;
import com.example.a21__void.afroturf.object.ServiceAfroObject;
import com.example.a21__void.afroturf.pkgConnection.DevDesignRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ASANDA on 2018/10/01.
 * for Pandaphic
 */
public class ServicesManager extends CacheManager {
    public static final String DB_DATABASE_NAME = "services.db", DB_TABLE_NAME = "services";
    private static ServicesManager instance;
    private final ServerCon serverCon;

    public ServicesManager(Context context){
        super(context);
        this.serverCon = ServerCon.getInstance(context);
    }

    @Override
    public String getDatabaseName() {
        return DB_DATABASE_NAME;
    }

    @Override
    public String getTableName() {
        return DB_TABLE_NAME;
    }

    private void fetchServices(final ManagerRequestListener<ServiceAfroObject[]> callback){
        String url = ServerCon.BASE_URL + "/afroturf/" + ServerCon.DEBUG_SALON_ID + "/service/-a";
        serverCon.HTTP(Request.Method.GET, url, 0, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                JsonParser parser = new JsonParser();
                JsonObject salonObject = parser.parse(response.data).getAsJsonArray().get(0).getAsJsonObject();

                JsonArray services = salonObject.getAsJsonArray("services");
                ArrayList<ServiceAfroObject> serviceAfroObjects = new ArrayList<>();

                for(int pos = 0; pos < services.size(); pos++){
                    JsonObject service = services.get(pos).getAsJsonObject();
                    String category = service.get("name").getAsString();
                    JsonArray subServices = service.get("subservices").getAsJsonArray();

                    for(int i = 0; i < subServices.size(); i++){
                        JsonObject subService = subServices.get(i).getAsJsonObject();
                        subService.addProperty("category", category);
                        Log.i("ixa", subService.toString());
                        ServiceAfroObject serviceAfroObject =  new ServiceAfroObject();//objectMapper.readValue(subService.toString(), ServiceAfroObject.class);
                        serviceAfroObject.set(parser, subService.toString());
                        serviceAfroObjects.add(serviceAfroObject);
                    }
                }

                callback.onRespond(serviceAfroObjects.toArray(new ServiceAfroObject[serviceAfroObjects.size()]));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public void init(Context context) {
        this.fetchServices(new ManagerRequestListener<ServiceAfroObject[]>() {
            @Override
            public void onRespond(ServiceAfroObject[] result) {
                ServicesManager.this.cacheData(result);
            }

            @Override
            public void onApiError(ApiError apiError) {
                //todo error
            }
        });
    }

    @Override
    public void beginManagement() {
        this.getCachePointer().swapCursor(this.afroObjectDatabaseHelper.getAll());
    }

    @Override
    public void notifyCacheChanged() {
        this.getCachePointer().swapCursor(this.afroObjectDatabaseHelper.getAll());
    }

    @Override
    public void requestRefresh(final ManagerRequestListener<CacheManager> callback) {
        Log.i("HNS", "requestRefresh: ");
        isDataInSync(new ManagerRequestListener<Boolean>() {
            @Override
            public void onRespond(Boolean inSync) {
                if(!inSync){
                    ServicesManager.this.fetchServices(new ManagerRequestListener<ServiceAfroObject[]>() {
                        @Override
                        public void onRespond(ServiceAfroObject[] result) {
                            Log.i("HNS", "onRespond: " + result.length);
                            cacheData(result);
                            if(callback != null)
                                callback.onRespond(ServicesManager.this);
                        }

                        @Override
                        public void onApiError(ApiError apiError) {
                            //todo data
                        }
                    });
                }else{
                    if(callback != null)
                        callback.onRespond(ServicesManager.this);
                }
            }

            @Override
            public void onApiError(ApiError apiError) {
                if(callback != null)
                        callback.onApiError(apiError);
            }
        });
    }

    @Override
    public void clearCache() {
        this.afroObjectDatabaseHelper.clear();
    }

    @Override
    protected void isDataInSync(ManagerRequestListener<Boolean> callback) {
        callback.onRespond(true);
    }

    public static ServicesManager getInstance(Context context){ return (instance != null) ? instance : (instance = new ServicesManager(context)); }
}
