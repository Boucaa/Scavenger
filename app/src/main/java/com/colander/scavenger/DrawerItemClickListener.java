package com.colander.scavenger;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by colander on 11/28/15.
 */
public class DrawerItemClickListener implements ListView.OnItemClickListener, OnMapReadyCallback {

    FragmentManager manager;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private android.support.v4.app.FragmentManager sup;
private AppCompatActivity parent;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
    }

    public DrawerItemClickListener(String[] mPlanetTitles, DrawerLayout mDrawerLayout, ListView mDrawerList, FragmentManager manager,AppCompatActivity parent) {
        this.manager = manager;
        this.mPlanetTitles = mPlanetTitles;
        this.mDrawerLayout = mDrawerLayout;
        this.mDrawerList = mDrawerList;
        this.parent = parent;
    }

    public void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ScanFragment();
                Bundle args = new Bundle();
                fragment.setArguments(args);
                manager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            case 1:
                fragment = new MapFragment();
                manager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;

        }


        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        parent.setTitle(mPlanetTitles[position]);

        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        LatLng sydney = new LatLng(-33.867, 151.206);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));
    }
}
