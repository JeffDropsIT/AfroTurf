package com.example.a21__void.afroturf;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.a21__void.Modules.AfroFragment;
import com.example.a21__void.Modules.ListPagesAdapter;
import com.example.a21__void.Modules.PageTransformer;
import com.example.a21__void.Modules.SalonsFragementAdapter;
import com.example.a21__void.afroturf.fragments.SmallPreviewFragment;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.SalonsManager;
import com.example.a21__void.Modules.SalonsPreviewFragment;
import com.example.a21__void.afroturf.object.SalonAfroObject;
import com.example.a21__void.afroturf.pkgSalon.SalonObject;

import java.util.ArrayList;


public class PagesFragment extends AfroFragment {
    private SalonsFragementAdapter pagesAdapter;
    private ViewPager vpgPages;
    private Animation animSlideIn, animSlideOut;
    private boolean animating = false;
    private SalonsFragementAdapter.OnItemClickListener onItemClickListener;

    public PagesFragment() {
        // Required empty public constructor
    }

    public static PagesFragment newInstance(String param1, String param2) {
        PagesFragment fragment = new PagesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.pagesAdapter = new SalonsFragementAdapter(this.getContext(), SalonsManager.getInstance(this.getContext()).getCachePointer());
        this.pagesAdapter.setClickListener(this.onItemClickListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout parent  = (RelativeLayout)inflater.inflate(R.layout.fragment_pages, container, false);
        this.vpgPages = parent.findViewById(R.id.vpg_pages);

        PageTransformer fragmentCardShadowTransformer = new PageTransformer(vpgPages, pagesAdapter);
        fragmentCardShadowTransformer.enableScaling(true);

        this.vpgPages.setPageTransformer(false, fragmentCardShadowTransformer);
        this.vpgPages.setOffscreenPageLimit(3);
        this.vpgPages.setAdapter(pagesAdapter);

        this.animSlideIn = AnimationUtils.loadAnimation(this.getContext(), R.anim.anim_slide_up);
        this.animSlideOut = AnimationUtils.loadAnimation(this.getContext(), R.anim.anim_slide_down);
        this.animSlideIn.setDuration(250);
        this.animSlideOut.setDuration(250);
        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(this.getView() != null && this.animSlideIn != null){
            this.animating = true;
            this.animSlideIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    PagesFragment.this.animating = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            vpgPages.startAnimation(this.animSlideIn);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.showIndeterminateProgress();
        SalonsManager.getInstance(this.getContext()).requestRefresh(new CacheManager.ManagerRequestListener<CacheManager>() {
            @Override
            public void onRespond(CacheManager result) {
                PagesFragment.this.hideIndeterminateProgress();
            }

            @Override
            public void onApiError(CacheManager.ApiError apiError) {
                //todo error
            }
        });
    }

    @Override
    public void requestClose(final AfroFragmentCallback callback) {
        if(this.getView() != null && this.animSlideOut != null){
            animating = true;
            animSlideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    PagesFragment.this.animating = false;
                    callback.onClose();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            this.vpgPages.startAnimation(this.animSlideOut);
        }else{
            callback.onClose();
        }
    }

    public void setOnItemClickListener(SalonsFragementAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        if(this.pagesAdapter != null)
            this.pagesAdapter.setClickListener(this.onItemClickListener);
    }

    @Override
    public String getTitle() {
        return "Pages";
    }

}
