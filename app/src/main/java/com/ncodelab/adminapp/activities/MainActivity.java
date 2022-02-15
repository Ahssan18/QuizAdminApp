package com.ncodelab.adminapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.ncodelab.adminapp.R;
import com.ncodelab.adminapp.fragments.HomeFragment;
import com.ncodelab.adminapp.fragments.SettingsFragment;
import com.ncodelab.adminapp.fragments.UserFragment;
import com.ncodelab.adminapp.fragments.WithdrawRequestFragment;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {

    AnimatedBottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomBar = findViewById(R.id.bottom_bar);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

        bottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int lastIndex, @Nullable AnimatedBottomBar.Tab lastTab, int newIndex, @NonNull AnimatedBottomBar.Tab newTab) {

                Fragment currentFragment = null;

                switch (newIndex){

                    case 0:
                        currentFragment = new HomeFragment();
                        break;
                    case 1:
                        currentFragment = new UserFragment();
                        break;
                    case 2:
                        currentFragment = new WithdrawRequestFragment();
                        break;
                    case 3:
                        currentFragment = new SettingsFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,currentFragment).commit();

            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });




    }
}