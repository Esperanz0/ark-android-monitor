package com.vrlc92.arkmonitor;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vrlc92.arkmonitor.fragments.DelegatesFragment;
import com.vrlc92.arkmonitor.fragments.LatestTransactionsFragment;
import com.vrlc92.arkmonitor.fragments.MainFragment;
import com.vrlc92.arkmonitor.fragments.PeersFragment;
import com.vrlc92.arkmonitor.fragments.SettingsFragment;
import com.vrlc92.arkmonitor.fragments.VotersFragment;
import com.vrlc92.arkmonitor.fragments.VotesFragment;
import com.vrlc92.arkmonitor.models.Settings;
import com.vrlc92.arkmonitor.scheduler.ForgingAlarmReceiver;
import com.vrlc92.arkmonitor.utils.Utils;
import com.wang.avi.AVLoadingIndicatorView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.OnSavedSettingsListener {

    private final ForgingAlarmReceiver mAlarm = new ForgingAlarmReceiver();
    private AVLoadingIndicatorView mLoadingIndicatorView;
    private ActionBarDrawerToggle mToggle;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.vrlc92.arkmonitor.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(com.vrlc92.arkmonitor.R.id.toolbar);
        setSupportActionBar(toolbar);

        mLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(com.vrlc92.arkmonitor.R.id.loadingIndicator);

        DrawerLayout drawer = (DrawerLayout) findViewById(com.vrlc92.arkmonitor.R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, com.vrlc92.arkmonitor.R.string.navigation_drawer_open, com.vrlc92.arkmonitor.R.string.navigation_drawer_close);
        drawer.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(com.vrlc92.arkmonitor.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Settings settings = Utils.getSettings(this);

        if (Utils.validateUsername(settings.getUsername()) &&
                (Utils.validateIpAddress(settings.getIpAddress()) &&
                        Utils.validatePort(settings.getPort()) || settings.getDefaultServerEnabled())) {

            onNavigationItemSelected(navigationView.getMenu().getItem(NavItem.HOME.getIndex()));

        } else {
            mToggle.setDrawerIndicatorEnabled(false);
            onNavigationItemSelected(navigationView.getMenu().getItem(NavItem.SETTINGS.getIndex()));
        }
    }

    public void showLoadingIndicatorView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoadingIndicatorView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideLoadingIndicatorView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoadingIndicatorView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(com.vrlc92.arkmonitor.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.vrlc92.arkmonitor.R.menu.main, menu);

        this.mMenu = menu;

        MenuItem menuItem = menu.findItem(com.vrlc92.arkmonitor.R.id.action_alarm);

        if (Utils.alarmEnabled(this)) {
            menuItem.setIcon(ContextCompat.getDrawable(this, com.vrlc92.arkmonitor.R.drawable.ic_alarm_on_white_24dp));
        } else {
            menuItem.setIcon(ContextCompat.getDrawable(this, com.vrlc92.arkmonitor.R.drawable.ic_alarm_off_white_24dp));
        }

        Settings settings = Utils.getSettings(this);

        if (!Utils.validateUsername(settings.getUsername()) ||
                !(Utils.validateIpAddress(settings.getIpAddress()) &&
                        Utils.validatePort(settings.getPort()) || settings.getDefaultServerEnabled())) {
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == com.vrlc92.arkmonitor.R.id.action_alarm) {

            boolean alarmEnabled = Utils.alarmEnabled(this);
            if (Utils.enableAlarm(this, !alarmEnabled)) {

                MenuItem menuItem = mMenu.findItem(com.vrlc92.arkmonitor.R.id.action_alarm);

                alarmEnabled = Utils.alarmEnabled(this);

                if (alarmEnabled) {
                    menuItem.setIcon(ContextCompat.getDrawable(this, com.vrlc92.arkmonitor.R.drawable.ic_alarm_on_white_24dp));
                    mAlarm.setAlarm(this);
                } else {
                    menuItem.setIcon(ContextCompat.getDrawable(this, com.vrlc92.arkmonitor.R.drawable.ic_alarm_off_white_24dp));
                    mAlarm.cancelAlarm(this);
                }
            }

            View view = this.findViewById(android.R.id.content);

            int stringMessageId = alarmEnabled ? com.vrlc92.arkmonitor.R.string.alarm_on : com.vrlc92.arkmonitor.R.string.alarm_off;
            Utils.showMessage(getResources().getString(stringMessageId), view);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == com.vrlc92.arkmonitor.R.id.nav_home) {
            fragment = new MainFragment();
            setTitle(com.vrlc92.arkmonitor.R.string.nav_home);
        } else if (id == com.vrlc92.arkmonitor.R.id.nav_latest_transactions) {
            fragment = new LatestTransactionsFragment();
            setTitle(com.vrlc92.arkmonitor.R.string.nav_latest_transactions);
        } else if (id == com.vrlc92.arkmonitor.R.id.nav_peers) {
            fragment = new PeersFragment();
            setTitle(com.vrlc92.arkmonitor.R.string.nav_peers);
        } else if (id == com.vrlc92.arkmonitor.R.id.nav_delegates) {
            fragment = new DelegatesFragment();
            setTitle(com.vrlc92.arkmonitor.R.string.nav_delegates);
        } else if (id == com.vrlc92.arkmonitor.R.id.nav_settings) {
            fragment = new SettingsFragment();
            setTitle(com.vrlc92.arkmonitor.R.string.nav_settings);
        } else if (id == com.vrlc92.arkmonitor.R.id.nav_votes_made) {
            fragment = new VotesFragment();
            setTitle(com.vrlc92.arkmonitor.R.string.nav_votes_made);
        } else if (id == com.vrlc92.arkmonitor.R.id.nav_votes_received) {
            fragment = new VotersFragment();
            setTitle(com.vrlc92.arkmonitor.R.string.nav_votes_received);
        }

        if (fragment != null) {
            showFragment(fragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(com.vrlc92.arkmonitor.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();

        fragmentTransaction
                .replace(com.vrlc92.arkmonitor.R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSavedSettingsListener() {
        final MenuItem menuItem = mMenu.findItem(com.vrlc92.arkmonitor.R.id.action_alarm);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToggle.setDrawerIndicatorEnabled(true);
                NavigationView navigationView = (NavigationView) findViewById(com.vrlc92.arkmonitor.R.id.nav_view);
                onNavigationItemSelected(navigationView.getMenu().getItem(NavItem.HOME.getIndex()));

                menuItem.setVisible(true);
            }
        });
    }

    enum NavItem {
        HOME(0),
        LATEST_TRANSACTIONS(1),
        PEERS(2),
        DELEGATES(3),
        VOTES(4),
        VOTERS(5),
        SETTINGS(6);

        private final int index;

        NavItem(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }
}
