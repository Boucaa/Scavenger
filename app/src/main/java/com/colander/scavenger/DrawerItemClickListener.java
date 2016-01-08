package com.colander.scavenger;

import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by colander on 11/28/15.
 */
public class DrawerItemClickListener implements ListView.OnItemClickListener {

    FragmentManager manager;
    private String[] mDrawerNames;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private android.support.v4.app.FragmentManager sup;
    private AppCompatActivity parent;
    private Fragment[] fragments;


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
    }

    public DrawerItemClickListener(String[] mDrawerNames, DrawerLayout mDrawerLayout, ListView mDrawerList, FragmentManager manager, com.colander.scavenger.MainActivity parent) {
        this.manager = manager;
        this.mDrawerNames = mDrawerNames;
        this.mDrawerLayout = mDrawerLayout;
        this.mDrawerList = mDrawerList;
        this.parent = parent;
        this.fragments = new Fragment[mDrawerNames.length];
        selectItem(1);
    }

    public void selectItem(int position) {
        Fragment fragment = null;
        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[0] = new ScanFragment();
                    break;
                case 1:
                    fragments[1] = new com.colander.scavenger.MapFragment();
                    break;
                case 2:
                    fragments[2] = new Fragment();
            }
        }
        parent.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragments[position]).commit();

        mDrawerList.setItemChecked(position, true);
        parent.setTitle(mDrawerNames[position]);

        mDrawerLayout.closeDrawer(mDrawerList);
    }
}
