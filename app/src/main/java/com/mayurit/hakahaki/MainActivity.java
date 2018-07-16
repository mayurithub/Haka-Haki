package com.mayurit.hakahaki;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mayurit.hakahaki.Fragments.FragmentCategory;
import com.mayurit.hakahaki.Fragments.FragmentDocuments;
import com.mayurit.hakahaki.Fragments.FragmentFeedback;
import com.mayurit.hakahaki.Fragments.FragmentHome;
import com.mayurit.hakahaki.Fragments.FragmentMembers;
import com.mayurit.hakahaki.Fragments.FragmentNEFEJ;
import com.mayurit.hakahaki.Fragments.FragmentNotice;
import com.mayurit.hakahaki.Fragments.FragmentVideo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment;
    String toolbartitle = "Home";
    public static final String EXTRA_OBJC = "key.EXTRA_OBJC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        changeFragment(FragmentHome.newInstance());
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

                if (doubleBackToExitPressedOnce) {
                    ActivityCompat.finishAffinity(MainActivity.this);
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        int id = item.getItemId();
        Intent intent;
        Context context = getApplicationContext();
        if (id == R.id.nav_home) {
            changeFragment(FragmentHome.newInstance());
        } else if (id == R.id.nav_nefej) {
            changeFragment(FragmentNEFEJ.newInstance());
        } else if (id == R.id.nav_video) {
            changeFragment(FragmentVideo.newInstance());
        }  else if (id == R.id.nav_document) {
            changeFragment(FragmentDocuments.newInstance());
        } else if (id == R.id.nav_music) {
            intent = new Intent(context, AudioActivity.class);
                 startActivity(intent);
        } else if (id == R.id.nav_notice) {
            intent = new Intent(context, ActivityPostTypeList.class);
            intent.putExtra(EXTRA_OBJC, "notice");
            startActivity(intent);
        } else if (id == R.id.nav_category) {
            toolbartitle = (String) getText(R.string.category);
            changeFragment(FragmentCategory.newInstance());
        } else if (id == R.id.nav_rate_us) {
            RateUs();
        }else if (id == R.id.nav_feedback) {
            changeFragment(FragmentFeedback.newInstance());
        }
        else if (id == R.id.nav_setting) {

            intent = new Intent(MainActivity.this,Seek.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void navigateFromDrawer(Fragment fragment, String title) {
        // depending on whether the device is a phone or tablet
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, title)
                .commit();


    }


    public void RateUs() {
        Uri uri = Uri.parse("market://details?id=com.mayurit.hakahaki");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.mayurit.hakahaki" +
                            MainActivity.this.getPackageName())));
        }
    }

    public void changeFragment(Fragment fragment, String title) {
        Bundle bundle = new Bundle();
        bundle.putString("mTitle", title);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

    }

}
