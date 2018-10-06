package com.example.a21__void.afroturf.manager;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.a21__void.Modules.ServerCon;
import com.example.a21__void.afroturf.object.StylistAfroObject;
import com.example.a21__void.afroturf.pkgConnection.DevDesignRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASANDA on 2018/10/01.
 * for Pandaphic
 */
public class StylistsManager extends CacheManager {
    public static final String DB_DATABASE_NAME = "stylists.db", DB_TABLE_NAME = "stylists";
    private final ServerCon serverCon;

    public StylistsManager(Context context) {
        super(context);
        this.serverCon = new ServerCon(context);
    }

    void fetchStylist(final ManagerRequestListener<StylistAfroObject[]> callback){
        String url =  ServerCon.BASE_URL + "/afroturf/filter/" + ServerCon.DEBUG_SALON_ID + "/stylist?query={}";
        final ObjectMapper objectMapper = this.afroObjectDatabaseHelper.objectMapper;

        serverCon.HTTP(Request.Method.GET, url, 0, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                JsonParser jsonParser = new JsonParser();
                JsonObject resultOrAnd = jsonParser.parse(response.data).getAsJsonObject().getAsJsonArray("data").get(0).getAsJsonObject();

                JsonArray stylists = resultOrAnd.getAsJsonArray("or").get(0).getAsJsonArray().get(0).getAsJsonObject().getAsJsonArray("stylists");

                StylistAfroObject[] stylistObjectLists = new StylistAfroObject[stylists.size()];
                for(int pos = 0; pos < stylists.size(); pos++){
                    JsonObject stylist = stylists.get(pos).getAsJsonObject();
                    StylistAfroObject stylistAfroObject = new StylistAfroObject();
                    stylistAfroObject.set(jsonParser, stylist.toString());
                    stylistObjectLists[pos] = stylistAfroObject;
                }
                callback.onRespond(stylistObjectLists);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
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
        fetchStylist(new ManagerRequestListener<StylistAfroObject[]>() {
            @Override
            public void onRespond(StylistAfroObject[] result) {
                cacheData(result);
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
        isDataInSync(new ManagerRequestListener<Boolean>() {
            @Override
            public void onRespond(Boolean inSync) {
                if(!inSync){
                    fetchStylist(new ManagerRequestListener<StylistAfroObject[]>() {
                        @Override
                        public void onRespond(StylistAfroObject[] result) {
                            cacheData(result);
                            if(callback != null)
                                callback.onRespond(StylistsManager.this);
                        }
                    });
                }else {
                    if (callback != null)
                        callback.onRespond(StylistsManager.this);
                }
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
}
