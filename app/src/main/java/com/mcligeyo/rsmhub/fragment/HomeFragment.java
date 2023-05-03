package com.mcligeyo.rsmhub.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.mcligeyo.rsmhub.MakeRequisitionActivity;
import com.mcligeyo.rsmhub.MapsActivity;
import com.mcligeyo.rsmhub.R;
import com.mcligeyo.rsmhub.RequisitionExpensesActivity;
import com.mcligeyo.rsmhub.UserProfileActivity;
import com.mcligeyo.rsmhub.adapters.GridViewAdaptar;
import com.mcligeyo.rsmhub.model.MainGriddViewMenu;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private GridView mainGridView;
    private ArrayList<MainGriddViewMenu> mainGriddViewMenus;
    private GridViewAdaptar gridViewAdaptar;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("RSM Home");


        final String[] menuTextViewNames = {"Make Requisition", "View Requisition,", "Location", "User Profile"};

        final int[] menuIcons = {
                R.drawable.requisition_icon,
                R.drawable.view_requisition,
                R.drawable.map_icon,
                R.drawable.user_profile
        };

        final int[] menuBackround = {
                R.drawable.cerclebackgroundpink,
                R.drawable.cerclebackgroundgreen,
                R.drawable.cerclebackgroundpurple,
                R.drawable.cerclebackgroundyello
        };

        mainGriddViewMenus = new ArrayList<>();

        for (int i = 0; i < menuTextViewNames.length; i++) {
            MainGriddViewMenu mainGriddViewMenu = new MainGriddViewMenu();
            mainGriddViewMenu.setMainGridMenuName(menuTextViewNames[i]);
            mainGriddViewMenu.setMainGridMenuIcon(menuIcons[i]);
            mainGriddViewMenu.setBackGroundColor(menuBackround[i]);
            mainGriddViewMenus.add(mainGriddViewMenu);

        }

        mainGridView = (GridView) view.findViewById(R.id.main_act_gridview);
        gridViewAdaptar = new GridViewAdaptar(getActivity(), mainGriddViewMenus);
        mainGridView.setAdapter(gridViewAdaptar);
        mainGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(getActivity(), MakeRequisitionActivity.class);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent1 = new Intent(getActivity(), RequisitionExpensesActivity.class);
                        startActivity(intent1);
                        break;

                    case 2:
                        Intent intent2 = new Intent(getActivity(), MapsActivity.class);
                        startActivity(intent2);
                        break;

                    case 3:
                        Intent intent3 = new Intent(getActivity(), UserProfileActivity.class);
                        startActivity(intent3);
                        break;


                }

            }
        });


    }
}
