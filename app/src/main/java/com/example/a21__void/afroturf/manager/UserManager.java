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
import com.example.a21__void.afroturf.pkgCommon.APIConstants;
import com.example.a21__void.afroturf.pkgConnection.DevDesignRequest;
import com.example.a21__void.afroturf.user.UserGeneral;
import com.example.a21__void.afroturf.user.UserOwner;
import com.example.a21__void.afroturf.user.UserStylist;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

/**
 * Created by ASANDA on 2018/12/05.
 * for Pandaphic
 */
public class UserManager {
    private static final String USER_JSON = "user_json", TOKEN = "user_token";
    private static final String JSON_PROP_USER_DATA = "userData";

    private static UserManager instance = null;
    private final ServerCon serverCon;
    private UserGeneral user;
    private final SharedPreferences sharedPreferences;


    private UserManager(Context context){
        this.serverCon = new ServerCon(context);

        this.sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        String rawUser = sharedPreferences.getString(USER_JSON, null);

        if(rawUser != null) {
            JsonParser parser = new JsonParser();
            JsonObject jsonUser = parser.parse(rawUser).getAsJsonObject();

            this.setUser(jsonUser);
        }else{
            this.user = null;
        }
    }

    private UserGeneral setUser(JsonObject jsonUser){
        int userType = UserGeneral.USER_TYPE_GENERAL;

        if(jsonUser.has(UserGeneral.JSON_PROP_USER_TYPE))
            userType = jsonUser.get(UserGeneral.JSON_PROP_USER_TYPE).getAsInt();

        switch (userType){
            case UserGeneral.USER_TYPE_GENERAL:
                this.user = new UserGeneral();
                break;
            case UserGeneral.USER_TYPE_STYLIST:
                this.user = new UserStylist();
                break;
            case UserGeneral.USER_TYPE_OWNER:
                this.user = new UserOwner();
                break;
        }

        this.user.set(jsonUser);
        return this.user;
    }


