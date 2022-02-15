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

public class WithdrawInfoActivity extends AppCompatActivity {

    TextView tvMinimumWithdrawLimit,tvPointsConversion;
    TextInputLayout minimumWithdrawLimitInput,pointsConversionInput;
    MaterialButton updateInfoBtn;

    FirebaseFirestore database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_info);

        database = FirebaseFirestore.getInstance();

        tvMinimumWithdrawLimit = findViewById(R.id.tv_minimum_withdraw_limit);
        tvPointsConversion = findViewById(R.id.tv_points_conversion);
        minimumWithdrawLimitInput = findViewById(R.id.minimum_withdraw_limit_input);
        pointsConversionInput = findViewById(R.id.points_conversion_input);
        updateInfoBtn = findViewById(R.id.update_withdraw_info_btn);

        getData();

        updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo();
            }
        });


    }
    public void getData(){

        DocumentReference documentReference = database.collection("Settings").document("withdrawInfo");

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                tvMinimumWithdrawLimit.setText(String.valueOf(documentSnapshot.getLong("minimumWithdrawLimit")));
                tvPointsConversion.setText(String.valueOf(documentSnapshot.getLong("pointsConversion")));

            }
        });


    }

    private void updateInfo(){

        String pointsConversion,minimumWithdrawLimit;

        pointsConversion = pointsConversionInput.getEditText().getText().toString();
        minimumWithdrawLimit = minimumWithdrawLimitInput.getEditText().getText().toString();

        if (minimumWithdrawLimitInput.getEditText().getText().toString().equals("") &&
                pointsConversionInput.getEditText().getText().toString().equals(""))
        {
                displayErrorToast("Enter Data to update");
        }
        if (minimumWithdrawLimitInput.getEditText().getText().toString().equals("")){

            database.collection("Settings").document("withdrawInfo")
                    .update("pointsConversion",Long.parseLong(pointsConversion))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                displaySuccessfulToast("Details Updated");
                                tvPointsConversion.setText(pointsConversion);
                                pointsConversionInput.getEditText().setText("");
                            }
                        }
                    });

        }

        if (pointsConversionInput.getEditText().getText().toString().equals("")){
            database.collection("Settings").document("withdrawInfo")
                    .update("minimumWithdrawLimit",Long.parseLong(minimumWithdrawLimit))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                displaySuccessfulToast("Details Updated");
                                tvMinimumWithdrawLimit.setText(minimumWithdrawLimit);
                                minimumWithdrawLimitInput.getEditText().setText("");
                            }

                        }
                    });
        }

        else {
            database.collection("Settings").document("withdrawInfo")
                    .update("minimumWithdrawLimit",Long.parseLong(minimumWithdrawLimit),"pointsConversion",Long.parseLong(pointsConversion))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                displaySuccessfulToast("Details Updated");
                                tvMinimumWithdrawLimit.setText(minimumWithdrawLimit);
                                tvPointsConversion.setText(pointsConversion);
                                minimumWithdrawLimitInput.getEditText().setText("");
                                pointsConversionInput.getEditText().setText("");
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