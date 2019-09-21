package com.example.a21__void.afroturf.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.libIX.fragment.AfroFragment;
import com.example.a21__void.afroturf.manager.LocationManager;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.google.gson.JsonParser;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmallPreviewFragment extends AfroFragment implements View.OnClickListener {
    private static final String BUNDLE_SALON_OBJECT = "bundle_salon_object";
    private SalonAfroObject salonAfroObject = null;
    private boolean isLoading = false;
    private View preview = null, txtNoSalons;
    private RelativeLayout relLoading;
    private InteractionListener interactionListener;

    public SmallPreviewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout relParent = (RelativeLayout)inflater.inflate(R.layout.small_preview, container, false);
        this.preview = relParent.findViewById(R.id.crd_small_preview);
        this.txtNoSalons = relParent.findViewById(R.id.txt_no_salons);
        this.relLoading = relParent.findViewById(R.id.rel_progress);

        this.preview.setOnClickListener(this);

        if(savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_SALON_OBJECT)){
            this.salonAfroObject = new SalonAfroObject();
            this.salonAfroObject.set(new JsonParser(), savedInstanceState.getString(BUNDLE_SALON_OBJECT));
        }

        return relParent;
    }


    @Override
    public void onResume() {
        super.onResume();
        this.refresh();
    }


    public void setClickListener(InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    public void setSalonObject(SalonAfroObject pSalonObject){
        this.salonAfroObject = pSalonObject;
        this.refresh();
    }

    private void updateUI() {
        if(this.preview == null)
            return;

        if(this.salonAfroObject != null){
            TextView txtName = this.preview.findViewById(R.id.txt_name);
            RatingBar ratRating = this.preview.findViewById(R.id.rat_rating);

            txtName.setText(this.salonAfroObject.getName());
            ratRating.setRating(this.salonAfroObject.getRating());
        }
    }

    public void setLoading(boolean pIsLoading) {
        this.isLoading = pIsLoading;
    }

    @Override
    public void retryProcess(int processId) {

    }

    @Override
    public void cancelProcess(int processId) {

    }

    @Override
    public void refresh() {
        if(preview == null)
            return;

        if(!isLoading && LocationManager.getInstance(this.getContext()).getCurrentLocation() == null){
            Log.i("TGISA", "refresh: hide");
            this.getView().setVisibility(View.INVISIBLE);
        }else{
            Log.i("TGISA", "refresh: show");
            this.getView().setVisibility(View.VISIBLE);
        }

        if(this.salonAfroObject == null){
            if(this.isLoading){
                relLoading.setVisibility(View.VISIBLE);
                txtNoSalons.setVisibility(View.INVISIBLE);
                preview.setVisibility(View.VISIBLE);
            }else{
                txtNoSalons.setVisibility(View.VISIBLE);
                preview.setVisibility(View.INVISIBLE);
            }
        }else{
            updateUI();
            txtNoSalons.setVisibility(View.INVISIBLE);
            relLoading.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if(this.interactionListener != null)
            this.interactionListener.onClick(this.salonAfroObject);
    }


    public interface InteractionListener{
        void onClick(SalonAfroObject salon);
    }
}
