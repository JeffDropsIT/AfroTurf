package com.example.a21__void.afroturf.pkgCommon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.a21__void.Modules.AfroFragment;
import com.example.a21__void.afroturf.R;

public class GeneralPopupDialogHandler{

    private GeneralPopupDialog generalPopupDialog = new GeneralPopupDialog();

    public  GeneralPopupDialogHandler(){


    }

    public void Show(FragmentManager f, AfroFragment fragment){
        this.generalPopupDialog.show(f, "");
        FragmentTransaction transaction = this.generalPopupDialog.getChildFragmentManager().beginTransaction().replace(R.id.crd_general, fragment);
        this.generalPopupDialog.show(transaction, "");
    }



   public static class GeneralPopupDialog extends DialogFragment {

       @Nullable
       @Override
       public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
           return inflater.inflate(R.layout.general_popup_layout, container, false);
       }
   }

}
