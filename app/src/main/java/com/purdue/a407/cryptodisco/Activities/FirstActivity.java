package com.purdue.a407.cryptodisco.Activities;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.purdue.a407.cryptodisco.R;

public class FirstActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    String[] headersArr = new String[]{"Welcome to CryptoDisco!","Search",
            "Advanced Trading Features", "Social Media!", "Click below to get started!"};
    String[] descriptionsArr = new String[]{"CryptoDisco is a trading application that allows access " +
            "to the most popular exchanges in crypto. This app allows you to create Limit/Stop/Market orders," +
            " along with watching trends of coins, incorporating social media, etc.",
            "To begin looking for new crypto " +
            "currency you can search for coins across our supported exchanges " +
            "or browse an exchange.","Want to place stop and trailing stops even " +
            "on exchanges that don’t support them by default? Go to the Exchanges tab on " +
            "the home screen and insert your API Keys for the necessary exchanges.", "See what's going " +
            "on around the world of cryptocurrency, along with the chat rooms associated with different " +
            "exchanges! Go to the communication section of the navigation list!",""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), headersArr,
                descriptionsArr);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        FloatingActionButton fab;
        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String header, String description, boolean isLast) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString("header", header);
            args.putString("desc", description);
            args.putBoolean("isLast", isLast);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_first, container, false);
            String head = getArguments().getString("header");
            String descript = getArguments().getString("desc");
            boolean isLast = getArguments().getBoolean("isLast");
            LinearLayout layout = rootView.findViewById(R.id.linLayout);
            fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
            if(isLast) {
                fab.setVisibility(View.VISIBLE);
                fab = rootView.findViewById(R.id.fab);
                fab.setOnClickListener(view -> getActivity().finish());
                layout.setVisibility(View.INVISIBLE);
            }
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            TextView textView1 = (TextView) rootView.findViewById(R.id.description);
            textView.setText(head);
            textView1.setText(descript);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        String[] headers;
        String[] descriptions;

        public SectionsPagerAdapter(FragmentManager fm, String[] headers, String[] descriptions) {
            super(fm);
            this.headers = headers;
            this.descriptions = descriptions;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(headers[position], descriptions[position],
                    position == headers.length - 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return headers.length;
        }
    }
}
