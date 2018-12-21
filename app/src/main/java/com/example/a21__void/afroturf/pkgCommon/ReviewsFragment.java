package com.example.a21__void.afroturf.pkgCommon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.a21__void.Modules.AfroFragment;
import com.example.a21__void.Modules.ServerCon;
import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.ReviewsManager;
import com.example.a21__void.afroturf.object.ReviewAfroObject;
import com.example.a21__void.afroturf.pkgConnection.DevDesignRequest;
import com.example.a21__void.afroturf.pkgSalon.AfroObjectCursorAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewsFragment extends AfroFragment  {
    private ProgressBar progLoading;
    private static final String PARAM_LISTENER = "listener";
    private AfroObjectCursorAdapter reviewsAdapter;
    private AfroObjectCursorAdapter.ItemClickListener reviewClickListener;
    private ReviewsManager reviewsManager;


    public ReviewsFragment() {
        // Required empty public constructor
    }

    public static ReviewsFragment newInstance(AfroObjectCursorAdapter.ItemClickListener listener) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM_LISTENER, listener);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.reviewsManager = (ReviewsManager)CacheManager.getManager(this.getContext(), ReviewsManager.class);
        this.reviewsAdapter = new AfroObjectCursorAdapter(reviewsManager.getCachePointer(), ReviewAfroObject.UIHandler.class, R.layout.reviews_layout);

        if (getArguments() != null) {
            this.reviewClickListener = (AfroObjectCursorAdapter.ItemClickListener)getArguments().getSerializable(PARAM_LISTENER);
            this.reviewsAdapter.setItemClickListener(reviewClickListener);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout parent = (RelativeLayout)inflater.inflate(R.layout.fragment_stylists, container, false);
        RecyclerView recyclerView = parent.findViewById(R.id.rcy_stylist);
        this.progLoading = parent.findViewById(R.id.prog_loading);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(this.reviewsAdapter);
        return parent;
    }

    @Override
    public String getTitle() {
        return "Reviews";
    }

    @Override
    public void onResume() {
        super.onResume();
        CacheManager.ManagerRequestListener<CacheManager> listener = null;
        if(this.reviewsManager.getCount() == 0){
            progLoading.setVisibility(View.VISIBLE);
            listener = new CacheManager.ManagerRequestListener<CacheManager>() {
                @Override
                public void onRespond(CacheManager result) {
                    progLoading.setVisibility(View.INVISIBLE);
                    if(result.getCount() == 0){
                        //no data
                    }

                }

                @Override
                public void onApiError(CacheManager.ApiError apiError) {
                    //todo error
                }
            };
        }

        this.reviewsManager.requestRefresh(listener);
    }
}
