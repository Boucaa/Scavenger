package com.colander.scavenger;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by colander on 11/28/15.
 */
public class DrawerItemClickListener implements ListView.OnItemClickListener {

    FragmentManager manager;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    selectItem(position);
    }

    public DrawerItemClickListener(String[] mPlanetTitles, DrawerLayout mDrawerLayout, ListView mDrawerList, FragmentManager manager) {
    this.manager = manager;
        this.mPlanetTitles = mPlanetTitles;
        this.mDrawerLayout = mDrawerLayout;
        this.mDrawerList = mDrawerList;
    }

    public void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        //args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);
        System.out.println(manager == null);
        // Insert the fragment by replacing any existing fragment
        manager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        //TODO TITLE setTitle(mPlanetTitles[position]);

        mDrawerLayout.closeDrawer(mDrawerList);
    }
}
