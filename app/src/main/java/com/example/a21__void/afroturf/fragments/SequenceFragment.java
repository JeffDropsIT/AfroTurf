package com.example.a21__void.afroturf.fragments;

import androidx.fragment.app.Fragment;

import com.example.a21__void.Modules.AfroFragment;
import com.example.a21__void.afroturf.Callback;

/**
 * Created by ASANDA on 2018/12/09.
 * for Pandaphic
 */
public abstract class SequenceFragment extends AfroFragment {
    public abstract void shouldProceed(Callback<Boolean> callback);
}
