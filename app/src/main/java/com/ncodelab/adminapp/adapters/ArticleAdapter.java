package com.ncodelab.adminapp.adapters;

import static android.view.View.GONE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ncodelab.adminapp.R;
import com.ncodelab.adminapp.activities.AddArticleActivity;
import com.ncodelab.adminapp.model.Article;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    Context context;
    ArrayList<Article> articleArrayList;

    public ArticleAdapter(Context context, ArrayList<Article> articleArrayList) {
        this.context = context;
        this.articleArrayList = articleArrayList;
    }

    @NonNull
    @Override
    public ArticleAdapter.ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_card_layout,parent,false);

        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleAdapter.ArticleViewHolder holder, int position) {

        Article article = articleArrayList.get(position);

        String now = "Now";
        String a = "";

        holder.articleTitle.setText(article.getArticleTitle());
        holder.articleDescription.setText(article.getArticleDescription());
        holder.urlToRedirect.setText(article.getUrl());
        if (article.getArticleImageUrl() != null){
            Picasso.get().load(article.getArticleImageUrl()).into(holder.articleImage);
        }
        else {
            holder.articleImage.setVisibility(GONE);
        }

        Date date = article.getUploadTime();

        if (date == null){
            holder.dateUploaded.setText(now);
        }
        else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy hh:mm");

            holder.dateUploaded.setText(a+simpleDateFormat.format(date));
        }



        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogue = new AlertDialog.Builder(v.getContext());
                dialogue.setTitle("Are sure you want to delete this article");

                dialogue.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseFirestore.getInstance().collection("Articles")
                                .document(article.getArticleId())
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){
                                            holder.articleCard.setVisibility(GONE);

                                            FirebaseFirestore.getInstance().collection("Stats").document("stats")
                                                    .update("totalArticle", FieldValue.increment(-1));

                                        }
                                        else {
                                            Log.d("DATABASE","ERROR = "+task.getException());
                                        }

                                    }
                                });

                    }
                });

                dialogue.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialogue.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView articleTitle,articleDescription,urlToRedirect,dateUploaded;
        ImageView articleImage;
        ImageView deleteBtn;
        MaterialCardView articleCard;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);

            articleTitle = itemView.findViewById(R.id.article_title);
            articleDescription = itemView.findViewById(R.id.article_description);
            urlToRedirect = itemView.findViewById(R.id.url_to_redirect);
            dateUploaded = itemView.findViewById(R.id.date_uploaded);

            articleImage = itemView.findViewById(R.id.article_image);

            deleteBtn = itemView.findViewById(R.id.btn_delete);

            articleCard = itemView.findViewById(R.id.article_card);



        }
    }
}
