package com.ncodelab.adminapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ncodelab.adminapp.R;

public class AdsInfo extends AppCompatActivity {


    TextView tvBannerAdId,tvInterstitialAdId;
    TextInputLayout bannerAdIdInput,interstitialAdIdInput;
    MaterialButton updateBannerAdIdBtn,updateInterstitialAdIdBtn;

    FirebaseFirestore database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_info);

        database = FirebaseFirestore.getInstance();

        tvBannerAdId = findViewById(R.id.tv_banner_ad_id);
        tvInterstitialAdId = findViewById(R.id.tv_interstitial_ad_id);
        bannerAdIdInput = findViewById(R.id.banner_ad_id_input);
        interstitialAdIdInput = findViewById(R.id.interstitial_ad_id_input);

        updateBannerAdIdBtn = findViewById(R.id.update_banner_ad_id_btn);
        updateInterstitialAdIdBtn = findViewById(R.id.update_interstitial_ad_btn);

        getData();

        updateBannerAdIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBannerAdId();
            }
        });

        updateInterstitialAdIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInterstitialAdId();
            }
        });


    }
    public void getData(){

        DocumentReference documentReference = database.collection("Settings").document("Ads");

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                tvBannerAdId.setText(String.valueOf(documentSnapshot.getString("BannerAdId")));
                tvInterstitialAdId.setText(String.valueOf(documentSnapshot.getString("InterstitialAdId")));

            }
        });


    }

    public void updateBannerAdId(){

        String bannerAdId = bannerAdIdInput.getEditText().getText().toString();

            database.collection("Settings").document("Ads")
                    .update("BannerAdId",bannerAdId)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                displaySuccessfulToast("Data Updated");
                                tvBannerAdId.setText(bannerAdId);
                                bannerAdIdInput.getEditText().setText("");
                            }

                        }
                    });


    }

    public void updateInterstitialAdId(){

        String interstitialAdId = bannerAdIdInput.getEditText().getText().toString();

            database.collection("Settings").document("Ads")
                    .update("InterstitialAdId",interstitialAdId)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                displaySuccessfulToast("Data Updated");
                                tvInterstitialAdId.setText(interstitialAdId);
                                interstitialAdIdInput.getEditText().setText("");
                            }

                        }
                    });


    }

    public void displayErrorToast(String msg){
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.toast_layout,findViewById(R.id.customToast));

        MaterialCardView customToast = view.findViewById(R.id.customToast);
        TextView toastMessage = view.findViewById(R.id.toastMessage);
        ImageView icon = view.findViewById(R.id.toastIcon);

        toastMessage.setText(msg);
        icon.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_wrong));

        customToast.setCardBackgroundColor(getResources().getColor(R.color.red));

        Toast errorToast = new Toast(getApplicationContext());
        errorToast.setDuration(Toast.LENGTH_SHORT);


        errorToast.setView(view);

        errorToast.setGravity(Gravity.TOP,0,20);

        errorToast.show();
    }

    public void displaySuccessfulToast(String msg){
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.toast_layout,findViewById(R.id.customToast));

        MaterialCardView customToast = view.findViewById(R.id.customToast);
        TextView toastMessage = view.findViewById(R.id.toastMessage);
        ImageView icon = view.findViewById(R.id.toastIcon);

        toastMessage.setText(msg);
        icon.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_right));

        customToast.setCardBackgroundColor(getResources().getColor(R.color.green));

        Toast errorToast = new Toast(getApplicationContext());
        errorToast.setDuration(Toast.LENGTH_SHORT);


        errorToast.setView(view);

        errorToast.setGravity(Gravity.TOP,0,20);

        errorToast.show();
    }
}