package com.example.a21__void.afroturf.pkgSalon;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a21__void.afroturf.database.AfroObjectDatabaseHelper;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.object.AfroObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import okio.ByteString;

/**
 * Created by ASANDA on 2018/09/22.
 * for Pandaphic
 */
public class AfroObjectCursorAdapter extends RecyclerView.Adapter<AfroObject.UIHandler> implements CacheManager.CachePointer.CursorSwapListener {
    private Constructor<? extends AfroObject.UIHandler> uiConstructor;
    private final Class<? extends AfroObject.UIHandler> uiClass;
    private final int resPath;
    private final ObjectMapper objectMapper;
    private final CacheManager.CachePointer cachePointer;
    private ItemClickListener itemClickListener;
    private AfroObject.UIHandler selectedUI = null;

    public AfroObjectCursorAdapter(CacheManager.CachePointer pCachePointer, Class<? extends AfroObject.UIHandler> pUIClass, int pResPath){
        try {
            this.uiConstructor = pUIClass.getConstructor(View.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        this.cachePointer = pCachePointer;
        this.uiClass = pUIClass;
        this.resPath = pResPath;
        this.objectMapper = new ObjectMapper();

        this.cachePointer.addListener(this);
    }

    @NonNull
    @Override
    public AfroObject.UIHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View child  = LayoutInflater.from(parent.getContext()).inflate(this.resPath, parent, false);
        try {
            return uiConstructor.newInstance(child);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final AfroObject.UIHandler holder, final int position) {
        Cursor cursor = this.cachePointer.getCursor();
        if(!cursor.moveToPosition(position))
            return;

        byte[] rawJson = cursor.getBlob(cursor.getColumnIndex(AfroObjectDatabaseHelper.COLUMN_JSON));

        final AfroObject afroObject;
        try {
            afroObject = holder.getObjectClass().getConstructor().newInstance();
            afroObject.set(new JsonParser(), new String(rawJson));

            holder.bind(afroObject, position);

            if(!holder.itemView.hasOnClickListeners()){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedUI != null)
                            selectedUI.itemView.setSelected(false);

                        selectedUI = holder;
                        selectedUI.itemView.setSelected(true);
                        if(itemClickListener != null)
                            itemClickListener.onItemClick(afroObject, position);
                    }
                });
            }

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

    @Override
    public int getItemCount() {
        return this.cachePointer.getCursor().getCount();
    }

    @Override
    public void onSwapCursor(Cursor cursor) {
        this.notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener pItemClickListener){
        this.itemClickListener = pItemClickListener;
    }


    public interface ItemClickListener extends Serializable{
        void onItemClick(AfroObject afroObject, int position);
    }
}
