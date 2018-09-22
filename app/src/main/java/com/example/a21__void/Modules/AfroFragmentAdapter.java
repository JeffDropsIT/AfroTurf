package com.example.a21__void.Modules;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by ASANDA on 2018/09/22.
 * for Pandaphic
 */
public class AfroFragmentAdapter extends FragmentPagerAdapter {
    private final List<AfroFragment> afroFragments;

    public AfroFragmentAdapter(FragmentManager fm) {
        super(fm);
        this.afroFragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return afroFragments.get(position);
    }

    @Override
    public int getCount() {
        return afroFragments.size();
    }

    public void add(AfroFragment... fragments){
        this.afroFragments.addAll(Arrays.asList(fragments));
        this.notifyDataSetChanged();
    }

    public AfroFragment get(int pos){
        return this.afroFragments.get(pos);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return get(position).getTitle();
    }
}
