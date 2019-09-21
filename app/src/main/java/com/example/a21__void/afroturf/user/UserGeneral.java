package com.example.a21__void.afroturf.user;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by ASANDA on 2019/08/03.
 * for Pandaphic
 */
public class UserGeneral {
    public static final String JSON_PROP_TOKEN = "token"
                                , JSON_PROP_UUID = "_id"
                                , JSON_PROP_NAME = "fname"
                                , JSON_PROP_USER_TYPE = "user_type";

    public static final int USER_TYPE_UNKNOWN = -1
                            , USER_TYPE_GENERAL = 0
                            , USER_TYPE_STYLIST = 1
                            , USER_TYPE_OWNER = 2;

    private JsonObject jsonUser;

    public UserGeneral set(String rawUser, JsonParser parse){
        if(rawUser == null || parse == null)
            return this;
        else
            return this.set(parse.parse(rawUser).getAsJsonObject());
    }
    public UserGeneral set(JsonObject pJsonUser){
        if(pJsonUser == null)
            return this;
        this.jsonUser = pJsonUser;

        return this;
    }

    protected JsonElement get(String propName){ return (this.jsonUser != null && this.jsonUser.has(propName)) ? this.jsonUser.get(propName) : null; }

    public String getToken(){
        JsonElement token = get(JSON_PROP_TOKEN);
        return token != null ? token.getAsString() : null;
    }
    public String getUUID(){
        JsonElement uuid = get(JSON_PROP_UUID);
        return uuid != null ? uuid.getAsString() : null;
    }
    public String getName(){
        JsonElement name = get(JSON_PROP_NAME);
        return name != null ? name.getAsString() : null;
    }
    public int getUserType(){
        JsonElement userType = get(JSON_PROP_USER_TYPE);
        return (userType != null) ? userType.getAsInt() : UserGeneral.USER_TYPE_UNKNOWN;
    }

    public void release(){ this.jsonUser = null; }

    @NonNull
    @Override
    public String toString() {
        return this.jsonUser != null ? this.jsonUser.toString() : "@null";
    }
}
