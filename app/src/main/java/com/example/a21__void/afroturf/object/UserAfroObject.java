package com.example.a21__void.afroturf.object;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by ASANDA on 2018/12/05.
 * for Pandaphic
 */
public class UserAfroObject extends AfroObject {
    private String name, uid, reviewsDocId;
    private String token;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getUID() {
        return this.uid;
    }

    @Override
    public void set(JsonParser parser, JsonElement jsonElement) {
        JsonObject user = jsonElement.getAsJsonObject();


        this.uid = user.get("_id").getAsString();
        this.name = user.get("fname").getAsString();
        //this.reviewsDocId = user.get("reviewsDocId").getAsString();

    }

    @Override
    public JsonElement asJson() {
        JsonObject user = new JsonObject();
        user.addProperty("_id", this.uid);
        user.addProperty("fname", this.name);
        //user.addProperty("reviewsDocId", this.reviewsDocId);
        return user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String pToken){
        this.token = pToken;
    }
}
