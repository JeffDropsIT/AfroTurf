package com.example.a21__void.afroturf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.a21__void.afroturf.object.AfroObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

/**
 * Created by ASANDA on 2018/09/30.
 * for Pandaphic
 */
public class AfroObjectDatabaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 2;
    public static final String COLUMN_NAME = "name", COLUMN_UID = "uid", COLUMN_JSON = "json_bytes", COLUMN_TIMESTAMP = "time_stamp";
    public final String tableName;
    public final ObjectMapper objectMapper;

    public AfroObjectDatabaseHelper(String databaseName, String pTableName, Context context) {
        super(context, databaseName, null, VERSION);
        this.tableName = pTableName;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + tableName + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_UID + " TEXT NOT NULL, " +
                COLUMN_JSON + " BYTE NOT NULL, " +
                COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        this.onCreate(db);
    }

    public void add(AfroObject... afroObjects){
        SQLiteDatabase database = this.getWritableDatabase();
        database.beginTransaction();

        ContentValues contentValues = new ContentValues();
        for(int pos = 0; pos < afroObjects.length; pos++){
            AfroObject afroObject = afroObjects[pos];
            contentValues.put(COLUMN_NAME, afroObject.getName());
            contentValues.put(COLUMN_UID, afroObject.getUID());
            contentValues.put(COLUMN_JSON, afroObject.get().getBytes());
            database.insert(this.tableName, null, contentValues);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public Cursor getAll(){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(this.tableName, null,null,null, null, null, COLUMN_TIMESTAMP + " DESC");
    }

    public void clear(){
        this.getWritableDatabase().execSQL("DELETE FROM " + this.tableName);
    }
}
