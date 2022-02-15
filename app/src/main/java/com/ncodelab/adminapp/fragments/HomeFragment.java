package com.ncodelab.adminapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ncodelab.adminapp.R;
import com.ncodelab.adminapp.activities.ArticleActivity;
import com.ncodelab.adminapp.activities.TotalQuizQuestionsActivity;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    TextView totalUser,totalQuizQuestions,totalWithdrawRequests;
    MaterialButton quizBtn,articleBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        totalUser = view.findViewById(R.id.total_users);
        totalQuizQuestions = view.findViewById(R.id.total_quiz_questions);
        totalWithdrawRequests = view.findViewById(R.id.total_withdraw_requests);

        quizBtn = view.findViewById(R.id.quiz_question_btn);
        articleBtn = view.findViewById(R.id.article_btn);


        getData();

        quizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), TotalQuizQuestionsActivity.class));
            }
        });

        articleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ArticleActivity.class));
            }
        });





        return view;

    }
    private void getData(){

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        DocumentReference dc = database.collection("Stats").document("stats");

        dc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                totalUser.setText(String.valueOf(documentSnapshot.getLong("totalUsers")));
                totalQuizQuestions.setText(String.valueOf(documentSnapshot.getLong("totalQuestions")));
                totalWithdrawRequests.setText(String.valueOf(documentSnapshot.getLong("withdrawRequests")));

            }
        });

    }

}