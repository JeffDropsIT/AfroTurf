package com.example.a21__void.Modules;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
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
