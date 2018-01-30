package com.example.teiva.testdrawer.map;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

import java.util.ArrayList;
import java.util.List;

public class Map{

    private List<PointOfInterest> listPointOfInterest = new ArrayList<>();
    public List<PointOfInterest> getListPointOfInterest(){return listPointOfInterest;}

    public MapView getMapView() {return mapView;}

    MapView mapView;


    public Map(MapView mapView, boolean b){
        this.mapView = mapView;
        enableRotation();
        enableMultiTouch();

        if (b){
            setMapViewDefaultStartingPoint();
        }
    }

    public void setMapViewDefault(){
        //mapView.setTileSource(TileSourceFactory.MAPNIK);
    }

    private void setMapViewDefaultStartingPoint(){
        setMapViewStartingPoint(45, 39, 9);
    }

    public void setMapViewStartingPoint(float lat, float lng, int zoomLevel){
        IMapController mapController = mapView.getController();
        mapController.setZoom(zoomLevel);
        mapController.setCenter(new GeoPoint(lat,lng));
    }

    public void addPointOfInterest(PointOfInterest pointOfInterest){
        listPointOfInterest.add(pointOfInterest);
        mapView.getOverlays().add(pointOfInterest.getMarker());
    }

    public void removePointOfInterest(PointOfInterest pointOfInterest){
        listPointOfInterest.remove(pointOfInterest);
        mapView.getOverlays().remove(pointOfInterest.getMarker());
    }

    public void setEventOverlay(MapEventsReceiver l){
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

}
