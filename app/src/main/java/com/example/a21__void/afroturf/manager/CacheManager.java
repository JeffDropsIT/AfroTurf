package com.example.a21__void.afroturf.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.a21__void.afroturf.database.AfroObjectDatabaseHelper;
import com.example.a21__void.afroturf.object.AfroObject;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.pkgCommon.APIConstants;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ASANDA on 2018/09/27.
 * for Pandaphic
 */
public abstract class CacheManager {
    public static final String ENTRY_RAW_OBJECT = AfroObjectDatabaseHelper.COLUMN_JSON;

    protected final AfroObjectDatabaseHelper afroObjectDatabaseHelper;
    private static final HashMap<String, CacheManager> instances;
    private Context context;
    static{
        instances = new HashMap<>();

    }

    private CachePointer cachePointer;
    private final HashMap<String, AfroObject> ramCache;

    public CacheManager(Context pContext){
        this.context = pContext;
        this.cachePointer = new CachePointer(null);
        this.afroObjectDatabaseHelper = new AfroObjectDatabaseHelper(getDatabaseName(), getTableName(), context);
        this.ramCache = new HashMap<>();
    }

    public abstract String getDatabaseName();
    public abstract String getTableName();
    public abstract void init(Context context);
    public abstract void beginManagement();
    public abstract void notifyCacheChanged();

    public abstract void requestRefresh(ManagerRequestListener<CacheManager> callback);
    public abstract void clearCache();

    protected abstract void isDataInSync(ManagerRequestListener<Boolean> callback);
    protected void cacheData(AfroObject... afroObjects){
        this.clearCache();
        this.afroObjectDatabaseHelper.add(afroObjects);
        this.notifyCacheChanged();
    }

    protected void cacheObject(AfroObject afroObject){
        this.afroObjectDatabaseHelper.add(afroObject);
        this.ramCache.put(afroObject.getUID(), afroObject);
        this.notifyCacheChanged();
    }

    public static ApiError parseApiError(VolleyError error){
        int httpCode;
        String msg;
        if(error.networkResponse != null){
            httpCode = error.networkResponse.statusCode;
            try {
                msg = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                msg = "Sorry, A Communication Error has occurred with the server. Please try again later";
            }
        }else{
            httpCode = APIConstants.NETWORK_ERROR;
            msg = error.getMessage();
        }
        return new ApiError(httpCode, msg);
    }

    public CachePointer getCachePointer() {
        return cachePointer;
    }
    protected Context getContext(){
        return this.context;
    }

    public final void get(String uid, ManagerRequestListener<AfroObject> callback){
        if(uid != null){
            if(this.ramCache.containsKey(uid))
                callback.onRespond(this.ramCache.get(uid));
            else
                this.remoteGet(uid, callback);
        }else
            callback.onRespond(null);
    }



    protected void remoteGet(String uid, ManagerRequestListener<AfroObject> callback) {
        callback.onRespond(null);
    }

    public int getCount(){ return cachePointer.cursor == null ? 0 : cachePointer.cursor.getCount(); }

    public static CacheManager getManager(Context context, Class<? extends CacheManager> managerClass){
        String name = managerClass.getName();
        if(!instances.containsKey(name)){
            try {
                CacheManager cacheManager = managerClass.getConstructor(Context.class).newInstance(context);
                instances.put(name, cacheManager);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return instances.get(name);
    }


    public interface ManagerRequestListener<T>{
        void onRespond(T result);
        void onApiError(ApiError apiError);
    }

    public static class CachePointer{
        private Cursor cursor;
        private final ArrayList<CursorSwapListener> listeners;

        public CachePointer(Cursor pCursor){
            this.cursor = pCursor;
            this.listeners = new ArrayList<>();
        }

        public void addListener(CursorSwapListener listener){
            this.listeners.add(listener);
        }

        public void removeListener(CursorSwapListener listener){
            this.listeners.remove(listener);
        }

        public void swapCursor(Cursor pCursor){
            this.release();
            this.cursor = pCursor;

            for(int pos = 0; pos < listeners.size(); pos++){
                listeners.get(pos).onSwapCursor(this.cursor);
            }
        }

        public Cursor getCursor() {
            return cursor;
        }

        public void release(){
            if(cursor != null)
                cursor.close();
        }

        public int ItemCount() {
            return this.cursor == null ? 0 : this.cursor.getCount();
        }


        public interface CursorSwapListener{
            void onSwapCursor(Cursor cursor);
        }
    }

    public static class ApiError{
        public final int errorCode;
        public final String message;

        public ApiError(int pErrorCode, String pMessage){
            this.errorCode = pErrorCode;
            this.message = pMessage;
        }
    }
}
