package com.example.vanlife.view;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vanlife.R;
import com.example.vanlife.model.map.Map;
import com.example.vanlife.model.map.PointOfInterest;
import com.example.vanlife.model.map.Type;
import com.example.vanlife.util.Display;
import com.example.vanlife.util.Keyboard;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PointOfInterestFragment.mapEventListener {
    private final static String STARTED = "STARTED";
    private static final String SETTINGS = "SETTINGS";
    private static final String ABOUT = "ABOUT";
    Map map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Map
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setContentView(R.layout.activity_drawer);

        //Tips
        //exception if "started" not set so app has just be launched
        //otherwise no so do not display message
        try{
            savedInstanceState.getBoolean(STARTED);
        }catch (Exception e){
            Display.StartupTips(getApplicationContext());
        }

        //Map
        map = new Map((MapView) findViewById(R.id.map), getApplicationContext(), getSupportFragmentManager());

        //Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void createPOI(String s, Type type) {
        Keyboard.close(this);
        map.addPointOfInterest(new PointOfInterest(map.getLastGeoPointClick(), map.getMapView(), type, s, getApplicationContext(), getSupportFragmentManager()));
    }

    @Override
    public void modifyPOI(String id, GeoPoint geoPoint, String s, Type type) {
        Keyboard.close(this);
        removePOI(id);
        map.addPointOfInterest(new PointOfInterest(geoPoint, map.getMapView(), type, s, getApplicationContext(), getSupportFragmentManager()));
    }

    @Override
    public void removePOI(String id){
        Keyboard.close(this);
        map.removePointOfInterestViaID(id);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            openFragment(new SettingsFragment());
        }
        else if (id == R.id.nav_about) {
            openFragment(new AboutFragment());
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void closeAllFragments(){
        getSupportFragmentManager().popBackStack();
    }

    private void openFragment(Fragment fragment){
        closeAllFragments();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_placeholder, fragment, SETTINGS)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();

    }



    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }

    @Override
    public void onStop() {
        super.onStop();
        map.saveMap();
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        //to not show tips again
        bundle.putBoolean(STARTED, true);
    }
}

