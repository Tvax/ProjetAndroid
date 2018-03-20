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

public class Map{

    private final Context applicationContext;
    private final FragmentManager supportFragmentManager;
    private List<PointOfInterest> listPointOfInterest = new ArrayList<>();
    private GeoPoint lastGeoPointClick;
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    private MapEventsReceiver mapEventsReceiver;


    private List<PointOfInterest> getListPointOfInterest(){return listPointOfInterest;}

    public MapView getMapView() {return mapView;}
    public GeoPoint getLastGeoPointClick() {return lastGeoPointClick;}

    private MapView mapView;

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

    public void setMapViewStartingPoint(float lat, float lng, int zoomLevel){
        IMapController mapController = mapView.getController();
        mapController.setZoom(zoomLevel);
        mapController.setCenter(new GeoPoint(lat,lng));
    }

    public void addPointOfInterest(PointOfInterest pointOfInterest){
        if(isAlreadyInList(pointOfInterest)){
            removePointOfInterest(pointOfInterest);
        }
        listPointOfInterest.add(pointOfInterest);
        mapView.getOverlays().add(pointOfInterest.getMarker());
        mapView.invalidate();
    }

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

    private void removePointOfInterest(PointOfInterest pointOfInterest){
        listPointOfInterest.remove(pointOfInterest);
        mapView.getOverlays().remove(pointOfInterest.getMarker());
        mapView.invalidate();
    }

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

    private void enableRotation(){
        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(mapView);
        mRotationGestureOverlay.setEnabled(true);
        mapView.getOverlays().add(mRotationGestureOverlay);
    }

    private void enableMultiTouch(){
        mapView.setMultiTouchControls(true);
    }

    private void resetMap() {
        //delete everything
        listPointOfInterest.clear();
        mapView.getOverlays().clear();

        //add rotation + event listener
        setEventOverlay(mapEventsReceiver);
        enableMultiTouch();
        enableRotation();
    }

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

    private void addPOIFirebaseToMapOverlays(POIFirebase poiFirebase) {
        addPointOfInterest(Converter.POIFirebaseTopointOfInterest(poiFirebase, this, applicationContext, supportFragmentManager));
    }
}
