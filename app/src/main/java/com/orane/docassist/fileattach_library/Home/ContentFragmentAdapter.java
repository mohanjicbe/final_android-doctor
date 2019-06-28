package com.orane.docassist.fileattach_library.Home;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class ContentFragmentAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 1;
    private final Context c;

    public ContentFragmentAdapter(FragmentManager fragmentManager, Context context, int item_count) {
        super(fragmentManager);
        NUM_ITEMS = item_count;
        c = context;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return HomeFragment.newInstance(position);
            case 1:
                return BookingsFragment.newInstance(position);
            case 2:
                return QueriesFragment.newInstance(position);
        }

        return null;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Home";
            case 1:
                return "Bookings";
            case 2:
                return "Queries";
        }


        return null;
    }

}
