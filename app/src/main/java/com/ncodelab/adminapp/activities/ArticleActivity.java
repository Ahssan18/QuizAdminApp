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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ncodelab.adminapp.R;
import com.ncodelab.adminapp.adapters.ArticleAdapter;
import com.ncodelab.adminapp.adapters.TotalQuizAdapter;
import com.ncodelab.adminapp.model.Article;
import com.ncodelab.adminapp.model.QuizQuestion;

import java.util.ArrayList;

public class ArticleActivity extends AppCompatActivity {

    RecyclerView articleRecyclerview;
    ArticleAdapter articleAdapter;
    ArrayList<Article> articleList;

    FirebaseFirestore database;

    MaterialButton addArticleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        database = FirebaseFirestore.getInstance();

        articleRecyclerview = findViewById(R.id.article_recyclerview);
        addArticleBtn = findViewById(R.id.add_article_btn);

        addArticleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ArticleActivity.this,AddArticleActivity.class));
            }
        });

        setArticleRecyclerview();
        DataChangeListener();


    }
    public void setArticleRecyclerview(){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ArticleActivity.this,LinearLayoutManager.VERTICAL,false);
        articleRecyclerview.setLayoutManager(layoutManager);

        articleList = new ArrayList<Article>();

        articleAdapter = new ArticleAdapter(ArticleActivity.this,articleList);


        articleRecyclerview.setAdapter(articleAdapter);

    }
    public void DataChangeListener(){

        database.collection("Articles")
                .orderBy("uploadTime", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null){
                            Log.d("firestoreError",error.getMessage());
                        }
                        else {
                            for (DocumentChange dc: value.getDocumentChanges()){

                                if (dc.getType() == DocumentChange.Type.ADDED){

                                    articleList.add(dc.getDocument().toObject(Article.class));
                                }
                                if (dc.getType() == DocumentChange.Type.REMOVED){

                                    articleList.remove(dc.getDocument().toObject(Article.class));
                                }
                                articleAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                });

    }
}