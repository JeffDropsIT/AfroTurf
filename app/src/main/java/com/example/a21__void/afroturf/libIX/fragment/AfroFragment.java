package com.example.a21__void.afroturf.libIX.fragment;

import android.content.Context;
import androidx.fragment.app.Fragment;

/**
 * Created by ASANDA on 2019/07/12.
 * for Pandaphic
 */
public abstract class AfroFragment extends Fragment {

    private InteractionListener interactionListener;

    protected void showProcessError(int processID, int resIcon, String title, String message){
        if(this.interactionListener != null)
            this.interactionListener.onProcessError(this, processID, resIcon, title, message);
    }
    protected void notifyBackgroundWorkStarted(){
        if(this.interactionListener != null)
            this.interactionListener.onDoBackgroundWork();
    }
    protected void notifyBackgroundWorkFinished(){
        if(this.interactionListener != null)
            this.interactionListener.onFinishBackgroundWork();
    }
    protected void showProcess(String message){ if(this.interactionListener != null) this.interactionListener.showProgress(message); }
    protected void hideProcess(){ if(this.interactionListener != null) this.interactionListener.hideProgress(); }
    protected void addToOnConnectionQueue(int pid){ if(this.interactionListener != null) this.interactionListener.addToOnConnectionQueue(this, pid);}
    protected void removeAtOnConnectionQueue(){ if(this.interactionListener != null) this.interactionListener.removeAtOnConnectionQueue(this);}

    public abstract void retryProcess(int processId);
    public abstract void cancelProcess(int processId);



    public abstract void refresh();

    public void setInteractionListener(InteractionListener pInteractionListener) {
        this.interactionListener = pInteractionListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof InteractionListener){
            this.interactionListener = (InteractionListener)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.interactionListener = null;
    }

    public interface InteractionListener{
        void onProcessError(AfroFragment parent, int processId, int resIcon, String title, String message);
        void onDoBackgroundWork();
        void onFinishBackgroundWork();
        void showProgress(String message);
        void hideProgress();
        void addToOnConnectionQueue(AfroFragment fragment, int pid);
        void removeAtOnConnectionQueue(AfroFragment fragment);
    }
}
