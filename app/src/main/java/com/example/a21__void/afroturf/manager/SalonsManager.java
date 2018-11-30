package com.example.a21__void.afroturf.manager;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.a21__void.Modules.ServerCon;
import com.example.a21__void.afroturf.database.AfroObjectDatabaseHelper;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.pkgConnection.DevDesignRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

/**
 * Created by ASANDA on 2018/07/28.
 * for Pandaphic
 */
public class SalonsManager extends CacheManager {
    public static final String DB_DATABASE_NAME = "salon.db", DB_TABLE_NAME = "salons";
    private static final String BASE_URL = "https://damp-crag-30377.herokuapp.com/afroturf";
    private static SalonsManager instance;

    private final ServerCon serverCon;

    public SalonsManager(Context context){
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

    @Override
    public void init(Context context) {
        this.fetchSalons(new ManagerRequestListener<SalonAfroObject[]>() {
            @Override
            public void onRespond(SalonAfroObject[] result) {
                SalonsManager.this.cacheData(result);
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
                    });
                }else
                    if(callback != null)
                    callback.onRespond(SalonsManager.this);
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


    public static SalonsManager getInstance(Context context){
        return (instance == null) ? (instance = new SalonsManager(context)) : instance;
    }
}
