package com.example.a21__void.afroturf.fragments;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.a21__void.Modules.AfroFragment;
import com.example.a21__void.afroturf.manager.SalonsManager;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.pkgSalon.AfroObjectCursorAdapter;


public class SalonsFragment extends AfroFragment {
    private static final String PARAMS_LISTENER = "listener";
    private ProgressBar progLoading;
    private AfroObjectCursorAdapter salonsCursorAdapter;
    private AfroObjectCursorAdapter.ItemClickListener salonClickListener;

    public SalonsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.salonClickListener = (AfroObjectCursorAdapter.ItemClickListener)getArguments().getSerializable(PARAMS_LISTENER);
        }
        CacheManager.CachePointer cachePointer = CacheManager.getManager(this.getContext(), SalonsManager.class).getCachePointer();
        this.salonsCursorAdapter = new AfroObjectCursorAdapter(cachePointer, SalonAfroObject.UIHandler.class, R.layout.salon_layout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout parent = (RelativeLayout)inflater.inflate(R.layout.fragment_stylists, container, false);
        RecyclerView recyclerView = parent.findViewById(R.id.rcy_stylist);
        this.progLoading = parent.findViewById(R.id.prog_loading);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(this.salonsCursorAdapter);

        return parent;
    }

    @Override
    public void onResume() {
        super.onResume();
        SalonsManager salonsManager = (SalonsManager)CacheManager.getManager(this.getContext(), SalonsManager.class);
        CacheManager.CachePointer cachePointer = salonsManager.getCachePointer();
        CacheManager.ManagerRequestListener<CacheManager> refreshFinishListener = null;
        if(cachePointer.getCursor().getCount() == 0){
            this.progLoading.setVisibility(View.VISIBLE);
            refreshFinishListener = new CacheManager.ManagerRequestListener<CacheManager>() {
                @Override
                public void onRespond(CacheManager result) {
                    if(result.getCachePointer().getCursor().getCount() == 0){
                        //show no salons found
                    }
                    SalonsFragment.this.progLoading.setVisibility(View.INVISIBLE);
                }
            };
        }
        SalonsManager.getInstance(this.getContext()).requestRefresh(refreshFinishListener);
    }

    @Override
    public String getTitle() {
        return "Salons";
    }

    public static SalonsFragment newInstance(AfroObjectCursorAdapter.ItemClickListener  salonClickListener) {
        SalonsFragment fragment = new SalonsFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAMS_LISTENER, salonClickListener);
        fragment.setArguments(args);
        return fragment;
    }
}
