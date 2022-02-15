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

public class ReferralInfoActivity extends AppCompatActivity {

    TextView tvReferrerBonus,tvReferralGetterBonus;
    TextInputLayout referrerBonusInput,referralGetterBonusInput;
    MaterialButton updateInfoBtn;

    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_info);

        database = FirebaseFirestore.getInstance();

        tvReferrerBonus = findViewById(R.id.tv_referrer_bonus);
        tvReferralGetterBonus = findViewById(R.id.tv_referral_getter_bonus);
        referrerBonusInput = findViewById(R.id.referrer_bonus_input);
        referralGetterBonusInput = findViewById(R.id.referral_getter_bonus_input);
        updateInfoBtn = findViewById(R.id.update_referral_info_btn);

        getData();

        updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo();
            }
        });

    }
    public void getData(){

        DocumentReference documentReference = database.collection("Settings").document("referralInfo");

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                tvReferrerBonus.setText(String.valueOf(documentSnapshot.getLong("referrerBonus")));
                tvReferralGetterBonus.setText(String.valueOf(documentSnapshot.getLong("referralGetterBonus")));

            }
        });


    }

    private void updateInfo(){

        String referrerBonus,referralGetterBonus;

        referrerBonus = referrerBonusInput.getEditText().getText().toString();
        referralGetterBonus = referralGetterBonusInput.getEditText().getText().toString();

        if (referrerBonusInput.getEditText().getText().toString().equals("") &&
                referralGetterBonusInput.getEditText().getText().toString().equals(""))
        {
            displayErrorToast("Enter Data to update");
        }
        if (referrerBonusInput.getEditText().getText().toString().equals("")){

            database.collection("Settings").document("referralInfo")
                    .update("referralGetterBonus",Long.parseLong(referralGetterBonus))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                displaySuccessfulToast("Details Updated");
                                tvReferralGetterBonus.setText(referralGetterBonus);
                                referralGetterBonusInput.getEditText().setText("");
                            }
                        }
                    });

        }

        if (referralGetterBonusInput.getEditText().getText().toString().equals("")){
            database.collection("Settings").document("referralInfo")
                    .update("referrerBonus",Long.parseLong(referrerBonus))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                displaySuccessfulToast("Details Updated");
                                tvReferrerBonus.setText(referrerBonus);
                                referrerBonusInput.getEditText().setText("");
                            }

                        }
                    });
        }

        else {
            database.collection("Settings").document("referralInfo")
                    .update("referralGetterBonus",Long.parseLong(referralGetterBonus),"referrerBonus",Long.parseLong(referrerBonus))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                displaySuccessfulToast("Details Updated");
                                tvReferralGetterBonus.setText(referralGetterBonus);
                                tvReferrerBonus.setText(referrerBonus);
                                referralGetterBonusInput.getEditText().setText("");
                                referrerBonusInput.getEditText().setText("");
                            }

                        }
                    });
        }


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