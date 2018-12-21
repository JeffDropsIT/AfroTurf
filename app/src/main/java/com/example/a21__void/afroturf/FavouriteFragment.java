package com.example.a21__void.afroturf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a21__void.BookmarkAdapter;
import com.example.a21__void.Modules.AfroFragment;
import com.example.a21__void.afroturf.manager.BookmarkManager;
import com.example.a21__void.afroturf.manager.CacheManager;
import com.example.a21__void.afroturf.manager.CacheManagerIX;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class FavouriteFragment extends AfroFragment implements CacheManagerIX.CacheListener {
    private Animation animIn, animOut;
    boolean isAnimating = false;
    private BookmarkAdapter bookmarkAdapter;
    private BookmarkManager bookmarkManager;
    private TextView txtEmptyBookmarks;

    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.animIn  = AnimationUtils.loadAnimation(this.getContext(), R.anim.anim_slide_left);
        this.animOut = AnimationUtils.loadAnimation(this.getContext(), R.anim.anim_slide_right);
        this.animIn.setDuration(250);
        this.animOut.setDuration(250);

        this.bookmarkManager = (BookmarkManager)CacheManager.getManager(this.getContext(), BookmarkManager.class);
        this.bookmarkManager.registerCacheListener(this);

        this.bookmarkAdapter = new BookmarkAdapter(this.getContext(), R.layout.bookmark_layout, bookmarkManager.ramCache);

        RelativeLayout parent =  (RelativeLayout) inflater.inflate(R.layout.fragment_favourite, container, false);
        ListView lstBookmarks = parent.findViewById(R.id.lst_bookmarks);
        this.txtEmptyBookmarks = parent.findViewById(R.id.txt_empty);
        this.txtEmptyBookmarks.setVisibility(View.INVISIBLE);

        lstBookmarks.setAdapter(this.bookmarkAdapter);
        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.isAnimating = true;
        this.animIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                FavouriteFragment.this.isAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if(this.getView() != null){
            this.getView().startAnimation(this.animIn);
        }
    }

    @Override
    public void onResume() {
        this.showIndeterminateProgress();
        this.bookmarkManager.requestRefresh(new CacheManager.ManagerRequestListener<CacheManager>() {
            @Override
            public void onRespond(CacheManager result) {
                if(FavouriteFragment.this.bookmarkManager.ramCache.size() == 0){
                    FavouriteFragment.this.txtEmptyBookmarks.setVisibility(View.VISIBLE);
                }
                FavouriteFragment.this.hideIndeterminateProgress();
            }

            @Override
            public void onApiError(CacheManager.ApiError apiError) {
                //TODO ERROR
            }
        });
        super.onResume();
    }

    @Override
    public void requestClose(final AfroFragmentCallback callback) {
        if(this.getView() != null && this.animOut != null){
            this.animOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    FavouriteFragment.this.isAnimating = false;
                    callback.onClose();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            this.getView().startAnimation(this.animOut);
        }else{
            callback.onClose();
        }
    }

    @Override
    public String getTitle() {
        return "Favourites";
    }

    @Override
    public void onCacheChanged() {
        this.bookmarkAdapter.notifyDataSetChanged();
    }
}
