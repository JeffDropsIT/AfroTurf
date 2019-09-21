package com.example.a21__void.afroturf.manager;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.a21__void.Modules.ServerCon;
import com.example.a21__void.afroturf.Interpol;
import com.example.a21__void.afroturf.database.AfroObjectDatabaseHelper;
import com.example.a21__void.afroturf.object.AfroObject;
import com.example.a21__void.afroturf.object.BookmarkAfroObject;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.pkgCommon.APIConstants;
import com.example.a21__void.afroturf.pkgConnection.DevDesignRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ASANDA on 2018/07/28.
 * for Pandaphic
 */
public class SalonsManager extends CacheManager {
    public static final String DB_DATABASE_NAME = "salon.db", DB_TABLE_NAME = "salons";
    private static final String BASE_URL = "https://damp-crag-30377.herokuapp.com/afroturf";
    private static SalonsManager instance;

    private final ServerCon serverCon;
    private SalonAfroObject.Location currentLocation;

    public SalonsManager(Context context){
        super(context);
        this.serverCon = ServerCon.getInstance(context);
    }

    public ArrayList<SalonAfroObject> getSalons(){ return this.getSalons(0, this.getCachePointer().ItemCount()); }
    public ArrayList<SalonAfroObject> getSalons(int index, int count){
        if(this.getCachePointer().ItemCount() == 0 || this.getCachePointer().getCursor() == null)
            return new ArrayList<>();

        if(index < 0)
            index = 0;
        else if(index >= this.getCachePointer().ItemCount())
            index = this.getCachePointer().ItemCount() - 1;

        if(count < 0)
            count = 0;
        else if(index + count >= this.getCachePointer().ItemCount())
            count = this.getCachePointer().ItemCount() - index;

        Cursor cursor = this.getCachePointer().getCursor();
        cursor.moveToPosition(index);

        int counter = count;
        ArrayList<SalonAfroObject> salons = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();

        do{
            byte[] rawJsonString = cursor.getBlob(cursor.getColumnIndex(CacheManagerIX.ENTRY_RAW_OBJECT));

            SalonAfroObject salon = new SalonAfroObject();
            salon.set(jsonParser, new String(rawJsonString));

            salons.add(salon);
            counter--;
        }while (cursor.moveToNext() && counter > 0);

        return salons;
    }
    public ArrayList<SalonAfroObject> getSalons(int index, int count, ManagerRequestListener<ArrayList<SalonAfroObject>> callback){

        this.requestRefresh(new ManagerRequestListener<CacheManager>() {
            @Override
            public void onRespond(CacheManager result) {
                if(callback != null)
                    callback.onRespond(SalonsManager.this.getSalons());
            }

            @Override
            public void onApiError(ApiError apiError) {
                if(callback != null)
                    callback.onApiError(apiError);
            }
        });

        return this.getSalons(index, count);
    }
    public ArrayList<SalonAfroObject> getSalons(ManagerRequestListener<ArrayList<SalonAfroObject>> callback){
        return this.getSalons( 0, this.getCachePointer().ItemCount(), callback);
    }



    public ArrayList<SalonAfroObject> get(int index, int length){
        ArrayList<SalonAfroObject> results = new ArrayList<>();
        CachePointer pointer = this.getCachePointer();
        if(pointer == null || pointer.getCursor() == null)
            return results;

        Cursor cursor = pointer.getCursor();
        cursor.moveToPosition(index);

        int count = Math.abs(length);
        JsonParser parser = new JsonParser();
        do{
            byte[] rawJson = cursor.getBlob(cursor.getColumnIndex(AfroObjectDatabaseHelper.COLUMN_JSON));
            SalonAfroObject salonAfroObject = new SalonAfroObject();
            salonAfroObject.set(parser, new String(rawJson));
            results.add(salonAfroObject);
            count--;
        }while (cursor.moveToNext() && count > 0);

        return results;
    }

    @Override
    public String getDatabaseName() {
        return DB_DATABASE_NAME;
    }

    @Override
    public String getTableName() {
        return DB_TABLE_NAME;
    }

