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

import com.example.vanlife.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 *
 * Created by tefleury on 07/03/18.
 */

public class AboutFragment extends Fragment {
    private static final String WHITE_SPACE = " ";
    private static final String SLASH = "/";
    FragmentActivity listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //TODO: disable opening fragment from fragment

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (FragmentActivity) context;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return new AboutPage(parent.getContext())
                .isRTL(false)
                .setDescription(getString(R.string.about_description))
                .addGroup(getString(R.string.libraries))
                .addGitHub(getString(R.string.osmdroid) + SLASH + getString(R.string.osmdroid), getString(R.string.osmdroid))
                .addGitHub(getString(R.string.about_AndroidGithub), getString(R.string.about_androidAboutPage))
                .addGroup(getString(R.string.some_more))
                .addItem(new Element()
                        .setTitle(getString(R.string.icon_by) + WHITE_SPACE + getString(R.string.freepik_url)))
                .addItem(new Element()
                        .setTitle(getString(R.string.vanlife_version_string)))
                .addItem(new Element()
                        .setTitle(getString(R.string.gpl3_string)))
                .create();
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
