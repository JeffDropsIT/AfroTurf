package com.example.a21__void.afroturf.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.a21__void.Modules.ServerCon;
import com.example.a21__void.afroturf.object.AfroObject;
import com.example.a21__void.afroturf.object.BookmarkAfroObject;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.pkgCommon.APIConstants;
import com.example.a21__void.afroturf.pkgConnection.DevDesignRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.maps.errors.ApiError;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ASANDA on 2018/09/27.
 * for Pandaphic
 */
public class BookmarkManager extends CacheManagerIX<BookmarkAfroObject> {
    private static final String DATABASE_NAME = "bookmarks.db";
    private static final String DB_TABLE_NAME = "bookmarks";

    private final ServerCon serverCon;

    public BookmarkManager(Context context) {
        super(context);
        this.serverCon = new ServerCon(context);
    }

    public void isBookmarked(final String uid, final ManagerRequestListener<String> callback){
        this.requestRefresh(new ManagerRequestListener<CacheManager>() {
            @Override
            public void onRespond(CacheManager result) {
                int bookmarksSize = BookmarkManager.this.ramCache.size();
                String bookmarkId = null;
                for(int pos = 0; pos < bookmarksSize; pos++){
                    BookmarkAfroObject bookmark = BookmarkManager.this.ramCache.get(pos);
                    String markObjectUID = bookmark.getMarkObjectUID();
                    if(markObjectUID != null && markObjectUID.equals(uid)){
                        bookmarkId = bookmark.getUID();
                        break;
                    }
                }
                if(callback != null)
                    callback.onRespond(bookmarkId);
            }

            @Override
            public void onApiError(ApiError apiError) {
                if(callback != null)
                    callback.onApiError(apiError);
            }
        });
    }
    @Override
    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    @Override
    public String getTableName() {
        return DB_TABLE_NAME;
    }

    @Override
    public void init(Context context) {
        this.fetchBookmarks(new ManagerRequestListener<ArrayList<BookmarkAfroObject>>() {
            @Override
            public void onRespond(ArrayList<BookmarkAfroObject> result) {
                BookmarkManager.this.cacheData(result.toArray(new BookmarkAfroObject[result.size()]));
            }

            @Override
            public void onApiError(ApiError apiError) {

            }
        });
    }

    @Override
    public void beginManagement() {

    }

    @Override
    public void notifyCacheChanged() {
        super.notifyCacheChanged();
    }

    @Override
    public void requestRefresh(final ManagerRequestListener<CacheManager> callback) {
        this.isDataInSync(new ManagerRequestListener<Boolean>() {
            @Override
            public void onRespond(Boolean inSync) {
                if(!inSync){
                    BookmarkManager.this.fetchBookmarks(new ManagerRequestListener<ArrayList<BookmarkAfroObject>>() {
                        @Override
                        public void onRespond(ArrayList<BookmarkAfroObject> result) {
                            BookmarkManager.this.cacheData(result.toArray(new BookmarkAfroObject[result.size()]));
                            if(callback != null)
                                callback.onRespond(BookmarkManager.this);
                        }

                        @Override
                        public void onApiError(ApiError apiError) {
                            if(callback != null)
                                callback.onApiError(apiError);
                        }
                    });
                }else{
                    if(callback != null)
                        callback.onRespond(BookmarkManager.this);
                }
            }

            @Override
            public void onApiError(ApiError apiError) {
                if(callback != null)
                    callback.onApiError(apiError);
            }
        });
    }

    void fetchBookmarks(final ManagerRequestListener<ArrayList<BookmarkAfroObject>> callback){
        String url = "http://ec2-52-15-103-215.us-east-2.compute.amazonaws.com/afroturf/user/profile/salon/bookmarks?userId=" + ServerCon.DEBUG_USER_ID;

        this.serverCon.HTTP(Request.Method.GET, url, 0, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                JsonParser parser = new JsonParser();
                JsonArray data = parser.parse(response.data).getAsJsonArray();
                BookmarkManager.this.ramCache.clear();
                if(data.size() > 0){

                    JsonArray bookmarks = data.get(data.size() - 1).getAsJsonArray();
                    for(int pos = 0; pos < data.size() - 1; pos++){
                        JsonObject markedObject = data.get(pos).getAsJsonObject();
                        JsonObject bookmark = bookmarks.get(pos).getAsJsonObject();
                        bookmark.add("markedObject", markedObject);

                        BookmarkAfroObject bookmarkAfroObject = new BookmarkAfroObject();
                        bookmarkAfroObject.set(parser, bookmark.toString());
                        BookmarkManager.this.add(bookmarkAfroObject);
                    }

                    if(callback != null)
                        callback.onRespond(BookmarkManager.this.ramCache);
                }else{
                    callback.onRespond(BookmarkManager.this.ramCache);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public void clearCache(){
        this.afroObjectDatabaseHelper.clear();
    }

    @Override
    protected void isDataInSync(ManagerRequestListener<Boolean> callback) {
        callback.onRespond(false);
    }

    public void addBookmark(final AfroObject afroObject, String userId, final ManagerRequestListener<BookmarkAfroObject> callback) {
        String url = "http://ec2-52-15-103-215.us-east-2.compute.amazonaws.com/afroturf/user/profile/salon/bookmarks";

        JsonObject reqBody = new JsonObject();
        reqBody.addProperty(APIConstants.FEILD_USER_ID, userId);
        reqBody.addProperty(APIConstants.FEILD_SALON_ID, afroObject.getUID());

        this.serverCon.HTTP(Request.Method.POST, url, 0, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                JsonParser parser = new JsonParser();
                JsonObject data = parser.parse(response.data).getAsJsonObject();
                JsonObject bookmarkJson = data.get("data").getAsJsonObject();
                bookmarkJson.add("markedObject", afroObject.asJson());

                BookmarkAfroObject bookmark = new BookmarkAfroObject();
                bookmark.set(parser, bookmarkJson);

                BookmarkManager.this.afroObjectDatabaseHelper.add(bookmark);
                BookmarkManager.this.ramCache.add(bookmark);
                BookmarkManager.this.notifyCacheChanged();

                if(callback != null)
                    callback.onRespond(bookmark);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(callback != null)
                    callback.onApiError(BookmarkManager.this.parseApiError(error.networkResponse));
            }
        }, reqBody);

    }

    public void removeBookmark(final String bookmarkUID, final ManagerRequestListener<Boolean> callback) {
        String url = "http://ec2-52-15-103-215.us-east-2.compute.amazonaws.com/afroturf/user/profile/salon/bookmarks/" + bookmarkUID;

        this.serverCon.HTTP(Request.Method.DELETE, url, 0, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                int cacheSize = ramCache.size();
                for(int pos = 0; pos < cacheSize; pos++){
                    BookmarkAfroObject bookmark = ramCache.get(pos);
                    if(bookmark.getUID().equals(bookmarkUID)){
                        ramCache.remove(bookmark);
                        break;
                    }
                }

                BookmarkManager.this.afroObjectDatabaseHelper.remove(bookmarkUID);
                BookmarkManager.this.notifyCacheChanged();

                if(callback != null)
                    callback.onRespond(true);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(callback != null)
                    callback.onApiError(BookmarkManager.this.parseApiError(error.networkResponse));
            }
        });
    }
}
