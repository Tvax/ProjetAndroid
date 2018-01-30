package com.example.teiva.testdrawer;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.teiva.testdrawer.map.Map;
import com.example.teiva.testdrawer.map.PointOfInterest;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Map map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Translucid
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //Map
        Context ctx = getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        //Android
        setContentView(R.layout.activity_drawer);

        if (savedInstanceState != null) {
            savedInstanceState.getParcelableArrayList("listPointOfInterest");
            map = new Map((MapView) findViewById(R.id.map), true);
        } else {
            map = new Map((MapView) findViewById(R.id.map), true);

            MapEventsReceiver l = new MapEventsReceiver() {
                @Override
                public boolean singleTapConfirmedHelper(GeoPoint p) {
                    return false;
                }

                @Override
                public boolean longPressHelper(GeoPoint p) {
                    //TODO: ajouter un marqueur (CAD afficher fenetre pour choisir type de marqueur + option

                    //oblige de laisser ici pour le getBaseContext()
                    //startActivity(new Intent(getBaseContext(), SettingsActivity.class));
                    map.addPointOfInterest(new PointOfInterest(p, map.getMapView()));
                    //map.getMapView().getOverlays().add(new PointOfInterest(p, map.getMapView()).getNewMarker());
                    return false;
                }
            };
            map.setEventOverlay(l);
        }


//        final MapView map = findViewById(R.id.map);
//        map.setTileSource(TileSourceFactory.MAPNIK);
//        IMapController mapController = map.getController();
//        mapController.setZoom(9);
//        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
//        mapController.setCenter(startPoint);
//        map.setMultiTouchControls(true);
//
//
//        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(map);
//        mRotationGestureOverlay.setEnabled(true);
//        map.setMultiTouchControls(true);
//        map.getOverlays().add(mRotationGestureOverlay);
//
//
//
//        //Event quand touche la carte
//        MapEventsReceiver l = new MapEventsReceiver() {
//            @Override
//            public boolean singleTapConfirmedHelper(GeoPoint p) {
//                return false;
//            }
//
//            @Override
//            public boolean longPressHelper(GeoPoint p) {
//                //TODO: ajouter un marqueur (CAD afficher fenetre pour choisir type de marqueur + option
//
//                map.getOverlays().add(new PointOfInterest(p, map).getNewMarker());
//                return false;
//            }
//        };
//        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(l);
//        map.getOverlays().add(mapEventsOverlay);


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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            startActivity(new Intent(getBaseContext(), SettingsActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onResume() {
        super.onResume();

        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

//        savedInstanceState.putBoolean("MyBoolean", true);
//        savedInstanceState.putDouble("myDouble", 1.9);
//        savedInstanceState.putInt("MyInt", 1);
//        savedInstanceState.putString("MyString", "Welcome back to Android");


        savedInstanceState.putAll(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

//onRestoreInstanceState

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
//            boolean myBoolean = savedInstanceState.getBoolean("MyBoolean");
//            double myDouble = savedInstanceState.getDouble("myDouble");
//            int myInt = savedInstanceState.getInt("MyInt");
//            String myString = savedInstanceState.getString("MyString");
        }
    }

