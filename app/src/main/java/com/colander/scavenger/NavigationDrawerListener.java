package com.colander.scavenger;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by colander on 3/12/16.
 */
public class NavigationDrawerListener implements NavigationView.OnNavigationItemSelectedListener {

    private AppCompatActivity activity;
    private DrawerLayout drawerLayout;
    private Fragment[] fragments;
    private Menu menu;

    public NavigationDrawerListener(AppCompatActivity activity) {
        this.activity = activity;
        this.drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        this.menu = ((NavigationView) activity.findViewById(R.id.navigation_view)).getMenu();
        this.fragments = new Fragment[menu.size()];
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        for (int i = 0; i < menu.size(); i++) {
            item.setChecked(false);
            if (item == menu.getItem(i)) {
                selectItem(i);
            }
        }
        item.setChecked(true);
        drawerLayout.closeDrawers();
        return true;
    }

    public void selectItem(int position) {
        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[0] = new MapFragment();
                    break;
                case 1:
                    fragments[1] = new ScanFragment();
                    break;
                case 2:
                    fragments[2] = new Fragment();
            }
        }
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragments[position]).commit();
        activity.setTitle(menu.getItem(position).getTitle());
    }
}
