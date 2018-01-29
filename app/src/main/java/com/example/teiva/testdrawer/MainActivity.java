package com.example.teiva.testdrawer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import android.widget.Toast;

import com.example.teiva.testdrawer.map.InterestPoint;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme;

import java.util.ArrayList;
import java.util.List;

import static org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay.backgroundColor;
import static org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay.fontColor;
import static org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay.fontSizeDp;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

        //Map
        final MapView map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        IMapController mapController = map.getController();
        mapController.setZoom(9);
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);
        map.setMultiTouchControls(true);

        // create 10k labelled points
// in most cases, there will be no problems of displaying >100k points, feel free to try
        List<IGeoPoint> points = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            points.add(new LabelledGeoPoint(37 + Math.random() * 5, -8 + Math.random() * 5
                    , "Point #" + i));
        }

// wrap them in a theme
        SimplePointTheme pt = new SimplePointTheme(points, true);


// set some visual options for the overlay
// we use here MAXIMUM_OPTIMIZATION algorithm, which works well with >100k points
        SimpleFastPointOverlayOptions opt = SimpleFastPointOverlayOptions.getDefaultStyle()
                .setAlgorithm(SimpleFastPointOverlayOptions.RenderingAlgorithm.MAXIMUM_OPTIMIZATION)
                .setRadius(7).setIsClickable(true).setCellSize(15).setTextStyle(new Paint());

// create the overlay with the theme
        final SimpleFastPointOverlay sfpo = new SimpleFastPointOverlay(pt, opt);

// onClick callback
        sfpo.setOnClickListener(new SimpleFastPointOverlay.OnClickListener() {
            @Override
            public void onClick(SimpleFastPointOverlay.PointAdapter points, Integer point) {
                Toast.makeText(map.getContext()
                        , "You clicked " + ((LabelledGeoPoint) points.get(point)).getLabel()
                        , Toast.LENGTH_SHORT).show();
            }
        });

// add overlay
        map.getOverlays().add(sfpo);

        //Rotation
        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(map);
        mRotationGestureOverlay.setEnabled(true);
        map.setMultiTouchControls(true);
        map.getOverlays().add(mRotationGestureOverlay);

        //Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Event quand touche la carte
        MapEventsReceiver l = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                //TODO: ajouter un marqueur (CAD afficher fenetre pour choisir type de marqueur + option
                //enables this opt in feature
//                Marker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true;
////build the InterestPoint
//                Marker m = new Marker(map);
//                m.setTextLabelBackgroundColor(backgroundColor);
//                m.setTextLabelFontSize(fontSizeDp);
//                m.setTextLabelForegroundColor(fontColor);
//                m.setTitle("hello world");
////must set the icon to null last
//                m.setIcon(null);
//                m.setPosition(p);

                map.getOverlays().add(new InterestPoint(p, map).getNewMarker());
                return false;
            }
        };
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(l);
        map.getOverlays().add(mapEventsOverlay);
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
        }
        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onResume(){
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

        savedInstanceState.putBoolean("MyBoolean", true);
        savedInstanceState.putDouble("myDouble", 1.9);
        savedInstanceState.putInt("MyInt", 1);
        savedInstanceState.putString("MyString", "Welcome back to Android");

        // etc.

        savedInstanceState.putAll(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

//onRestoreInstanceState

//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//
//        super.onRestoreInstanceState(savedInstanceState);
//        // Restore UI state from the savedInstanceState.
//        // This bundle has also been passed to onCreate.
//
//        boolean myBoolean = savedInstanceState.getBoolean("MyBoolean");
//        double myDouble = savedInstanceState.getDouble("myDouble");
//        int myInt = savedInstanceState.getInt("MyInt");
//        String myString = savedInstanceState.getString("MyString");
//    }
}
