package com.ncodelab.adminapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ncodelab.adminapp.R;
import com.ncodelab.adminapp.model.QuizQuestion;

public class AddQuizQuestionActivity extends AppCompatActivity {

    TextInputLayout questionInput,optionAInput,optionBInput,optionCInput,optionDInput,answerInput;
    MaterialButton addQuestionBtn;

    long totalQuestions;

    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz_question);

        initializeViews();
        database = FirebaseFirestore.getInstance();

        addQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestion();
            }
        });


        getData();


    }
    public void initializeViews(){



        questionInput = findViewById(R.id.question_input);
        optionAInput = findViewById(R.id.option_a_input);
        optionBInput = findViewById(R.id.option_b_input);
        optionCInput = findViewById(R.id.option_c_input);
        optionDInput = findViewById(R.id.option_d_input);
        answerInput = findViewById(R.id.answer_input);
        addQuestionBtn = findViewById(R.id.add_question_btn);



    }

    private void getData(){

        DocumentReference documentReference = database.collection("Stats").document("stats");

        try {
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    totalQuestions = documentSnapshot.getLong("totalQuestions");

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void addQuestion(){

        if (questionInput.getEditText().getText().toString().equals("") &&
                optionAInput.getEditText().getText().toString().equals("") &&
                optionBInput.getEditText().getText().toString().equals("") &&
                optionCInput.getEditText().getText().toString().equals("") &&
                optionDInput.getEditText().getText().toString().equals(""))
        {
            displayErrorToast("Enter data in all fields");
        }

        String question,optionA,optionB,optionC,optionD,answer;

        question = questionInput.getEditText().getText().toString();
        optionA = optionAInput.getEditText().getText().toString();
        optionB = optionBInput.getEditText().getText().toString();
        optionC = optionCInput.getEditText().getText().toString();
        optionD = optionDInput.getEditText().getText().toString();
        answer = answerInput.getEditText().getText().toString();

        QuizQuestion quizQuestion = new QuizQuestion(question,optionA,optionB,optionC,optionD,answer,++totalQuestions);

        //Adding Question
        try {
            database.collection("QuizQuestions").document()
                    .set(quizQuestion)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                displaySuccessfulToast("Question Added");

                                database.collection("Stats").document("stats")
                                        .update("totalQuestions", FieldValue.increment(1));

                                questionInput.getEditText().setText("");
                                optionAInput.getEditText().setText("");
                                optionBInput.getEditText().setText("");
                                optionCInput.getEditText().setText("");
                                optionDInput.getEditText().setText("");
                                answerInput.getEditText().setText("");

                            }
                            else {
                                Log.d("FIRESTORE_ERROR","DATABASE_ERROR = "+task.getException().toString());
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
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