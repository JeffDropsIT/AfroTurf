package com.example.a21__void.afroturf.object;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by ASANDA on 2018/12/01.
 * for Pandaphic
 */
public class BookmarkAfroObject extends AfroObject {
    private AfroObject markedObject;
    private String bookmarkID;

    public  BookmarkAfroObject(){

    }

    public String getMarkObjectUID(){
        return this.markedObject != null ? this.markedObject.getUID() : null;
    }

    public String getInfo(){
        if(this.markedObject instanceof SalonAfroObject)
            return "";//((SalonAfroObject)this.markedObject).location.address;
        else
            return "";
    }

    @Override
    public String getName() {
        return "";//markedObject.getName();
    }

    @Override
    public String getUID() {
        return this.bookmarkID;
    }

    @Override
    public void set(JsonParser parser, String json) {
        this.set(parser, parser.parse(json));
    }

    @Override
    public void set(JsonParser parser, JsonElement jsonElement) {
        JsonObject bookmark = jsonElement.getAsJsonObject();
        Log.i("TGIM", bookmark.toString());
        this.bookmarkID =  "" + bookmark.get("bookmarkId").getAsInt();

//       // JsonObject jsonMarkedObject = bookmark.get("markedObject").getAsJsonObject();
//        if(!jsonMarkedObject.has("stylistId")){
//            SalonAfroObject salonAfroObject = new SalonAfroObject();
//            salonAfroObject.set(parser, jsonMarkedObject.toString());
//            this.markedObject = salonAfroObject;
//        }
    }

    @Override
    public JsonElement asJson() {
        JsonObject bookmark = new JsonObject();
        JsonParser parser = new JsonParser();
        bookmark.addProperty("bookmarkId", this.bookmarkID);
//        bookmark.add("markedObject", markedObject.asJson().getAsJsonObject());
        return bookmark;
    }
}
