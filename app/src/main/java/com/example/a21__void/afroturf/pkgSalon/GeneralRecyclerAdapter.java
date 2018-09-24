package com.example.a21__void.afroturf.pkgSalon;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.StylistObject;
import com.example.a21__void.afroturf.pkgCommon.HolderTemplate;

import java.lang.reflect.InvocationTargetException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by ASANDA on 2018/09/22.
 * for Pandaphic
 */
public class GeneralRecyclerAdapter<T> extends RecyclerView.Adapter<HolderTemplate<T>> {
    private Class<? extends HolderTemplate> cls;
    private final int Res;
    private final ArrayList<T>  subServiceObjects;
    private GeneralAdapterListener<T> listener;

    public GeneralRecyclerAdapter(Class<? extends  HolderTemplate> pcls, int res){
        this.cls = pcls;
        this.Res = res;
        subServiceObjects = new ArrayList<>();
    }
    public GeneralRecyclerAdapter(Class<? extends  HolderTemplate> pcls, int res, GeneralAdapterListener<T> pListener){
        this.cls = pcls;
        this.Res = res;
        this.listener = pListener;
        subServiceObjects = new ArrayList<>();
    }

    @NonNull
    @Override
    public HolderTemplate<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup child  = (ViewGroup)LayoutInflater.from(parent.getContext()).inflate(this.Res, parent, false);
        Log.i("sdc", "onCreateViewHolder: " + Res + "|" + cls + "|" + StylistObject.StylistObjectTemplate.class);
        try {
            return cls.getConstructor(ViewGroup.class).newInstance(child);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderTemplate<T> holder, final int position) {
        final T data = this.subServiceObjects.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onItemClick(position, data);
                }
            }
        });
        holder.bind(data, position);
    }

    @Override
    public int getItemCount() {
        return subServiceObjects.size();
    }

    public void add(T... pSubServiceObjects){
        this.subServiceObjects.addAll(Arrays.asList(pSubServiceObjects));
        this.notifyDataSetChanged();
    }

    public void clear() {
        this.subServiceObjects.clear();
        this.notifyDataSetChanged();
    }

    public interface GeneralAdapterListener<T>{
        void onItemClick(int pos, T data);
    }
}
