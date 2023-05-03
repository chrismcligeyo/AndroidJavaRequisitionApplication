package com.mcligeyo.rsmhub.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mcligeyo.rsmhub.fragment.ApprovedExpensesFragment;
import com.mcligeyo.rsmhub.fragment.PendingExpensesFragment;

public class FragPageAdapter extends FragmentPagerAdapter {

    //    public FragPageAdapter(@NonNull FragmentManager fm, int behavior) {
//        super(fm, behavior);
//    }
    private int numOfTabs;

    public FragPageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                return fragment = new PendingExpensesFragment();
//                return new PendingExpensesFragment();
            case 1:
                return fragment = new ApprovedExpensesFragment();
//            default:
//                return null;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