    public void setUserType(String uuid, int userType, CacheManager.ManagerRequestListener<Boolean> callback){
        String userTypeStr = userTypeToString(userType);
        if(uuid == null || userTypeStr == null){
            callback.onApiError(new CacheManager.ApiError(APIConstants.HTTP_BAD_REQUEST, "uuid or userType unknown"));
        }

        String url = "http://ec2-52-15-103-215.us-east-2.compute.amazonaws.com/afroturf/user/edit/profile";

        JsonObject body = new JsonObject();
        body.addProperty(APIConstants.FIELD_UID, uuid);
        body.addProperty(APIConstants.FIELD_TYPE,  userTypeStr);

        this.serverCon.HTTP(Request.Method.POST, url, APIConstants.TIME_OUT
                , new Response.Listener<DevDesignRequest.DevDesignResponse>() {
                    @Override
                    public void onResponse(DevDesignRequest.DevDesignResponse response) {
                        if(callback != null)
                            callback.onRespond(true);
                    }
                }
                , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(callback != null){
                            callback.onApiError(CacheManager.parseApiError(error));
                        }
                    }
                }
                , body);
    }

    public void signUpUser(String name, String username, String password, int userType, final CacheManager.ManagerRequestListener<UserGeneral> callback){
        if(name == null || username == null || password == null || userType < 0){
            callback.onApiError(new CacheManager.ApiError(APIConstants.HTTP_BAD_REQUEST, "Invalid Parameter."));
            return;
        }

        String signUpUserUrl = "http://ec2-52-15-103-215.us-east-2.compute.amazonaws.com/afroturf/user/register";

        JsonObject body = new JsonObject();
        body.addProperty(APIConstants.FIELD_NAME , name);
        body.addProperty(APIConstants.FIELD_USERNAME , username);
        body.addProperty(APIConstants.FIELD_PASSWORD , password);
        body.addProperty(APIConstants.FIELD_USER_TYPE, userType);

        this.serverCon.HTTP(Request.Method.POST, signUpUserUrl, 0, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                JsonParser parser = new JsonParser();
                JsonObject result = parser.parse(response.data).getAsJsonObject();

                if(result.has(APIConstants.FIELD_USER_DATA)){
                    JsonObject jsonUserData = result.getAsJsonObject(JSON_PROP_USER_DATA);
                    String token = result.getAsJsonObject(UserGeneral.JSON_PROP_TOKEN).get(UserGeneral.JSON_PROP_TOKEN).getAsString();

                    jsonUserData.addProperty(UserGeneral.JSON_PROP_TOKEN, token);

                    if(!jsonUserData.has(UserGeneral.JSON_PROP_USER_TYPE))
                        jsonUserData.addProperty(UserGeneral.JSON_PROP_USER_TYPE, userType);

                    UserManager.this.cacheUser(UserManager.this.setUser(jsonUserData));

                    UserManager.this.setUserType(UserManager.this.user.getUUID()
                            , userType
                            , new CacheManager.ManagerRequestListener<Boolean>() {

                        @Override
                        public void onRespond(Boolean result) {
                            if(result && callback != null){
                                callback.onRespond(UserManager.this.user);
                            }


                        }

                        @Override
                        public void onApiError(CacheManager.ApiError apiError) {
                            callback.onApiError(new CacheManager.ApiError(APIConstants.HTTP_INTERNAL_SERVER_ERROR, "Sorry, An Internal Server Error has occurred. Please try again later."));
                        }
                    });
                }else{
                    callback.onApiError(new CacheManager.ApiError(APIConstants.HTTP_INTERNAL_SERVER_ERROR, "Sorry, An Internal Server Error has occurred. Please try again later."));
                }
            }
        }
        , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(callback != null){
                    callback.onApiError(CacheManager.parseApiError(error));
                }
            }
        }, body);
    }

    public String userTypeToString(int userType){
        switch (userType){
            case UserGeneral.USER_TYPE_GENERAL:
                return "user";
            case UserGeneral.USER_TYPE_STYLIST:
                return "stylist";
            case UserGeneral.USER_TYPE_OWNER:
                return "owner";
            default:
                return null;
        }
    }

    private int userTypeFromString(String userType) {
        switch (userType){
            case "user":
                return UserGeneral.USER_TYPE_GENERAL;
            case "stylist":
                return UserGeneral.USER_TYPE_STYLIST;
            case "owner":
                return UserGeneral.USER_TYPE_OWNER;
            default:
                return UserGeneral.USER_TYPE_GENERAL;
        }
    }

    private void saveToken(String token) {
        if(token != null)
            this.sharedPreferences.edit().putString(TOKEN, token).apply();;
    }

    private void cacheUser(UserGeneral userAfroObject) {
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

    public UserGeneral getCurrentUser() {
        if(this.user == null){
            String rawJsonUser = this.sharedPreferences.getString(USER_JSON, null);
            JsonParser parser = new JsonParser();
            JsonObject jsonUserObject = parser.parse(rawJsonUser).getAsJsonObject();

            this.user = this.setUser(jsonUserObject);
        }

        return this.user;
    }

    public void login(String username, String password, final CacheManager.ManagerRequestListener<UserGeneral> callback) {
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("password", password);

        String url  = ServerCon.BASE_URL + "/afroturf/user/login";
        this.serverCon.HTTP(Request.Method.POST, url, ServerCon.TIMEOUT, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                JsonParser parser = new JsonParser();
                JsonObject jsonUserObject = parser.parse(response.data).getAsJsonObject();

                if(jsonUserObject.has(APIConstants.FIELD_TYPE))
                    jsonUserObject.addProperty(APIConstants.FIELD_TYPE, userTypeFromString(jsonUserObject.get(APIConstants.FIELD_TYPE).getAsString()));
                else{
                    jsonUserObject.addProperty(APIConstants.FIELD_TYPE, UserGeneral.USER_TYPE_GENERAL);
                }

                UserGeneral user = UserManager.this.setUser(jsonUserObject);
                UserManager.this.cacheUser(user);

                if(callback != null)
                    callback.onRespond(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(callback != null){
                    callback.onApiError(CacheManager.parseApiError(error));
                }
            }
        }, body);
     }



    public void registerWithSalon(SalonAfroObject salon, final CacheManager.ManagerRequestListener<UserGeneral> callback) {
        if((this.user == null || salon == null) && !(this.user instanceof UserStylist)){
            callback.onApiError(new CacheManager.ApiError(APIConstants.HTTP_BAD_REQUEST, "Invalid Parameter."));
            return;
        }

        String url = ServerCon.BASE_URL + "/afroturf/user/profile/salon/apply";

        JsonObject body = new JsonObject();
        body.addProperty(APIConstants.FIELD_USER_ID, this.user.getUUID());
        body.addProperty(APIConstants.FIELD_SALON_OBJ_ID, salon.getSalonObjId());

        this.serverCon.HTTP(Request.Method.POST, url, ServerCon.TIMEOUT, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                if(callback != null)
                    callback.onRespond(UserManager.this.user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(callback != null){
                    callback.onApiError(CacheManager.parseApiError(error));
                }
            }
        }, body);
    }
}
