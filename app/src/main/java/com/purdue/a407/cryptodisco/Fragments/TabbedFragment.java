package com.purdue.a407.cryptodisco.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.purdue.a407.cryptodisco.Adapter.SectionsAdapter;
import com.purdue.a407.cryptodisco.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class TabbedFragment extends Fragment {

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.container)
    ViewPager viewPager;

    private SectionsAdapter sectionsAdapter;

    public TabbedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        setStuffUp();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setStuffUp();
    }

    public void setStuffUp() {
        sectionsAdapter = new SectionsAdapter(getFragmentManager(), getFragments(),
                getTitles());
        viewPager.setAdapter(sectionsAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.post(() -> tabLayout.setupWithViewPager(viewPager));
    }

    public abstract View getView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState);

    public abstract Fragment[] getFragments();

    public abstract String[] getTitles();

    public abstract void onSelected(int position);
}
