package com.example.a21__void.afroturf;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.a21__void.Modules.AfroFragment;


public class UserSettingFragment extends AfroFragment {

    private boolean animating = false;
    private Animation animIn, animOut;

    public UserSettingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usersetting_list, container, false);

        this.animIn  = AnimationUtils.loadAnimation(this.getContext(), R.anim.anim_slide_left);
        this.animOut = AnimationUtils.loadAnimation(this.getContext(), R.anim.anim_slide_right);
        this.animIn.setDuration(250);
        this.animOut.setDuration(250);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new UserSettingRecyclerViewAdapter(UserPathContent.ITEMS));
        }
        return view;
    }

    @Override
    public void onStart() {
        if(this.getView() != null && this.animIn != null){
            this.animating = true;
            this.animIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animating = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            this.getView().startAnimation(this.animIn);
        }
        super.onStart();
    }

    @Override
    public void requestClose(final AfroFragmentCallback callback) {
        if(this.getView() != null && this.animOut != null){
            this.animating = true;
            this.animOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animating = false;
                    if(callback != null)
                        callback.onClose();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            this.getView().startAnimation(animOut);
        }else{
            if(callback != null)
                callback.onClose();
        }
    }

    @Override
    public String getTitle() {
        return "User Settings";
    }
}
