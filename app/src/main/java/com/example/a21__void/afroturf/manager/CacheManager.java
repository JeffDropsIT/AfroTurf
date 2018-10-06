package com.example.a21__void.afroturf.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.example.a21__void.afroturf.database.AfroObjectDatabaseHelper;
import com.example.a21__void.afroturf.object.AfroObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ASANDA on 2018/09/27.
 * for Pandaphic
 */
public abstract class CacheManager {
    protected final AfroObjectDatabaseHelper afroObjectDatabaseHelper;
    private static final HashMap<String, CacheManager> instances;
    static{
        instances = new HashMap<>();
    }

    private CachePointer cachePointer;

    public CacheManager(Context context){
        this.cachePointer = new CachePointer(null);
        this.afroObjectDatabaseHelper = new AfroObjectDatabaseHelper(getDatabaseName(), getTableName(), context);
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

    public CachePointer getCachePointer() {
        return cachePointer;
    }

    public int getCount(){ return cachePointer.cursor.getCount();}

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


        public interface CursorSwapListener{
            void onSwapCursor(Cursor cursor);
        }
    }
}
