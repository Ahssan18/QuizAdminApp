package com.ncodelab.adminapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ncodelab.adminapp.R;
import com.ncodelab.adminapp.activities.AddArticleActivity;
import com.ncodelab.adminapp.model.WithdrawRequests;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WithdrawRequestAdapter extends RecyclerView.Adapter<WithdrawRequestAdapter.WithdrawRequestViewHolder> {

    Context context;
    ArrayList<WithdrawRequests> withdrawRequestsList;

    public WithdrawRequestAdapter(Context context, ArrayList<WithdrawRequests> withdrawRequestsList) {
        this.context = context;
        this.withdrawRequestsList = withdrawRequestsList;
    }

    @NonNull
    @Override
    public WithdrawRequestAdapter.WithdrawRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.withdraw_request_layout,parent,false);

        return new WithdrawRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WithdrawRequestAdapter.WithdrawRequestViewHolder holder, int position) {

        WithdrawRequests withdrawRequests = withdrawRequestsList.get(position);

        holder.userName.setText(withdrawRequests.getName());
        holder.userPaypalId.setText(withdrawRequests.getPaypalId());
        holder.withdrawStatus.setText(withdrawRequests.getStatus());

        Date date = withdrawRequests.getRequestedDate();


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy hh:mm");
        holder.withdrawDate.setText(simpleDateFormat.format(date));

        holder.paidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore.getInstance().collection("WithdrawRequests").document(withdrawRequests.getWithdrawId())
                        .update("status","Paid")
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    holder.withdrawStatus.setText("Paid");
                                    holder.withdrawStatus.setTextColor(context.getResources().getColor(R.color.green));
                                    Log.d("FIRESTORE","Data Updated");
                                    Snackbar successSnackBar = Snackbar.make(v,"Withdraw Request Paid",Snackbar.LENGTH_SHORT);
                                    successSnackBar.setBackgroundTint(v.getContext().getResources().getColor(R.color.green));
                                    successSnackBar.setTextColor(v.getContext().getResources().getColor(R.color.white));
                                    successSnackBar.show();
                                }
                                else {
                                    Log.d("FIRESTORE","Error = "+task.getException());
                                }

                            }
                        });


            }
        });

        if (withdrawRequests.getStatus().equals("Paid")){
            holder.withdrawStatus.setTextColor(context.getResources().getColor(R.color.green));
            holder.paidBtn.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return withdrawRequestsList.size();
    }

    public class WithdrawRequestViewHolder extends RecyclerView.ViewHolder {

        TextView userName,userPaypalId,pointsToWithdraw,withdrawDate,withdrawStatus;
        MaterialButton paidBtn;

        public WithdrawRequestViewHolder(@NonNull View itemView) {
            super(itemView);


            userName = itemView.findViewById(R.id.user_name);
            userPaypalId = itemView.findViewById(R.id.user_paypal_id);
            pointsToWithdraw = itemView.findViewById(R.id.points_to_withdraw);
            withdrawDate = itemView.findViewById(R.id.withdraw_time);
            withdrawStatus = itemView.findViewById(R.id.withdraw_status);

            paidBtn = itemView.findViewById(R.id.btn_paid);

        }
    }
}
