package com.example.trivia_app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    boolean checked = false;
    static public File questions = null;
    static public FirebaseAuth mAuth;
    static public FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                }
                // ...
            }
        };
        setupQuestions();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void setupQuestions(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://quiz-bowl-e9002.appspot.com");
        StorageReference qs = storageRef.child("questions.txt");

        try {
            questions = File.createTempFile("questions", "txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        qs.getFile(questions).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public void signUpPage(View view){
        Intent i = new Intent(getApplicationContext(), SignUp.class);
        startActivity(i);
    }

    public void signInPage(View view){
        Intent i = new Intent(getApplicationContext(), SignIn.class);
        startActivity(i);
    }

    public void createIndividual(View view){
        Intent i = new Intent(getApplicationContext(), IndividualMenu.class);
        startActivity(i);
    }

    public void createGroup(View view){
        Intent i = new Intent(getApplicationContext(), Hoster_Menu.class);
        startActivity(i);
    }

    public void joinGroup(View view){

    }

    @Override
    public void onBackPressed() {}

}
