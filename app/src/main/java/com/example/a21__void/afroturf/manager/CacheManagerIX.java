package com.example.a21__void.afroturf.manager;

import android.content.Context;

import com.example.a21__void.afroturf.object.AfroObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASANDA on 2018/12/01.
 * for Pandaphic
 */
public abstract class CacheManagerIX<T extends AfroObject> extends CacheManager{
    private final ArrayList<CacheListener> cacheListeners;
    public final ArrayList<T> ramCache;

    public CacheManagerIX(Context context) {
        super(context);
        this.ramCache = new ArrayList<>();
        this.cacheListeners = new ArrayList<>();
    }

    protected void add(T afroObject){
        this.ramCache.add(afroObject);
    }

    public final void release(){
        this.ramCache.clear();
    }
    public final void registerCacheListener(CacheListener cacheListener){
        this.cacheListeners.add(cacheListener);
    }
    public final void deregisterCacheListener(CacheListener cacheListener){
        this.cacheListeners.remove(cacheListener);
    }

    public int count(){ return this.ramCache.size(); }
    public T get(int pos){ return this.ramCache.get(pos); }
    @Override
    public void notifyCacheChanged() {
        for(int pos = 0; pos < this.cacheListeners.size(); pos++)
            if(cacheListeners.get(pos) != null)
                cacheListeners.get(pos).onCacheChanged();
    }

    @Override
    public void clearCache() {
        this.release();
    }

    public interface CacheListener{
        void onCacheChanged();
    }
}
