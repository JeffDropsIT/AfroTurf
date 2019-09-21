package com.example.a21__void.afroturf.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ASANDA on 2019/09/14.
 * for Pandaphic
 */
public class ConnectionManager {
    public static final int WORKING_MODE_ONLINE = 0
                            , WORKING_MODE_OFFLINE = 1;

    private static final String KEY_WORKING_MODE = "key.working.mode";

    private static ConnectionManager instance;

    private final SharedPreferences sharedPreferences;

    private ConnectionManager(Context context){
        this.sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public int getWorkingMode(){ return this.sharedPreferences.getInt(KEY_WORKING_MODE, WORKING_MODE_ONLINE); }
    public void setWorkingMode(int mode){ this.sharedPreferences.edit().putInt(KEY_WORKING_MODE, mode).apply(); }

    public static ConnectionManager getInstance(Context context){
        return instance != null ? instance : (instance = new ConnectionManager(context));
    }
}
