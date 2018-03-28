package com.example.vanlife.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.vanlife.R;
import com.example.vanlife.model.map.Type;
import com.example.vanlife.util.Keyboard;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Class representing the PointOfInterest fragment
 *
 * @author tefleury, thpradier
 */

public class PointOfInterestFragment extends Fragment {
    /**
     * PointOfInterest id name
     */
    private final static String ID = "ID";
    /**
     * PointOfInterest description name
     */
    private final static String DESCRIPTION = "DESCRIPTION";
    /**
     * PointOfInterest type name
     */
    private final static String TYPE = "TYPE";
    /**
     * PointOfInterest geopoint name
     */
    private final static String GEOPOINT = "GEOPOINT";
    /**
     * exception name
     */
    private final static String EXCEPTION =  " must implement mapEventListener";
    /**
     * first index name
     */
    private static final int FIRST_INDEX = 0;

    /**
     * activity manager
     */
    FragmentActivity listener;
    /**
     * event manager
     */
    mapEventListener mapEventListener;

    /**
     * PointOfInterest geopoint
     */
    GeoPoint geoPoint;
    /**
     * PointOfInterest id
     */
    String id;
    /**
     * PointOfInterest description
     */
    String description;
    /**
     * PointOfInterest type
     */
    String type;
    /**
     * boolean used to know the PointOfInterest is modified
     */
    private boolean modified = false;

    /**
     * Interface with all methods which implements to create, modify or remove the PointOfInterest
     *
     * @author tefleury, thpradier
     */
    public interface mapEventListener {
        /**
         * create a PointOfInterest with parameters on map
         * 
         * @param s PointOfInterest description
         * @param type PointOfInterest type
         */
        void createPOI(String s, Type type);
        /**
         * modify a PointOfInterest with parameters on map
         * 
         * @param id PointOfInterest id
         * @param geoPoint PointOfInterest geoPoint
         * @param s PointOfInterest description
         * @param type PointOfInterest type
         */
        void modifyPOI(String id, GeoPoint geoPoint, String s, Type type);
        /**
         * create a PointOfInterest with parameters on map
         * 
         * @param id PointOfInterest id
         */

        void removePOI(String id);
    }

    /**
     * when the fragment is launched
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * when the fragment is display
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (FragmentActivity) context;
        }
        try {
            mapEventListener = (mapEventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + EXCEPTION);
        }
        try {
            this.id = (String) getArguments().get(ID);
            this.description = (String) getArguments().get(DESCRIPTION);
            this.type = (String) getArguments().get(TYPE);
            this.geoPoint = (GeoPoint) getArguments().get(GEOPOINT);
            this.modified = true;
        }catch (Exception ignored){}
    }

    /**
     * before the fragment is display
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.point_of_interest_activity, parent, false);
    }

    /**
     * after the fragment is display
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * deduct the populate type tab
     */
    private String[] populateTypeTab(){
        int i = Type.values().length;

        ArrayList<Type> tmp = new ArrayList<>();

        SortedMap<String, Type> map = new TreeMap<>();
        for (Type t : Type.values()) {
            map.put(getString(t.getDescription()), t);
        }
        List<String> l = new ArrayList<>(map.keySet());
        for (String s : l){
            tmp.add(getTypeFromString(s));
        }
        Collections.reverse(tmp);
        tmp.remove(Type.OTHER);
        tmp.add(FIRST_INDEX,Type.OTHER);


        String[] tmp1 = new String[i];

        for (Type t : tmp){
            tmp1[i-1] = getString(t.getDescription());
            i--;
        }
        return tmp1;
    }

    /**
     * deduct type with string type
     * 
     * @param type string type
     */
    private Type getTypeFromString(String type){
        for (Type t : Type.values()){
            if(getString(t.getDescription()) == type){
                return t;
            }
        }
        return null;
    }

    /**
     * when the fragment is display like activity
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Type[] type = new Type[1];
        final String[] description = new String[1];

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                getContext(),
                android.R.layout.simple_spinner_item,
                populateTypeTab()
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner spinner = getView().findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        final TextView descriptionTextView = getView().findViewById(R.id.textInputDescription);

        //si l'objet passe est modifie on a ces parametres
        if(this.modified){
            descriptionTextView.setText(this.description);
            spinner.setSelection(adapter.getPosition(this.type));
        }

        Button clickButton = getView().findViewById(R.id.submitButton);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description[0] = descriptionTextView.getText().toString();
                try {
                    type[0] = getTypeFromString((String) spinner.getSelectedItem());
                } catch (Exception e) {
//                    TODO: display toast error like type is not available
                }

                if(modified){
                    mapEventListener.modifyPOI(id, geoPoint, description[0], type[0]);
                }else {
                    mapEventListener.createPOI(description[0], type[0]);
                }
                getActivity().onBackPressed();
            }
        });

        Button removeButton = getView().findViewById(R.id.removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id != null){
                    mapEventListener.removePOI(id);
                }
                getActivity().onBackPressed();
            }
        });

        if(!modified){
            removeButton.setClickable(false);
            removeButton.setAlpha(.5f);
        }

        Button cancelButton = getView().findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Keyboard.close(getActivity());
                getActivity().onBackPressed();
            }
        });
    }
}
