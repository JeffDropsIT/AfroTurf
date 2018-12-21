package com.example.a21__void.afroturf.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.a21__void.Modules.ServerCon;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.object.UserAfroObject;
import com.example.a21__void.afroturf.pkgConnection.DevDesignRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by ASANDA on 2018/12/05.
 * for Pandaphic
 */
public class UserManager {
    private static final String USER_JSON = "user_json", TOKEN = "user_token";
    private static UserManager instance = null;
    private final ServerCon serverCon;
    private UserAfroObject user = null;
    private final SharedPreferences sharedPreferences;


    private UserManager(Context context){
        this.serverCon = new ServerCon(context);

        this.sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(USER_JSON, null);
        String token = sharedPreferences.getString(TOKEN, null);

        if(userJson != null) {
            this.user = new UserAfroObject();
            this.user.set(new JsonParser(), userJson);
            this.user.setToken(token);
        }else{
            this.user = null;
        }
    }

    public void signUpUser(String name, String username, String password, final CacheManager.ManagerRequestListener<UserAfroObject> callback){
        String signUpUserUrl = "http://ec2-52-15-103-215.us-east-2.compute.amazonaws.com/afroturf/user/register";
        JsonObject body = new JsonObject();
        body.addProperty("fname", name);
        body.addProperty("username", username);
        body.addProperty("password", password);

        this.serverCon.HTTP(Request.Method.POST, signUpUserUrl, 0, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                JsonParser parser = new JsonParser();
                JsonObject result = parser.parse(response.data).getAsJsonObject();

                JsonObject userData = result.getAsJsonObject("userData");
                String token = result.getAsJsonObject("token").get("token").getAsString();

                UserAfroObject userAfroObject = new UserAfroObject();
                userAfroObject.set(parser, userData);
                userAfroObject.setToken(token);

                UserManager.this.user = userAfroObject;
                UserManager.this.saveUser(userAfroObject);
                UserManager.this.saveToken(token);
                callback.onRespond(userAfroObject);
            }
        }
        , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(callback != null){
                    if(error.networkResponse != null){
                        callback.onApiError(CacheManager.parseApiError(error.networkResponse));
                    }else{
                        callback.onApiError(new CacheManager.ApiError(-1, error.getMessage()));
                    }
                }
            }
        }, body);
    }

    private void saveToken(String token) {
        if(token != null)
            this.sharedPreferences.edit().putString(TOKEN, token).apply();;
    }

    private void saveUser(UserAfroObject userAfroObject) {
        if(userAfroObject != null)
            this.sharedPreferences.edit().putString(USER_JSON, userAfroObject.toString()).apply();
    }

    public boolean isLoggedIn(){
        return false;//this.user != null && !this.isTokenExpired(this.user.getToken());
    }

    public boolean isTokenExpired(String token){
        return false;
    }

    public final void release(){
        this.user = null;
        instance = null;
    }

    public static UserManager getInstance(Context context){
        return instance == null ? instance = new UserManager(context) : instance;
    }

    public UserAfroObject getCurrentUser() {
        return null;
    }

    public void login(String username, String password, final CacheManager.ManagerRequestListener<UserAfroObject> callback) {
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("password", password);

        String url  = ServerCon.BASE_URL + "/afroturf/user/login";
        this.serverCon.HTTP(Request.Method.POST, url, ServerCon.TIMEOUT, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                Log.i("TGIS", response.data);
                if(callback != null)
                    callback.onRespond(null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse != null){
                    callback.onApiError(CacheManager.parseApiError(error.networkResponse));
                }else{
                    callback.onApiError(new CacheManager.ApiError(-1, error.getMessage()));
                }
            }
        }, body);
     }

    public void registerWithSalon(SalonAfroObject salon, final CacheManager.ManagerRequestListener<UserAfroObject> callback) {
        if(this.user == null || salon == null){
            return;
        }

        String url = ServerCon.BASE_URL + "/afroturf/user/profile/salon/apply";
        JsonObject body = new JsonObject();
        body.addProperty("userId", this.user.getUID());
        body.addProperty("salonObjId",salon.getSalonObjId());

        Log.i("TGITc", body.toString());

        this.serverCon.HTTP(Request.Method.POST, url, ServerCon.TIMEOUT, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                if(callback != null)
                    callback.onRespond(UserManager.this.user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(callback != null)
                    if(error.networkResponse != null)
                        callback.onApiError(CacheManager.parseApiError(error.networkResponse));
                    else
                        callback.onApiError(new CacheManager.ApiError(-1, ""));
            }
        }, body);
    }
}
