package com.pilloclock.medicinereminder.app.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/***
 * Created by Nikko on May.
 * Project name: Pillo'Clock
 */

//View page adapter class for tab view
public class ViewPagerAdapter extends FragmentPagerAdapter {


    private final ArrayList<Fragment> fragments = new ArrayList<>();
    private final ArrayList<String> tabTitles = new ArrayList<>();

    public void addFragments(Fragment fragments, String titles)
    {
        this.fragments.add(fragments);
        this.tabTitles.add(titles);
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}
