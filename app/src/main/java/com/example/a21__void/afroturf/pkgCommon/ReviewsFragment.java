package com.example.a21__void.afroturf.pkgCommon;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.a21__void.afroturf.R;
import com.example.a21__void.afroturf.libIX.fragment.AfroFragment;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.ReviewsManager;
import com.example.a21__void.afroturf.object.ReviewAfroObject;
import com.example.a21__void.afroturf.pkgSalon.AfroObjectCursorAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewsFragment extends AfroFragment {
    private static final String PARAM_LISTENER = "listener";
    private static final String PARAMS_OUID = "params.ouid";
    private AfroObjectCursorAdapter reviewsAdapter;
    private AfroObjectCursorAdapter.ItemClickListener reviewClickListener;
    private ReviewsManager reviewsManager;
    private String ouid;


    public ReviewsFragment() {
        // Required empty public constructor
    }

    public static ReviewsFragment newInstance(String ouid) {
        ReviewsFragment reviewsFragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putString(PARAMS_OUID, ouid);
        reviewsFragment.setArguments(args);
        return reviewsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.ouid = this.getArguments().getString(PARAMS_OUID);
        }

        this.reviewsManager = (ReviewsManager)CacheManager.getManager(this.getContext(), ReviewsManager.class);
        //this.reviewsAdapter = new AfroObjectCursorAdapter(reviewsManager.getReviews(this.ouid), ReviewAfroObject.UIHandler.class, R.layout.reviews_layout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout parent = (RelativeLayout)inflater.inflate(R.layout.fragment_reviews_layout, container, false);
        RecyclerView recyclerView = parent.findViewById(R.id.rcv_reviews);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(this.reviewsAdapter);
        return parent;
    }



    @Override
    public void onResume() {
        super.onResume();
        CacheManager.ManagerRequestListener<CacheManager> listener = null;
        if(this.reviewsManager.getCount() == 0){
            //progLoading.setVisibility(View.VISIBLE);
            listener = new CacheManager.ManagerRequestListener<CacheManager>() {
                @Override
                public void onRespond(CacheManager result) {
                   // progLoading.setVisibility(View.INVISIBLE);
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

    @Override
    public void retryProcess(int processId) {

    }

    @Override
    public void cancelProcess(int processId) {

    }

    @Override
    public void refresh() {

    }
}
