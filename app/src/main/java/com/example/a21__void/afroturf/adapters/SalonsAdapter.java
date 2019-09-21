package com.example.a21__void.afroturf.adapters;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a21__void.afroturf.Salon;
import com.example.a21__void.afroturf.database.AfroObjectDatabaseHelper;
import com.example.a21__void.afroturf.object.AfroObject;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.pkgSalon.AfroObjectCursorAdapter;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by ASANDA on 2019/08/09.
 * for Pandaphic
 */
public class SalonsAdapter extends RecyclerView.Adapter<AfroObject.UIHandler>{
    public static final int MODE_CLICK = 0, MODE_SELECT = 1;

    private Constructor<? extends AfroObject.UIHandler> uiConstructor;
    private InteractionListener<SalonAfroObject> interactionListener;
    private ArrayList<SalonAfroObject> salons;

    private AfroObject.UIHandler selectedUI = null;
    private int selectedSalonPosition = -1;

    private int resUIPath;
    private int currentMode = MODE_CLICK;

    public SalonsAdapter(ArrayList<SalonAfroObject> pSalons, Class<? extends AfroObject.UIHandler> pUIClass, int pResUIPath){
        this.salons = pSalons;
        try {
            this.uiConstructor = pUIClass.getConstructor(View.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        this.resUIPath = pResUIPath;
    }

    public void setInteractionListener(InteractionListener<SalonAfroObject> pInteractionListener){
        this.interactionListener = pInteractionListener;
    }
    public void setMode(int pMode){
        this.currentMode = pMode;
    }

    public SalonAfroObject getSelectedSalon(){
        if(this.selectedSalonPosition < 0 || this.selectedSalonPosition > this.salons.size()){
            return null;
        }else{
            return this.salons.get(this.selectedSalonPosition);
        }
    }

    @NonNull
    @Override
    public AfroObject.UIHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View child  = LayoutInflater.from(parent.getContext()).inflate(this.resUIPath, parent, false);
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
    public void onBindViewHolder(@NonNull AfroObject.UIHandler holder, final int position) {
        final SalonAfroObject afroObject = this.salons.get(position);
        holder.bind(afroObject, position);

        if(!holder.itemView.hasOnClickListeners()){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        switch (SalonsAdapter.this.currentMode){
                            case SalonsAdapter.MODE_CLICK:
                                if(SalonsAdapter.this.interactionListener != null)
                                    SalonsAdapter.this.interactionListener.onItemClick(afroObject, position);
                                break;
                            case SalonsAdapter.MODE_SELECT:

                                if(position != SalonsAdapter.this.selectedSalonPosition){

                                    if(SalonsAdapter.this.selectedUI != null)
                                        SalonsAdapter.this.selectedUI.itemView.setSelected(false);

                                    SalonsAdapter.this.selectedUI = holder;
                                    SalonsAdapter.this.selectedUI.itemView.setSelected(true);
                                    SalonsAdapter.this.selectedSalonPosition = position;

                                    if(SalonsAdapter.this.interactionListener != null)
                                        SalonsAdapter.this.interactionListener.onItemSelected(afroObject, position);
                                }
                                break;
                        }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.salons.size();
    }




    public interface InteractionListener<T> extends Serializable {
        void onItemClick(T obj, int position);
        void onItemSelected(T obj, int position);
    }
}
