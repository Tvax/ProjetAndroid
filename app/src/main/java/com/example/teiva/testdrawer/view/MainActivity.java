package com.example.teiva.testdrawer.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.teiva.testdrawer.R;
import com.example.teiva.testdrawer.firebase.POIFirebase;
import com.example.teiva.testdrawer.model.map.Map;
import com.example.teiva.testdrawer.model.map.PointOfInterest;
import com.example.teiva.testdrawer.model.map.Type;
import com.example.teiva.testdrawer.util.GatewayDatabase;
import com.github.florent37.rxgps.RxGps;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Map map;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        new RxGps(this).locationLowPower()
//
//                .subscribeOn(Schedulers.newThread())
//                //.observeOn(new Scheduler() {@Override public Worker createWorker() {return null;}})
//
//                .subscribe(location -> {
//                    Log.d("location", String.valueOf(location.getLatitude()));
//                    Log.d("location", String.valueOf(location.getLongitude()));
//                    //double lol =
//                    //you've got the location
//                }, throwable -> {
//                    if (throwable instanceof RxGps.PermissionException) {
//                        //the user does not allow the permission
//                    } else if (throwable instanceof RxGps.PlayServicesNotAvailableException) {
//                        //the user do not have play services
//                    }
//                });
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        double lw = location.getLongitude();

        //Translucid
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //Map
        final Context ctx = getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        //Android
        setContentView(R.layout.activity_drawer);

        //Map
        map = new Map((MapView) findViewById(R.id.map), false);
        GatewayDatabase.loadMap(map, getApplicationContext());
        //map.setMapViewStartingPoint((float) lel2.getLatitude(), (float) lel2.getLongitude(), 10);
//        GpsMyLocationProvider provider = new GpsMyLocationProvider(getApplicationContext());
//        provider.addLocationSource(LocationManager.GPS_PROVIDER);
//        MyLocationNewOverlay myLocationNewOverlay = new MyLocationNewOverlay(provider, map.getMapView());
//        myLocationNewOverlay.enableMyLocation();
//        map.getMapView().getOverlays().add(myLocationNewOverlay);

//        LocationConfiguration awesomeConfiguration = silentConfiguration(false);
//        com.yayandroid.locationmanager.LocationManager awesomeLocationManager = new com.yayandroid.locationmanager.LocationManager.Builder(getApplicationContext())
//                .activity(this) // Only required to ask permission and/or GoogleApi - SettingsApi
//                //.fragment(fragmentInstance) // Only required to ask permission and/or GoogleApi - SettingsApi
//                .configuration(awesomeConfiguration)
//                //.locationProvider(new YourCustomLocationProvider())
//                //.notify(new LocationListener())
//                .build();
//        awesomeLocationManager.get();

        MapEventsReceiver l = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }

            @SuppressLint("ResourceType")
            @Override
            public boolean longPressHelper(final GeoPoint p) {
                //TODO: ajouter un marqueur (CAD afficher activity pour choisir type de marqueur + option

                //TODO: ajouter le marker une fois POI passe depuis l'acitvity qui le cree
                map.addPointOfInterest(new PointOfInterest(p, map.getMapView(), Type.VAN_SPOT, "Description", getApplicationContext()));

                //reaload la map avec les changements
                map.getMapView().invalidate();

                return false;
            }
        };
        map.setEventOverlay(l);

        //Firebase
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    addPOIFirebaseToMapOverlays(dataSnapshot1.getValue(POIFirebase.class));
                }
//                POIFirebase lel = dataSnapshot.child("0").getValue(POIFirebase.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        //Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

//    public static LocationConfiguration silentConfiguration(boolean keepTracking) {
//        return new LocationConfiguration.Builder()
//                .keepTracking(keepTracking)
//                //.useGooglePlayServices(new GooglePlayServicesConfiguration.Builder().askForSettingsApi(false).build())
//                .useDefaultProviders(new DefaultProviderConfiguration.Builder().build())
//                .build();
//    }

    private void addPOIFirebaseToMapOverlays(POIFirebase poiFirebase){
        map.addPointOfInterest(convertPOIFirebaseTopointOfInterest(poiFirebase));
        map.getMapView().invalidate();
    }

    private PointOfInterest convertPOIFirebaseTopointOfInterest(POIFirebase poiFirebase) {
        return new PointOfInterest(new GeoPoint(poiFirebase.lat, poiFirebase.lng), map.getMapView(), poiFirebase.type, poiFirebase.description, getApplicationContext());
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
            startActivity(new Intent(getBaseContext(), com.example.teiva.testdrawer. view.SettingsActivity.class));
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
    public void onStop() {
        super.onStop();
        GatewayDatabase.saveMap(map, getApplicationContext());

        //int i = 0;

        for (PointOfInterest pointOfInterest : map.getListPointOfInterest()){
            final POIFirebase tmp = new POIFirebase(pointOfInterest.getLat(), pointOfInterest.getLong(), pointOfInterest.getType(), pointOfInterest.getMarker().getSubDescription());

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(tmp.id);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){
                        myRef.child(tmp.id).setValue(tmp);
                        //TODO: check if values have been modified before not adding it to DB
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //myRef.child(String.valueOf(i)).setValue(tmp);
            //myRef.child("POIs").child(String.valueOf(i)).setValue(tmp);
            //i++;
        }
/*
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

*/

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

