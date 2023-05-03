package com.mcligeyo.rsmhub;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mcligeyo.rsmhub.fragment.HomeFragment;
import com.mcligeyo.rsmhub.fragment.MakeRequisitionFragment;
import com.mcligeyo.rsmhub.fragment.SearchFragment;
import com.mcligeyo.rsmhub.fragment.UserProfileFragment;
import com.mcligeyo.rsmhub.fragment.ViewRequisitionFragment;

public class UserProfileActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        bottomNav = findViewById(R.id.bottomBarBottomNavView);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavListener);


        //dispay userprofilefrag when useprofactivity loads
        getSupportFragmentManager().beginTransaction().replace(R.id.userProfFrame, new UserProfileFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.bar_home:
                    selectedFragment = new HomeFragment();
                    break;

                case R.id.bar_add_circle:
                    selectedFragment = new MakeRequisitionFragment();
                    break;

                case R.id.bar_view:
                    selectedFragment = new ViewRequisitionFragment();
                    break;

                case R.id.bar_search:
                    selectedFragment = new SearchFragment();
                    break;

            }

            getSupportFragmentManager().beginTransaction().replace(R.id.userProfFrame, selectedFragment).commit();
            return true;  //enables items to be clicked, if falseitems not clickable but viewabe onl
        }
    };


}
