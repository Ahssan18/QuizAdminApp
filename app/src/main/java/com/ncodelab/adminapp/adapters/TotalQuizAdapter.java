package com.ncodelab.adminapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.math.Stats;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ncodelab.adminapp.R;
import com.ncodelab.adminapp.model.QuizQuestion;

import java.util.ArrayList;

public class TotalQuizAdapter extends RecyclerView.Adapter<TotalQuizAdapter.TotalQuizViewHolder> {

    Context context;
    ArrayList<QuizQuestion> questionArrayList;
    private String TAG = "TotalQuizAdapter";
    private FirebaseFirestore db;

    public TotalQuizAdapter(Context context, ArrayList<QuizQuestion> questionArrayList) {
        this.context = context;
        this.questionArrayList = questionArrayList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public TotalQuizAdapter.TotalQuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.total_quiz_layout, parent, false);

        return new TotalQuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TotalQuizAdapter.TotalQuizViewHolder holder, int position) {

        QuizQuestion quizQuestion = questionArrayList.get(position);
        Log.e(TAG, "onBindViewHolder => " + quizQuestion.toString());
        holder.question.setText(quizQuestion.getQuestion());
        holder.optionA.setText(quizQuestion.getOptionA());
        holder.optionB.setText(quizQuestion.getOptionB());
        holder.optionC.setText(quizQuestion.getOptionC());
        holder.optionD.setText(quizQuestion.getOptionD());
        holder.answer.setText(quizQuestion.getAnswer());

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("QuizQuestions").
                        document(quizQuestion.getId())
                        .delete().
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // inside on complete method we are checking
                                // if the task is success or not.
                                if (task.isSuccessful()) {
                                    // this method is called when the task is success
                                    // after deleting we are starting our MainActivity.
                                    DocumentReference dc = db.collection("Stats").document("stats");
                                    dc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Long num = documentSnapshot.getLong("totalQuestions");
                                            num = num - 1;
                                            if (num < 0) {
                                                num = Long.valueOf(0);
                                            }
                                            db.collection("Stats").document("stats")
                                                    .update("totalQuestions", num);
                                        }
                                    });
                                    Toast.makeText(context, "Question deleted successfully!", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                } else {
                                    // if the delete operation is failed
                                    // we are displaying a toast message.
                                    Toast.makeText(context, "Fail to delete the Question. ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return questionArrayList.size();
    }

    public class TotalQuizViewHolder extends RecyclerView.ViewHolder {

        TextView question, optionA, optionB, optionC, optionD, answer;
        ImageView ivDelete;

        public TotalQuizViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.question);

            optionA = itemView.findViewById(R.id.option_a);
            optionB = itemView.findViewById(R.id.option_b);
            optionC = itemView.findViewById(R.id.option_c);
            optionD = itemView.findViewById(R.id.option_d);
            ivDelete = itemView.findViewById(R.id.iv_delete);

            answer = itemView.findViewById(R.id.answer);

        }
    }
}
