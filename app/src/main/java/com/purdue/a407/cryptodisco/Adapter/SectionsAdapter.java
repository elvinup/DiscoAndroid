package com.purdue.a407.cryptodisco.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class SectionsAdapter extends FragmentStatePagerAdapter {

    private Fragment[] fragments;
    private String[] titles;

    public SectionsAdapter(FragmentManager fm, Fragment[] fragments,
                           String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public String getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}