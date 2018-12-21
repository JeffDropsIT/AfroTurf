package com.example.a21__void.afroturf.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.SalonsManager;
import com.example.a21__void.afroturf.object.AfroObject;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.pkgSalon.AfroObjectCursorAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SalonPickerFragment extends Fragment {

    public static final int MODE_SELECT = 0, MODE_CLICK = 1;
    private static final String PARAMS_LISTENER = "listener";
    private ProgressBar progLoading;
    private AfroObjectCursorAdapter salonsCursorAdapter;
    private AfroObjectCursorAdapter.ItemClickListener salonClickListener;

    private int mode = MODE_SELECT;

    private SalonAfroObject selectedSalon = null;


    public SalonPickerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CacheManager.CachePointer cachePointer = CacheManager.getManager(this.getContext(), SalonsManager.class).getCachePointer();
        this.salonsCursorAdapter = new AfroObjectCursorAdapter(cachePointer, SalonAfroObject.UIHandler.class, R.layout.salon_layout);
        if (getArguments() != null) {
            this.salonClickListener = (AfroObjectCursorAdapter.ItemClickListener)getArguments().getSerializable(PARAMS_LISTENER);
            this.salonsCursorAdapter.setItemClickListener(new AfroObjectCursorAdapter.ItemClickListener() {
                @Override
                public void onItemClick(AfroObject afroObject, int position) {
                    if(mode == MODE_SELECT);
                        //SalonsFragment.this.selectedSalon = (SalonAfroObject)afroObject;
                    else
                        salonClickListener.onItemClick(afroObject, position);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout parent = (RelativeLayout)inflater.inflate(R.layout.fragment_stylists, container, false);
        RecyclerView recyclerView = parent.findViewById(R.id.rcy_stylist);
        this.progLoading = parent.findViewById(R.id.prog_loading);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(this.salonsCursorAdapter);

        return parent;
    }

}
