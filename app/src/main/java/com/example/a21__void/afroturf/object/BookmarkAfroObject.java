package com.example.a21__void.afroturf.object;

import android.util.Log;

import com.example.a21__void.afroturf.pkgCommon.APIConstants;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by ASANDA on 2018/12/01.
 * for Pandaphic
 */
public class BookmarkAfroObject extends AfroObject {
    public static final int MARKED_TYPE_SALON = 0, MARKED_TYPE_USER = 1;

    private String markedObjectUID;
    private String bookmarkID, title, info;
    private int markedType;

    public  BookmarkAfroObject() {
    }

    public int getMarkedType() {
        return markedType;
    }

    public String getMarkedObjectUID() {
        return markedObjectUID;
    }

    public String getInfo(){
        return this.info;
    }

    @Override
    public String getName() { return title; }

    @Override
    public String getUID() { return this.bookmarkID; }

    @Override
    public void set(JsonParser parser, String json) {
        this.set(parser, parser.parse(json));
    }

    @Override
    public void set(JsonParser parser, JsonElement jsonElement) {
        JsonObject bookmark = jsonElement.getAsJsonObject();
        this.bookmarkID =  "" + bookmark.get("bookmarkId").getAsInt();
        this.title = bookmark.get(APIConstants.FIELD_TITLE).getAsString();
        this.info = bookmark.get(APIConstants.FIELD_INFO).getAsString();

        this.markedObjectUID = bookmark.get(APIConstants.FIELD_SALON_ID).getAsInt() + "";
        this.markedType = MARKED_TYPE_SALON;
    }

    @Override
    public JsonElement asJson() {
        JsonObject bookmark = new JsonObject();

        bookmark.addProperty("bookmarkId", Integer.parseInt(this.bookmarkID));
        bookmark.addProperty(APIConstants.FIELD_TITLE, this.title);
        bookmark.addProperty(APIConstants.FIELD_INFO, this.info);
        bookmark.addProperty(APIConstants.FIELD_SALON_ID, Integer.parseInt(this.markedObjectUID));

        return bookmark;
    }
}
