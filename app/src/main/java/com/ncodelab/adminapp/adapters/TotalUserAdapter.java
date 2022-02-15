package com.ncodelab.adminapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ncodelab.adminapp.R;
import com.ncodelab.adminapp.model.Users;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TotalUserAdapter extends RecyclerView.Adapter<TotalUserAdapter.TotalUserViewHolder> {

    Context context;
    ArrayList<Users> usersArrayList;

    public TotalUserAdapter(Context context, ArrayList<Users> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public TotalUserAdapter.TotalUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_layout,parent,false);
        return new TotalUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TotalUserAdapter.TotalUserViewHolder holder, int position) {

        Users users = usersArrayList.get(position);

        String points = " Points";

        holder.userName.setText(users.getName());
        holder.userEmail.setText(users.getEmail());
        holder.userPoints.setText(users.getUserPoints()+points);
        Date date = users.getLastLogin();


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy hh:mm");
        holder.lastLoginDate.setText(simpleDateFormat.format(date));

        if (users.getSignInBy().equals("Google")){
            holder.signInBy.setImageResource(R.drawable.google);
        }
        else {
            holder.signInBy.setImageResource(R.drawable.ic_mail);
        }


    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class TotalUserViewHolder extends RecyclerView.ViewHolder {

        TextView userName,userEmail,userPoints,lastLoginDate;
        ImageView signInBy;


        public TotalUserViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_name);
            userEmail = itemView.findViewById(R.id.user_email);
            userPoints = itemView.findViewById(R.id.user_points);
            lastLoginDate = itemView.findViewById(R.id.last_sign_in_date);

            signInBy = itemView.findViewById(R.id.sign_in_by);

        }
    }
}
