package com.mcligeyo.rsmhub.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.mcligeyo.rsmhub.R;
import com.mcligeyo.rsmhub.adapters.FragPageAdapter;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewRequisitionFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragPageAdapter fragPageAdapter;
    private TabItem pending;
    private TabItem approved;

    public ViewRequisitionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_requisition, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar() != null) {
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(getResources().getString(R.string.requisition_expenses)); //Requisition Expenses
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

        tabLayout = (TabLayout) view.findViewById(R.id.reqExpensesTablayout);
        pending = (TabItem) view.findViewById(R.id.reqExpensesTabPending);
        approved = (TabItem) view.findViewById(R.id.reqExpensesTabApproved);
        viewPager = (ViewPager) view.findViewById(R.id.reqExpensesViewpager);

        fragPageAdapter = new FragPageAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
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
