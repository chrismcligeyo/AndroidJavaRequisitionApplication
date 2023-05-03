package com.mcligeyo.rsmhub;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.mcligeyo.rsmhub.adapters.FragPageAdapter;

public class RequisitionExpensesActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragPageAdapter fragPageAdapter;
    private TabItem pending;
    private TabItem approved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition_expenses);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.requisition_expenses)); //Requisition Expenses
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tabLayout = (TabLayout) findViewById(R.id.reqExpensesTablayout);
        pending = (TabItem) findViewById(R.id.reqExpensesTabPending);
        approved = (TabItem) findViewById(R.id.reqExpensesTabApproved);
        viewPager = (ViewPager) findViewById(R.id.reqExpensesViewpager);

        fragPageAdapter = new FragPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(fragPageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));//makes view pager in sync with tab items in tablayout.


    }
}
