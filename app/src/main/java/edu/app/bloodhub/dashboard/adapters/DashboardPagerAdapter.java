package edu.app.bloodhub.dashboard.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import edu.app.bloodhub.dashboard.fragments.DonorListFragment;
import edu.app.bloodhub.dashboard.fragments.EmergencyFragment;
import edu.app.bloodhub.dashboard.fragments.EventsFragment;

public class DashboardPagerAdapter extends FragmentStatePagerAdapter{

    private String tabTitles[] = new String[] { "Emergency", "Donor List", "Events" };

    public DashboardPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return EmergencyFragment.newInstance();
            case 1:
                return DonorListFragment.newInstance();
            case 2:
                return EventsFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {

        return tabTitles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
