package com.example.a21__void.afroturf.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.a21__void.Modules.AfroFragment;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.StylistsManager;
import com.example.a21__void.afroturf.object.StylistAfroObject;
import com.example.a21__void.afroturf.pkgSalon.AfroObjectCursorAdapter;


public class StylistsFragment extends AfroFragment {

    private ProgressBar progLoading;
    private static final String PARAM_LISTENER = "listener";
    private StylistsManager stylistsManager;
    private AfroObjectCursorAdapter stylistAdapter;
    private AfroObjectCursorAdapter.ItemClickListener  stylistClickListener;

    public StylistsFragment() {
        // Required empty public constructor
    }

    public static StylistsFragment newInstance(AfroObjectCursorAdapter.ItemClickListener listener) {
        StylistsFragment fragment = new StylistsFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM_LISTENER, listener);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.stylistClickListener = (AfroObjectCursorAdapter.ItemClickListener)getArguments().getSerializable(PARAM_LISTENER);
        }
        this.stylistsManager = (StylistsManager)CacheManager.getManager(this.getContext(), StylistsManager.class);
        CacheManager.CachePointer cachePointer = this.stylistsManager.getCachePointer();
        this.stylistAdapter = new AfroObjectCursorAdapter(cachePointer, StylistAfroObject.UIHandler.class, R.layout.stylist_layout_two);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout parent = (RelativeLayout)inflater.inflate(R.layout.fragment_stylists, container, false);
        RecyclerView recyclerView = parent.findViewById(R.id.rcy_stylist);
        this.progLoading = parent.findViewById(R.id.prog_loading);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(this.stylistAdapter);

        return parent;
    }

    @Override
    public String getTitle() {
        return "Stylists";
    }

    @Override
    public void onResume() {
        super.onResume();
        CacheManager.ManagerRequestListener<CacheManager> listener = null;
        if(this.stylistsManager.getCount() == 0){
            this.progLoading.setVisibility(View.VISIBLE);
            listener = new CacheManager.ManagerRequestListener<CacheManager>() {
                @Override
                public void onRespond(CacheManager manager) {
                    progLoading.setVisibility(View.INVISIBLE);
                    if(manager.getCount() == 0){
                        //no data
                    }
                }
            };
        }
        this.stylistsManager.requestRefresh(listener);
    }

}
