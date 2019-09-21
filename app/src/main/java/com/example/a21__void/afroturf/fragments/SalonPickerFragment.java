package com.example.a21__void.afroturf.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.Salon;
import com.example.a21__void.afroturf.adapters.SalonsAdapter;
import com.example.a21__void.afroturf.libIX.fragment.AfroFragment;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.SalonsManager;
import com.example.a21__void.afroturf.manager.UserManager;
import com.example.a21__void.afroturf.object.AfroObject;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.pkgSalon.AfroObjectCursorAdapter;
import com.example.a21__void.afroturf.user.UserGeneral;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SalonPickerFragment extends AfroFragment implements View.OnClickListener {

    public static final int MODE_SELECT = 0, MODE_CLICK = 1;
    private static final String PARAMS_LISTENER = "listener";
    private ProgressBar progLoading;
    private SalonsAdapter salonsAdapter;
    private InteractionListener interactionListener;

    private int mode = MODE_SELECT;

    private SalonAfroObject selectedSalon = null;


    public SalonPickerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheManager.CachePointer cachePointer = CacheManager.getManager(this.getContext(), SalonsManager.class).getCachePointer();
        SalonsManager salonsManager = SalonsManager.getInstance(this.getContext());
        salonsManager.beginManagement();

        this.salonsAdapter = new SalonsAdapter(salonsManager.getHiringSalons(), SalonAfroObject.UIHandler.class, R.layout.salon_item_small_layout);
        this.salonsAdapter.setMode(SalonsAdapter.MODE_SELECT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout parent = (RelativeLayout)inflater.inflate(R.layout.fragment_signup_with_salon, container, false);
        RecyclerView recyclerView = parent.findViewById(R.id.rcy_salons);
        this.progLoading = parent.findViewById(R.id.prog_background_work);
        parent.findViewById(R.id.txt_apply).setOnClickListener(this);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(this.salonsAdapter);

        return parent;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.txt_apply:
                this.apply();
                break;
        }
    }

    private void apply() {
        if(this.salonsAdapter.getSelectedSalon() != null){

            UserManager userManager = UserManager.getInstance(this.getContext());
            UserGeneral userGeneral = userManager.getCurrentUser();

            SalonAfroObject salon = this.salonsAdapter.getSelectedSalon();
            SalonsManager salonsManager = SalonsManager.getInstance(this.getContext());

            this.showProcess("Sending Request.");
            salonsManager.registerStylist(salon.getSalonObjId(), userGeneral.getUUID(), new CacheManager.ManagerRequestListener<String>() {
                @Override
                public void onRespond(String result) {
                    SalonPickerFragment.this.hideProcess();
                    if(SalonPickerFragment.this.interactionListener == null)
                        SalonPickerFragment.this.interactionListener.onSignUp(userGeneral, salon);
                }

                @Override
                public void onApiError(CacheManager.ApiError apiError) {
                    SalonPickerFragment.this.hideProcess();
                    Log.i("register", "onApiError: " + apiError.message);
                }
            });
        }else{
            Toast.makeText(this.getContext(), "Please Select A Salon.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void retryProcess(int processId) {

    }

    @Override
    public void cancelProcess(int processId) {

    }

    @Override
    public void refresh() {

    }

    public interface InteractionListener{
        void onSignUp(UserGeneral user, SalonAfroObject salon);
    }
}
