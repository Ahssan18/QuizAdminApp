package com.ncodelab.adminapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ncodelab.adminapp.R;
import com.ncodelab.adminapp.adapters.TotalQuizAdapter;
import com.ncodelab.adminapp.model.QuizQuestion;

import java.util.ArrayList;

public class TotalQuizQuestionsActivity extends AppCompatActivity {

    RecyclerView quizQuestionRecyclerview;
    ArrayList<QuizQuestion> quizQuestionArrayList;
    TotalQuizAdapter totalQuizAdapter;

    FirebaseFirestore database;

    MaterialButton addQuizBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_quiz_questions);

        database = FirebaseFirestore.getInstance();

        quizQuestionRecyclerview = findViewById(R.id.quiz_questions_recyclerview);
        addQuizBtn = findViewById(R.id.add_article_btn);

        setArticleRecyclerview();
        DataChangeListener();

        addQuizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TotalQuizQuestionsActivity.this,AddQuizQuestionActivity.class));
            }
        });

    }

    public void setArticleRecyclerview(){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TotalQuizQuestionsActivity.this,LinearLayoutManager.VERTICAL,false);
        quizQuestionRecyclerview.setLayoutManager(layoutManager);

        quizQuestionArrayList = new ArrayList<QuizQuestion>();

        totalQuizAdapter = new TotalQuizAdapter(TotalQuizQuestionsActivity.this,quizQuestionArrayList);


        quizQuestionRecyclerview.setAdapter(totalQuizAdapter);

    }
    public void DataChangeListener(){

        database.collection("QuizQuestions")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null){
                            Log.d("firestoreError",error.getMessage());
                        }
                        else {
                            for (DocumentChange dc: value.getDocumentChanges()){

                                if (dc.getType() == DocumentChange.Type.ADDED){

                                    quizQuestionArrayList.add(dc.getDocument().toObject(QuizQuestion.class));
                                }
                                if (dc.getType() == DocumentChange.Type.REMOVED){

                                    quizQuestionArrayList.remove(dc.getDocument().toObject(QuizQuestion.class));
                                }
                                totalQuizAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                });

    }

}