    @Override
    public void init(Context context) {
        this.fetchSalons(new ManagerRequestListener<SalonAfroObject[]>() {
            @Override
            public void onRespond(SalonAfroObject[] result) {
                SalonsManager.this.cacheData(result);
            }

            @Override
            public void onApiError(ApiError apiError) {

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

    private void fetchSalons(final ManagerRequestListener<SalonAfroObject[]> callback) {
        String url= ServerCon.BASE_URL + "/afroturf/salons/-a";
        serverCon.HTTP(0, url, 0, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                JsonParser parser = new JsonParser();
                JsonArray salons = parser.parse(response.data).getAsJsonArray();

                SalonAfroObject[] salonAfroObjects = new SalonAfroObject[salons.size()];
                for(int pos = 0; pos < salons.size(); pos++){
                    JsonObject salon = salons.get(pos).getAsJsonObject();
                    SalonAfroObject salonAfroObject = new SalonAfroObject();
                    salonAfroObject.set(parser, salon.toString());
                    salonAfroObjects[pos] = salonAfroObject;//SalonsManager.this.afroObjectDatabaseHelper.objectMapper.readValue(salon.toString(), SalonAfroObject.class);
                }
                if(callback != null)
                    callback.onRespond(salonAfroObjects);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(callback != null)
                    callback.onApiError(CacheManager.parseApiError(error));
            }
        });
    }


    @Override
    public void requestRefresh(final ManagerRequestListener<CacheManager> callback) {
        this.isDataInSync(new ManagerRequestListener<Boolean>() {
            @Override
            public void onRespond(Boolean inSync) {
                if(!inSync){
                    SalonsManager.this.fetchSalons(new ManagerRequestListener<SalonAfroObject[]>() {
                        @Override
                        public void onRespond(SalonAfroObject[] result) {
                            cacheData(result);
                            if(callback != null)
                            callback.onRespond(SalonsManager.this);
                        }

                        @Override
                        public void onApiError(ApiError apiError) {
                            if(callback != null)
                                callback.onApiError(apiError);
                        }
                    });
                }else
                    if(callback != null)
                    callback.onRespond(SalonsManager.this);
            }

            @Override
            public void onApiError(ApiError apiError) {
                if(callback != null)
                    callback.onApiError(apiError);
            }
        });
    }

    @Override
    protected void isDataInSync(ManagerRequestListener<Boolean> callback) {
        //TODO implement isDataInSync
        callback.onRespond(false);
    }


    @Override
    public void clearCache() {
        this.afroObjectDatabaseHelper.clear();
    }


    @Override
    protected void remoteGet(String uid, final ManagerRequestListener<AfroObject> callback) {
        String url = ServerCon.BASE_URL + "/afroturf/user/salons/obj/?salonObj=" + uid;
        this.serverCon.HTTP(Request.Method.GET, url, ServerCon.TIMEOUT, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                SalonAfroObject salonAfroObject = new SalonAfroObject();
                JsonParser jsonParser = new JsonParser();
                salonAfroObject.set(jsonParser, response.data);
                cacheObject(salonAfroObject);

                if(callback != null)
                    callback.onRespond(salonAfroObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(callback != null)
                    if(error.networkResponse != null)
                        callback.onRespond(null);
                    else
                        callback.onApiError(new ApiError(ServerCon.ERROR_NETWORK, ServerCon.NETWORK_ERROR_MSG));
            }
        });
    }

    public static SalonsManager getInstance(Context context){
        return (instance == null) ? (instance = new SalonsManager(context)) : instance;
    }

    public ArrayList<SalonAfroObject> getAll() {
        return get(0, this.getCachePointer().getCursor().getCount());
    }

    public void registerStylist(String salonObjId, String uuid, ManagerRequestListener<String> callback) {
        String url = "http://ec2-52-15-103-215.us-east-2.compute.amazonaws.com/afroturf/user/profile/salon/apply";

        JsonObject body = new JsonObject();
        body.addProperty(APIConstants.FIELD_SALON_OBJ_ID, salonObjId);
        body.addProperty(APIConstants.FIELD_USER_ID, uuid);

        this.serverCon.HTTP(Request.Method.POST, url, 30000, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                Log.i("register stylist", "onResponse: " + response.data);

                if(callback != null)
                    callback.onRespond("test");
            }
        }
        , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(callback != null){
                    if(error.networkResponse != null){
                        callback.onApiError(CacheManager.parseApiError(error));
                    }else{
                        callback.onApiError(new CacheManager.ApiError(APIConstants.NETWORK_ERROR, error.getMessage()));
                    }
                }
            }
        }
        , body);
    }

    public ArrayList<SalonAfroObject> getHiringSalons() {
        return new ArrayList();
    }

    public ArrayList<SalonAfroObject> updateLocation(SalonAfroObject.Location userLocation, ManagerRequestListener<ArrayList<SalonAfroObject>> callback) {
        this.currentLocation = userLocation;
        return this.getSalons(callback);
    }
}
