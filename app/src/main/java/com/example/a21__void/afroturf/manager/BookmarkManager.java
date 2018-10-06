package com.example.a21__void.afroturf.manager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;

/**
 * Created by ASANDA on 2018/09/27.
 * for Pandaphic
 */
public class BookmarkManager extends CacheManager {
    private static final String CACHE_UID_KEY = "cache_uid_key";

    private String cacheUID;
    private String[] ramCache;

    public BookmarkManager(Context context) {
        super(context);
    }

    @Override
    public String getDatabaseName() {
        return null;
    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public void init(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        this.cacheUID = sharedPreferences.getString(CACHE_UID_KEY, null);
        if(this.cacheUID == null){
            this.cacheData();
        }
    }

    @Override
    public void beginManagement() {

    }

    @Override
    public void notifyCacheChanged() {

    }

    @Override
    public void requestRefresh(ManagerRequestListener<CacheManager> callback) {

    }


    public void isBookmarked(final String SUID, final ManagerRequestListener<Boolean> callback){
        this.isBookmarksInSync(new ManagerRequestListener<Boolean>() {
            @Override
            public void onRespond(Boolean isInSync) {
                if(isInSync){
                    boolean isBookmarked = BookmarkManager.this.hasSalon(SUID);
                    if(callback != null)
                        callback.onRespond(isBookmarked);

                }else{
                    BookmarkManager.this.fetchBookmarks(new ManagerRequestListener<String[]>() {
                        @Override
                        public void onRespond(String[] salonSUIDs) {
                            //new bookmarks
                            BookmarkManager.this.clearCache();
                            BookmarkManager.this.cacheData(salonSUIDs);
                            boolean isBookmarked = Arrays.asList(ramCache).contains(SUID);
                            if(callback != null)
                                callback.onRespond(isBookmarked);
                        }
                    });
                }
            }
        });
    }

    void fetchBookmarks(ManagerRequestListener<String[]> callack){

    }
    void isBookmarksInSync(ManagerRequestListener<Boolean> callback){

    }

    private final boolean hasSalon(String suid){
        if(this.ramCache == null){
            //query sLite
            return false;
        }else{
            return Arrays.asList(ramCache).contains(suid);
        }
    }

    private final void cacheData(String[] salonSUIDs){
        this.ramCache = salonSUIDs;
        //sqLite storage part
    }


    @Override
    public void clearCache(){

    }

    @Override
    protected void isDataInSync(ManagerRequestListener<Boolean> callback) {

    }
}
