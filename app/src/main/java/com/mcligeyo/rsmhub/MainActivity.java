package com.mcligeyo.rsmhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.mcligeyo.rsmhub.adapters.GridViewAdaptar;
import com.mcligeyo.rsmhub.model.MainGriddViewMenu;

import java.util.ArrayList;
import java.util.Objects;

import hotchemi.android.rate.AppRate;

public class MainActivity extends AppCompatActivity {
    private GridView mainGridView;
    private ArrayList<MainGriddViewMenu> mainGriddViewMenus;
    private GridViewAdaptar gridViewAdaptar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setTitle("RSM Home");

        //android rate. show google rate dialog after conditions met
        AppRate.with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(3) // default 10
                .setRemindInterval(2) // default 1
                .setShowLaterButton(true) // default true 
                .setDebug(false) // default false
//                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
//                    @Override
//                    public void onClickButton(int which) {
//                        Log.d(MainActivity.class.getName(), Integer.toString(which));
//                    }
//                })
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);


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

        mainGridView = (GridView) findViewById(R.id.main_act_gridview);
        gridViewAdaptar = new GridViewAdaptar(this, mainGriddViewMenus);
        mainGridView.setAdapter(gridViewAdaptar);
        mainGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, MakeRequisitionActivity.class);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent1 = new Intent(MainActivity.this, RequisitionExpensesActivity.class);
                        startActivity(intent1);
                        break;

                    case 2:
                        Intent intent2 = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(intent2);
                        break;

                    case 3:
                        Intent intent3 = new Intent(MainActivity.this, UserProfileActivity.class);
                        startActivity(intent3);
                        break;


                }

            }
        });


    }


}
