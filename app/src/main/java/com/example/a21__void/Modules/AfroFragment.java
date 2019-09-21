package com.example.a21__void.Modules;


import android.app.Activity;
import androidx.fragment.app.Fragment;

import com.example.a21__void.afroturf.AfroActivity;

/**
 * Created by ASANDA on 2018/08/21.
 * for Pandaphic
 */
public abstract class AfroFragment extends Fragment {
    private AfroFragmentCallback afroFragmentCallback;


    public void requestClose(AfroFragmentCallback callback){
        callback.onClose();
    }

    public final void showIndeterminateProgress(){
        Activity parentActivity = this.getActivity();
        if(parentActivity != null)
            if(parentActivity instanceof AfroActivity)
                ((AfroActivity)parentActivity).showIndeterminateProgress();
    }

    public final void hideIndeterminateProgress(){
        Activity parentActivity = this.getActivity();
        if(parentActivity != null)
            if(parentActivity instanceof AfroActivity)
                ((AfroActivity)parentActivity).hideIndeterminateProgress();
    }


    @Override
    public void onResume() {
        super.onResume();
        if(this.afroFragmentCallback != null){
            this.afroFragmentCallback.onShow();
        }
    }

    @Override
    public void onPause() {
        if(this.afroFragmentCallback != null){
            this.afroFragmentCallback.onClose();
        }
        super.onPause();
    }

    public void setAfroFragmentCallback(AfroFragmentCallback pAfroFragmentCallback){
        this.afroFragmentCallback = pAfroFragmentCallback;
    }

    public abstract String getTitle();

    public AfroFragmentCallback getAfroFragmentCallback(){ return this.afroFragmentCallback; }

    public interface AfroFragmentCallback{
        void onShow();
        void onClose();
    }
}
