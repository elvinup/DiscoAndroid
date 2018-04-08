package com.purdue.a407.cryptodisco.Activities;

import android.arch.lifecycle.ViewModelProvider;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.purdue.a407.cryptodisco.App;
import com.purdue.a407.cryptodisco.Fragments.ExchangeFragment;
import com.purdue.a407.cryptodisco.Fragments.ExchangesFragment;
import com.purdue.a407.cryptodisco.Fragments.NotificationsFragment;
import com.purdue.a407.cryptodisco.Fragments.MyExchangeFragment;

import com.purdue.a407.cryptodisco.Fragments.SettingsFragment;
import com.purdue.a407.cryptodisco.Fragments.WatchlistFragment;
import com.purdue.a407.cryptodisco.R;
import com.purdue.a407.cryptodisco.ViewModels.ChatMsgViewModel;
import com.purdue.a407.cryptodisco.ViewModels.CoinViewModel;
import com.purdue.a407.cryptodisco.ViewModels.ExchangesViewModel;
import com.purdue.a407.cryptodisco.ViewModels.WatchlistViewModel;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Inject ViewModelProvider.Factory viewModelFactory;

    @Inject
    ExchangesViewModel viewModel;

    @Inject
    CoinViewModel coinsvm;

    @Inject
    WatchlistViewModel watchvm;

    @Inject
    ChatMsgViewModel msgvm;

    @Inject
    SharedPreferences sharedPreferences;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.watermark)
    ImageView watermark;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ((App) getApplication()).getNetComponent().inject(this);
        ButterKnife.bind(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        viewModel.getExchangesList().observe(this, listCDResource -> {
            //viewModel.getExchangesList().removeObservers(this);
        });
        coinsvm.getCoins().observe(this, listCDResource -> {
            //coinsvm.getCoins().removeObservers(this);
        });
        watchvm.getWatchList().observe(this, listCDResource -> {
            //coinsvm.getCoins().removeObservers(this);
        });
        //msgvm.getChatmessagesList().observe(this, listCDResource -> {

        //});




    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.d("BACK STACK COUNT",
                    String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            }
            else {
                finish();
            }
        }
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (id == R.id.nav_account) {
            ExchangesFragment fragment = new ExchangesFragment();

            //Notify fragment this is the account tab
            Bundle args = new Bundle();
            args.putBoolean("isAccount", TRUE);
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().
                    replace(R.id.replaceView,fragment).addToBackStack("myExchanges").commit();
        }
        else if (id == R.id.nav_exchanges) {
            ExchangesFragment fragment = new ExchangesFragment();

            //Notify fragment this is the exchange tab
            Bundle args = new Bundle();
            args.putBoolean("isAccount", FALSE);
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().
                    replace(R.id.replaceView,fragment).addToBackStack("exchanges").commit();
        }
        else if (id == R.id.nav_watchlist) {
            WatchlistFragment fragment = new WatchlistFragment();
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.replaceView,fragment).addToBackStack("watchlist").commit();
        }
        //else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        }
        else if (id == R.id.nav_chat) {
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        } else if(id == R.id.nav_notifications) {
            NotificationsFragment fragment = new NotificationsFragment();
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.replaceView,fragment).addToBackStack("notifications").commit();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
