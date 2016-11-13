package com.example.trivia_app;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class SoloQuiz extends AppCompatActivity {

    String answerTime;
    int correctAnswer = 1;
    boolean answered = false;
    String answers[] = new String[4];
    int numberOfProblems;
    int questionNum = 0;

    CountDownTimer timeCounter;
    CountDownTimer initialWait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_solo_quiz);

        Intent intent = getIntent();
        answerTime = intent.getStringExtra("answerTime");

        ProgressBar pb = (ProgressBar) findViewById(R.id.time_bar);
        pb.setMax(Integer.parseInt(answerTime));
        pb.setProgressBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        pb.setProgressTintList(ColorStateList.valueOf(Color.rgb(26, 198, 255)));

        getNumOfProblems();
        addAnswers();
        initializeQuestion();

        initialWait = new CountDownTimer(7000,1000) {
            public void onTick(long millisUntilFinished) {
                buttonsText(Integer.toString(Math.round(millisUntilFinished/1000)));
            }
            public void onFinish() {
                initializeAnswers();
                buttonsTextColor(Color.BLACK);
                startTime();
            }
        }.start();

    }

    public void getNumOfProblems(){
        try {
            String currentLine;
            BufferedReader br = new BufferedReader(new FileReader(MainActivity.questions));
            int i=0;
            while((currentLine=br.readLine())!=null){
                numberOfProblems = i;
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAnswers(){
        //int questionNum = AppPreferences.getAnswered(SoloQuiz.this);
        Random rand = new Random();
        questionNum = rand.nextInt(numberOfProblems);
        try {
            String currentLine;
            BufferedReader br = new BufferedReader(new FileReader(MainActivity.questions));
            int i=0;
            while((currentLine=br.readLine())!=null){
                if(i==questionNum) {
                    String info[] = currentLine.split("_");
                    if(info[1].startsWith("("))
                        answers[0] = info[1].substring(3);
                    else{
                        answers[0] = info[1].substring(4);
                        correctAnswer = 0;
                    }
                    if(info[2].startsWith("("))
                        answers[1] = info[2].substring(3);
                    else{
                        answers[1] = info[2].substring(4);
                        correctAnswer = 1;
                    }
                    if(info[3].startsWith("("))
                        answers[2] = info[3].substring(3);
                    else{
                        answers[2] = info[3].substring(4);
                        correctAnswer = 2;
                    }
                    if(info[4].startsWith("("))
                        answers[3] = info[4].substring(3);
                    else{
                        answers[3] = info[4].substring(4);
                        correctAnswer = 3;
                    }
                    break;
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeAnswers(){
        Button a = (Button) findViewById(R.id.A);
        a.setText(answers[0]);
        Button b = (Button) findViewById(R.id.B);
        b.setText(answers[1]);
        Button c = (Button) findViewById(R.id.C);
        c.setText(answers[2]);
        Button d = (Button) findViewById(R.id.D);
        d.setText(answers[3]);
    }

    public void initializeQuestion(){
        //int questionNum = AppPreferences.getAnswered(SoloQuiz.this);
        try {
            String currentLine;
            BufferedReader br = new BufferedReader(new FileReader(MainActivity.questions));
            int i=0;
            while((currentLine=br.readLine())!=null){
                if(i==questionNum) {
                    String info[] = currentLine.split("_");
                    TextView q = (TextView) findViewById(R.id.Q);
                    q.setText(info[0]);
                    break;
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buttonsText(String str){
        Button a = (Button) findViewById(R.id.A);
        a.setText(str);
        Button b = (Button) findViewById(R.id.B);
        b.setText(str);
        Button c = (Button) findViewById(R.id.C);
        c.setText(str);
        Button d = (Button) findViewById(R.id.D);
        d.setText(str);
    }

    public void buttonsTextColor(int color){
        Button a = (Button) findViewById(R.id.A);
        a.setTextColor(color);
        Button b = (Button) findViewById(R.id.B);
        b.setTextColor(color);
        Button c = (Button) findViewById(R.id.C);
        c.setTextColor(color);
        Button d = (Button) findViewById(R.id.D);
        d.setTextColor(color);
    }

    public void startTime(){
        timeCounter = new CountDownTimer(Integer.parseInt(answerTime)*1000+1000,1000){
            ProgressBar pb = (ProgressBar) findViewById(R.id.time_bar);
            public void onTick(long millisUntilFinished) {
                pb.setProgress(pb.getProgress()+1);
            }
            public void onFinish() {
                if(!answered){
                    showWrong(-1);
                }
            }
        }.start();
    }

    public void chooseA(View view){processAnswer(0);}
    public void chooseB(View view){processAnswer(1);}
    public void chooseC(View view){processAnswer(2);}
    public void chooseD(View view){processAnswer(3);}

    public void processAnswer(int answer){
        if(!answered) {
            try {
                timeCounter.cancel();
                answered = true;
                AppPreferences.saveAnswered(SoloQuiz.this, AppPreferences.getAnswered(SoloQuiz.this)+1);
                if (answer == correctAnswer) {
                    showRight();
                } else {
                    showWrong(answer);
                }
                showNextButton();
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
    }

    public void showNextButton(){
        Button next = new Button(this);
        next.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        next.setTextAlignment(LinearLayout.TEXT_ALIGNMENT_CENTER);
        next.setText("Next Question");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion(v);
            }
        });
        LinearLayout layout = (LinearLayout) findViewById(R.id.solo_quiz);
        layout.addView(next);
    }

    public void nextQuestion(View v){
        Intent i = new Intent(getApplicationContext(), SoloQuiz.class);
        i.putExtra("answerTime", answerTime);
        startActivity(i);
    }

    public void showWrong(int chosen){
        switch(chosen){
            case -1:{
                showWrong(0);
                showWrong(1);
                showWrong(2);
                showWrong(3);
                showRight();
                break;
            }
            case 0:{
                Button b = (Button) findViewById(R.id.A);
                b.setBackgroundColor(Color.RED);
                showRight();
                break;
            }
            case 1:{
                Button b = (Button) findViewById(R.id.B);
                b.setBackgroundColor(Color.RED);
                showRight();
                break;
            }
            case 2:{
                Button b = (Button) findViewById(R.id.C);
                b.setBackgroundColor(Color.RED);
                showRight();
                break;
            }
            case 3:{
                Button b = (Button) findViewById(R.id.D);
                b.setBackgroundColor(Color.RED);
                showRight();
                break;
            }
        }
    }

    public void showRight(){
        switch(correctAnswer){
            case 0:{
                Button b = (Button) findViewById(R.id.A);
                b.setBackgroundColor(Color.GREEN);
                break;
            }
            case 1:{
                Button b = (Button) findViewById(R.id.B);
                b.setBackgroundColor(Color.GREEN);
                break;
            }
            case 2:{
                Button b = (Button) findViewById(R.id.C);
                b.setBackgroundColor(Color.GREEN);
                break;
            }
            case 3:{
                Button b = (Button) findViewById(R.id.D);
                b.setBackgroundColor(Color.GREEN);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {}

}
