package com.example.a21__void.Modules;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter{
    private final List<Fragment> fragments = new ArrayList<>();



    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void add(Fragment pFragment){
        fragments.add(pFragment);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
