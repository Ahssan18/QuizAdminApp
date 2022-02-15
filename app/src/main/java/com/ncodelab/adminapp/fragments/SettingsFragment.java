package com.ncodelab.adminapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ncodelab.adminapp.R;
import com.ncodelab.adminapp.activities.AdsInfo;
import com.ncodelab.adminapp.activities.ReferralInfoActivity;
import com.ncodelab.adminapp.activities.WithdrawInfoActivity;


public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    MaterialButton withdrawInfoBtn,referralInfoBtn,adsInfoBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);

        withdrawInfoBtn = view.findViewById(R.id.withdraw_info_btn);
        referralInfoBtn = view.findViewById(R.id.referral_info_btn);
        adsInfoBtn = view.findViewById(R.id.ads_info_btn);

        withdrawInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), WithdrawInfoActivity.class));
            }
        });

        referralInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ReferralInfoActivity.class));
            }
        });

        adsInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdsInfo.class));
            }
        });

        return view;
    }

}