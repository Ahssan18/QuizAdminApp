package com.ncodelab.adminapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ncodelab.adminapp.R;
import com.ncodelab.adminapp.model.QuizQuestion;

import java.util.ArrayList;

public class TotalQuizAdapter extends RecyclerView.Adapter<TotalQuizAdapter.TotalQuizViewHolder> {

    Context context;
    ArrayList<QuizQuestion> questionArrayList;

    public TotalQuizAdapter(Context context, ArrayList<QuizQuestion> questionArrayList) {
        this.context = context;
        this.questionArrayList = questionArrayList;
    }

    @NonNull
    @Override
    public TotalQuizAdapter.TotalQuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.total_quiz_layout,parent,false);

        return new TotalQuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TotalQuizAdapter.TotalQuizViewHolder holder, int position) {

        QuizQuestion quizQuestion = questionArrayList.get(position);

        holder.question.setText(quizQuestion.getQuestion());

        holder.optionA.setText(quizQuestion.getOptionA());
        holder.optionB.setText(quizQuestion.getOptionB());
        holder.optionC.setText(quizQuestion.getOptionC());
        holder.optionD.setText(quizQuestion.getOptionD());

        holder.answer.setText(quizQuestion.getAnswer());


    }

    @Override
    public int getItemCount() {
        return questionArrayList.size();
    }

    public class TotalQuizViewHolder extends RecyclerView.ViewHolder {

        TextView question,optionA,optionB,optionC,optionD,answer;

        public TotalQuizViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.question);

            optionA = itemView.findViewById(R.id.option_a);
            optionB = itemView.findViewById(R.id.option_b);
            optionC = itemView.findViewById(R.id.option_c);
            optionD = itemView.findViewById(R.id.option_d);

            answer = itemView.findViewById(R.id.answer);

        }
    }
}
