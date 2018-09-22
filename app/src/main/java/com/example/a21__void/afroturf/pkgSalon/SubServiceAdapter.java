package com.example.a21__void.afroturf.pkgSalon;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.a21__void.afroturf.R;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by ASANDA on 2018/09/22.
 * for Pandaphic
 */
public class SubServiceAdapter extends RecyclerView.Adapter<SubServiceObject.SubServiceTemplate> {
    private final ArrayList<SubServiceObject>  subServiceObjects;

    public SubServiceAdapter(){
        subServiceObjects = new ArrayList<>();
    }

    @NonNull
    @Override
    public SubServiceObject.SubServiceTemplate onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup child  = (ViewGroup)LayoutInflater.from(parent.getContext()).inflate(R.layout.service_layout, parent, false);
        return new SubServiceObject.SubServiceTemplate(child);
    }

    @Override
    public void onBindViewHolder(@NonNull SubServiceObject.SubServiceTemplate holder, int position) {
        SubServiceObject subServiceObject = this.subServiceObjects.get(position);
        holder.txtName.setText(subServiceObject.Name);
        holder.txtCat.setText(subServiceObject.Category);
        holder.txtPrice.setText("R" + subServiceObject.Price);
    }

    @Override
    public int getItemCount() {
        return subServiceObjects.size();
    }

    public void add(SubServiceObject... pSubServiceObjects){
        this.subServiceObjects.addAll(Arrays.asList(pSubServiceObjects));
        this.notifyDataSetChanged();
    }
}
