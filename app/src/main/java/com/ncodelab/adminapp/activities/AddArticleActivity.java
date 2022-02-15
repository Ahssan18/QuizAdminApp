package com.ncodelab.adminapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.ncodelab.adminapp.R;
import com.ncodelab.adminapp.model.Article;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.Random;

public class AddArticleActivity extends AppCompatActivity {

    TextInputLayout articleTitleInput,articleDescriptionInput,urlInput;
    ImageView articleImage;
    MaterialButton uploadArticleImageBtn,uploadArticleBtn;

    int IMAGE_PICK_CODE = 1;
    Uri imgPath;
    Bitmap bitmap;
    View view;
    String imageUrl;

    long totalArticles;

    public final String TAG = "ARTICLE_ACTIVITY";

    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        initializeViews();

        view = findViewById(android.R.id.content);

        database = FirebaseFirestore.getInstance();

        getData();

        articleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(AddArticleActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Image to upload"), IMAGE_PICK_CODE);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        uploadArticleImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageToFirebase();
            }
        });

        uploadArticleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArticle();
            }
        });

    }
    public void initializeViews(){
        articleTitleInput = findViewById(R.id.article_title_input);
        articleDescriptionInput = findViewById(R.id.article_description_input);
        urlInput = findViewById(R.id.url_input);

        articleImage = findViewById(R.id.article_image);

        uploadArticleImageBtn = findViewById(R.id.upload_article_image_btn);
        uploadArticleBtn = findViewById(R.id.upload_article_btn);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK){
            imgPath = data.getData();
            Picasso.get().load(imgPath).into(articleImage);

        }

        try {
            InputStream inputStream = getContentResolver().openInputStream(imgPath);
            bitmap = BitmapFactory.decodeStream(inputStream);
        }
        catch (Exception e){

        }

        super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImageToFirebase(){

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        StorageReference storageReference = firebaseStorage.getReference("Article Images")
                .child(articleTitleInput.getEditText().getText().toString());

        if (imgPath.equals(null)){
            Snackbar errorSnackBar = Snackbar.make(view,"Upload image",Snackbar.LENGTH_SHORT);
            errorSnackBar.setBackgroundTint(AddArticleActivity.this.getResources().getColor(R.color.red));
            errorSnackBar.setTextColor(AddArticleActivity.this.getResources().getColor(R.color.white));
            errorSnackBar.show();
            return;
        }

        storageReference.putFile(imgPath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Snackbar successfulSnackbar = Snackbar.make(view,"Image Uploaded",Snackbar.LENGTH_SHORT);
                successfulSnackbar.setBackgroundTint(AddArticleActivity.this.getResources().getColor(R.color.green));
                successfulSnackbar.setTextColor(AddArticleActivity.this.getResources().getColor(R.color.white));
                successfulSnackbar.show();

                //Getting Image download Url
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        imageUrl = uri.toString();

                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                Snackbar progressSnackbar = Snackbar.make(view,"Uploading Image",Snackbar.LENGTH_LONG);
                progressSnackbar.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Snackbar errorSnackBar = Snackbar.make(view,"Some Error Occurred in uploading Image",Snackbar.LENGTH_SHORT);
                errorSnackBar.setBackgroundTint(AddArticleActivity.this.getResources().getColor(R.color.red));
                Log.d("STORE_ERR",e.toString());
                errorSnackBar.setTextColor(AddArticleActivity.this.getResources().getColor(R.color.white));
                errorSnackBar.show();

            }
        });



    }

    public void addArticle(){

        if (articleTitleInput.getEditText().getText().toString().equals("")){
            Snackbar errorSnackBar = Snackbar.make(view,"Enter article title",Snackbar.LENGTH_SHORT);
            errorSnackBar.setBackgroundTint(AddArticleActivity.this.getResources().getColor(R.color.red));
            errorSnackBar.setTextColor(AddArticleActivity.this.getResources().getColor(R.color.white));
            errorSnackBar.show();
            return;
        }
        if (urlInput.getEditText().getText().toString().equals("")){
            Snackbar errorSnackBar = Snackbar.make(view,"Enter article redirect url",Snackbar.LENGTH_SHORT);
            errorSnackBar.setBackgroundTint(AddArticleActivity.this.getResources().getColor(R.color.red));
            errorSnackBar.setTextColor(AddArticleActivity.this.getResources().getColor(R.color.white));
            errorSnackBar.show();
            return;
        }

        String articleTitle,articleDescription,urlToRedirect;

        articleTitle = articleTitleInput.getEditText().getText().toString();
        articleDescription = articleDescriptionInput.getEditText().getText().toString();
        urlToRedirect = urlInput.getEditText().getText().toString();

        long addArticle = ++totalArticles;

        Random random = new Random();

        int randomInt = random.nextInt(9);

        String articleId = "ARTICLE"+addArticle+"A"+randomInt;

        Article article = new Article(articleTitle,imageUrl,articleDescription,urlToRedirect,articleId);

        database.collection("Articles").document(articleId)
                .set(article)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            displaySuccessfulToast("Article Uploaded");
                            articleTitleInput.getEditText().setText("");
                            articleDescriptionInput.getEditText().setText("");
                            urlInput.getEditText().setText("");

                            //Updating database
                            database.collection("Stats").document("stats")
                                    .update("totalArticle", FieldValue.increment(1));
                        }
                        else {
                            Log.d(TAG,"ERROR = "+task.getException());
                        }

                    }
                });



    }
    public void getData(){

        DocumentReference documentReference = database.collection("Stats").document("stats");

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                totalArticles = documentSnapshot.getLong("totalArticle");
            }
        });


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