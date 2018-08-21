package com.example.a21__void.Modules;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by ASANDA on 2018/08/21.
 * for Pandaphic
 */
public final class FragmentContainerManager {

    private final int containerId;
    private final FragmentManager fragmentManager;

    public  FragmentContainerManager(int pContainerId, FragmentManager pFragmentManager){
        this.containerId = pContainerId;
        this.fragmentManager = pFragmentManager;
    }


}
