package com.example.a21__void.afroturf.object;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by ASANDA on 2018/12/05.
 * for Pandaphic
 */
public class UserAfroObject extends AfroObject {
    public static final String JSON_PROP_UUID = "_id"
                                , JSON_PROP_NAME = "fname"
                                , JSON_PROP_TYPE = "type";

    private String name, uid, reviewsDocId;

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


        this.uid = user.get(JSON_PROP_UUID).getAsString();
        this.name = user.get(JSON_PROP_NAME).getAsString();
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


}
