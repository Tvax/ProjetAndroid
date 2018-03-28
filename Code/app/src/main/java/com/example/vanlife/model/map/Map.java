package com.example.vanlife.model.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.vanlife.R;
import com.example.vanlife.firebase.POIFirebase;
import com.example.vanlife.util.Converter;
import com.example.vanlife.util.Display;
import com.example.vanlife.util.GatewayDatabase;
import com.example.vanlife.view.PointOfInterestFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class representing a points of interest map (PointOfInterest)
 *
 * @author tefleury, thpradier
 */

public class Map{
    /**
     * application context
     */
    private final Context applicationContext;
    /**
     * fragment manager
     * (used to display a fragment)
     */
    private final FragmentManager supportFragmentManager;
    /**
     * PointOfInterest List to display on map
     */
    private List<PointOfInterest> listPointOfInterest = new ArrayList<>();
    /**
     * last position clicked
     */
    private GeoPoint lastGeoPointClick;
    /**
     * database Firebase
     */
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    /**
     * event manager
     */
    private MapEventsReceiver mapEventsReceiver;
    /**
     * map view (graphic element)
     */
    private MapView mapView;


    /**
     * Returns to PointOfInterest List
     *
     * @return PointOfInterest List
     */
    private List<PointOfInterest> getListPointOfInterest(){return listPointOfInterest;}
    
    /**
     * Returns to map view
     *
     * @return map view
     */
    public MapView getMapView() {return mapView;}

    /**
     * Returns to last position clicked
     *
     * @return last position clicked
     */
    public GeoPoint getLastGeoPointClick() {return lastGeoPointClick;}

    /**
     * Constructor of Map with parameters
     *
     * @param mapView map view to display
     * @param applicationContext application context where display
     * @param supportFragmentManager fragment manager
     */
    public Map(MapView mapView, Context applicationContext, FragmentManager supportFragmentManager){
        this.mapView = mapView;
        this.applicationContext = applicationContext;
        this.supportFragmentManager = supportFragmentManager;
        GatewayDatabase.loadMap(this, applicationContext);
        enableRotation();
        enableMultiTouch();
        setDatabase();
        setMapListener();
    }

    /**
     * set starting point of map
     *
     * @param lat PointOfInterest latitude
     * @param lng PointOfInterest longitude
     * @param zoomLevel level of zoom
     */
    public void setMapViewStartingPoint(float lat, float lng, int zoomLevel){
        IMapController mapController = mapView.getController();
        mapController.setZoom(zoomLevel);
        mapController.setCenter(new GeoPoint(lat,lng));
    }

    /**
     * add PointOfInterest to the map
     *
     * @param pointOfInterest PointOfInterest to add
     */
    public void addPointOfInterest(PointOfInterest pointOfInterest){
        if(isAlreadyInList(pointOfInterest)){
            removePointOfInterest(pointOfInterest);
        }
        listPointOfInterest.add(pointOfInterest);
        mapView.getOverlays().add(pointOfInterest.getMarker());
        mapView.invalidate();
    }

    /**
     * search if the PointOfInterest in parameters to the map
     *
     * @param pointOfInterest PointOfInterest
     */
    private boolean isAlreadyInList(PointOfInterest pointOfInterest){
        for (PointOfInterest p : listPointOfInterest) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if(Objects.equals(p.getId(), pointOfInterest.getId())){
                    return true;
                }
            }
            else if(p.getId() == pointOfInterest.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * remove PointOfInterest to the map
     *
     * @param pointOfInterest PointOfInterest to remove
     */
    private void removePointOfInterest(PointOfInterest pointOfInterest){
        listPointOfInterest.remove(pointOfInterest);
        mapView.getOverlays().remove(pointOfInterest.getMarker());
        mapView.invalidate();
    }

    /**
     * remove PointOfInterest to the map with ID (search the PointOfInterest with ID and then deletes it)
     *
     * @param id id of PointOfInterest to remove
     */
    public void removePointOfInterestViaID(final String id){
        try {
            for (PointOfInterest p : getListPointOfInterest()) {
                if (p.getId() == id) {
                    removePointOfInterest(p);
                }
            }
        }catch (Exception ignored){}
    }

    private void setEventOverlay(MapEventsReceiver l){
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(l);
        mapView.getOverlays().add(mapEventsOverlay);
    }

    /**
     * enable the screen rotation
     */
    private void enableRotation(){
        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(mapView);
        mRotationGestureOverlay.setEnabled(true);
        mapView.getOverlays().add(mRotationGestureOverlay);
    }

    /**
     * enable the screen multi-touch
     */
    private void enableMultiTouch(){
        mapView.setMultiTouchControls(true);
    }

    /**
     * remove all PointOfInterests to the map
     */
    private void resetMap() {
        //delete everything
        listPointOfInterest.clear();
        mapView.getOverlays().clear();

        //add rotation + event listener
        setEventOverlay(mapEventsReceiver);
        enableMultiTouch();
        enableRotation();
    }

    /**
     * prepares Map to receive events
     */
    private void setMapListener(){
        mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return false;
            }

            @SuppressLint("ResourceType")
            @Override
            public boolean longPressHelper(GeoPoint p) {
                lastGeoPointClick = p;

                FragmentTransaction ft = supportFragmentManager.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                ft.replace(R.id.fragment_placeholder, new PointOfInterestFragment())
                        .addToBackStack(null)
                        .commit();
                return false;
            }
        };
        setEventOverlay(mapEventsReceiver);
    }

    /**
     * save all PointOfInterests on the database
     */
    public void saveMap() {
        GatewayDatabase.saveMap(this, applicationContext);

        myRef.removeValue();

        for (PointOfInterest pointOfInterest : getListPointOfInterest()) {

            final POIFirebase tmp = new POIFirebase(pointOfInterest.getId(), pointOfInterest.getLat(), pointOfInterest.getLong(), pointOfInterest.getType(), pointOfInterest.getMarker().getSubDescription());
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(tmp.id);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //if object already exists its modified otherwise added
                    myRef.child(tmp.id).setValue(tmp);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Display.Toast(applicationContext.getString(R.string.error_saving_DB), applicationContext);
                }
            });
        }
    }

    /**
     * prepares database
     */
    private void setDatabase(){
        //Firebase
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //Clear map ici
                resetMap();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    addPOIFirebaseToMapOverlays(dataSnapshot1.getValue(POIFirebase.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Display.Toast(applicationContext.getString(R.string.erro_loading_DB), applicationContext);
            }
        });
    }

    /**
     * add PointOfInterest to the map with POIFirebase (convert the POIFirebase on PointOfInterest and then adds it)
     *
     * @param poiFirebase PointOfInterest of Firebase to add
     */
    private void addPOIFirebaseToMapOverlays(POIFirebase poiFirebase) {
        addPointOfInterest(Converter.POIFirebaseTopointOfInterest(poiFirebase, this, applicationContext, supportFragmentManager));
    }
}
