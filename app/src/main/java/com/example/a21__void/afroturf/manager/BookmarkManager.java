package com.example.a21__void.afroturf.manager;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.a21__void.Modules.ServerCon;
import com.example.a21__void.afroturf.object.AfroObject;
import com.example.a21__void.afroturf.object.BookmarkAfroObject;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.pkgCommon.APIConstants;
import com.example.a21__void.afroturf.pkgConnection.DevDesignRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

    public ArrayList<BookmarkAfroObject> getBookmarks(){ return this.getBookmarks(new State(0, this.getCachePointer().ItemCount()), false); }
    public ArrayList<BookmarkAfroObject> getBookmarks(State state, boolean updateState){
        if(this.getCachePointer().ItemCount() == 0 || this.getCachePointer().getCursor() == null)
            return new ArrayList<>();

        int index = state.getIndex();
        int count = state.getCount();

        if(index < 0)
            index = 0;
        else if(index >= this.getCachePointer().ItemCount())
            index = this.getCachePointer().ItemCount() - 1;

        if(count < 0)
            count = 0;
        else if(index + count >= this.getCachePointer().ItemCount())
            count = this.getCachePointer().ItemCount() - index;

        Cursor cursor = this.getCachePointer().getCursor();
        cursor.moveToPosition(index);

        int counter = count;
        ArrayList<BookmarkAfroObject> bookmarks = new ArrayList<>();
        JsonParser jsonParser = new JsonParser();

        do{
            byte[] rawJsonString = cursor.getBlob(cursor.getColumnIndex(CacheManagerIX.ENTRY_RAW_OBJECT));

            BookmarkAfroObject bookmark = new BookmarkAfroObject();
            bookmark.set(jsonParser, new String(rawJsonString));

            bookmarks.add(bookmark);
            counter--;
        }while (cursor.moveToNext() && counter > 0);

        if(updateState)
            state.setIndex(index + count);

        return bookmarks;
    }
    public ArrayList<BookmarkAfroObject> getBookmarks(State state, boolean updateState, ManagerRequestListener<ArrayList<BookmarkAfroObject>> callback){

        this.requestRefresh(new ManagerRequestListener<CacheManager>() {
            @Override
            public void onRespond(CacheManager result) {
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(callback != null)
                                    callback.onRespond(BookmarkManager.this.getBookmarks());
                            }
                        }, 2000);

            }

            @Override
            public void onApiError(ApiError apiError) {
                if(callback != null)
                    callback.onApiError(apiError);
            }
        });

        return this.getBookmarks(state, updateState);
    }
    public ArrayList<BookmarkAfroObject> getBookmarks(ManagerRequestListener<ArrayList<BookmarkAfroObject>> callback){
        return this.getBookmarks( new State(0, this.getCachePointer().ItemCount()), false, callback);
    }


    public void isBookmarked(final String uid, final ManagerRequestListener<String> callback){
        this.requestRefresh(new ManagerRequestListener<CacheManager>() {
            @Override
            public void onRespond(CacheManager result) {
                int bookmarksSize = BookmarkManager.this.ramCache.size();
                String bookmarkId = null;
                for(int pos = 0; pos < bookmarksSize; pos++){
                    BookmarkAfroObject bookmark = BookmarkManager.this.ramCache.get(pos);
                    String markObjectUID = bookmark.getMarkedObjectUID();
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
        /*this.fetchBookmarks(new ManagerRequestListener<ArrayList<BookmarkAfroObject>>() {
            @Override
            public void onRespond(ArrayList<BookmarkAfroObject> result) {
                BookmarkManager.this.cacheData(result.toArray(new BookmarkAfroObject[result.size()]));
            }

            @Override
            public void onApiError(ApiError apiError) {

            }
        });*/
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
        final String uuid = UserManager.getInstance(this.getContext()).getCurrentUser().getUUID();
        this.isDataInSync(new ManagerRequestListener<Boolean>() {
            @Override
            public void onRespond(Boolean inSync) {
                if(!inSync){
                    BookmarkManager.this.fetchBookmarks(uuid, new ManagerRequestListener<ArrayList<BookmarkAfroObject>>() {
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

    void fetchBookmarks(String uuid, final ManagerRequestListener<ArrayList<BookmarkAfroObject>> callback){
        String url = "http://ec2-52-15-103-215.us-east-2.compute.amazonaws.com/afroturf/user/profile/salon/bookmarks?userId=" + uuid;

        this.serverCon.HTTP(Request.Method.GET, url, 0, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                JsonParser parser = new JsonParser();
                JsonArray bookmarks = parser.parse(response.data).getAsJsonArray();
                BookmarkManager.this.clearCache();

                BookmarkAfroObject[] result = new BookmarkAfroObject[bookmarks.size()];
                for(int pos = 0; pos < bookmarks.size(); pos++){
                    JsonObject bookmark = bookmarks.get(pos).getAsJsonObject();

                    BookmarkAfroObject bookmarkAfroObject = new BookmarkAfroObject();
                    bookmarkAfroObject.set(parser, bookmark.toString());
                    result[pos] = bookmarkAfroObject;

                }
                BookmarkManager.this.cacheData(result);


                if(callback != null)
                    callback.onRespond(BookmarkManager.this.getBookmarks());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(callback != null)
                    callback.onApiError(CacheManager.parseApiError(error));
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

    public void addBookmark(final SalonAfroObject salon, String userId, final ManagerRequestListener<BookmarkAfroObject> callback) {
        String url = "http://ec2-52-15-103-215.us-east-2.compute.amazonaws.com/afroturf/user/profile/salon/bookmarks";

        JsonObject reqBody = new JsonObject();
        reqBody.addProperty(APIConstants.FIELD_USER_ID, userId);
        reqBody.addProperty(APIConstants.FIELD_SALON_ID, salon.getUID());
        reqBody.addProperty(APIConstants.FIELD_INFO, salon.getLocation().toString());
        reqBody.addProperty(APIConstants.FIELD_TITLE, salon.getName());

        this.serverCon.HTTP(Request.Method.POST, url, 0, new Response.Listener<DevDesignRequest.DevDesignResponse>() {
            @Override
            public void onResponse(DevDesignRequest.DevDesignResponse response) {
                JsonParser parser = new JsonParser();
                Log.i("TGIJ", "onResponse: " + response.data);
                JsonObject data = parser.parse(response.data).getAsJsonObject();
                JsonObject bookmarkJson = data.get("data").getAsJsonObject();
                bookmarkJson.add("markedObject", salon.asJson());

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
                    callback.onApiError(BookmarkManager.this.parseApiError(error));
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
                    callback.onApiError(BookmarkManager.this.parseApiError(error));
            }
        });
    }
}
