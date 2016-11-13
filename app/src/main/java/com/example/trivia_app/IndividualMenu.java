package com.example.trivia_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class IndividualMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_menu);
    }

    public void startTrivia(View view){
        Intent intent = new Intent(getApplicationContext(), SoloQuiz.class);
        EditText text = (EditText) findViewById(R.id.answer_time);
        String answerTime = text.getText().toString();
        if(!answerTime.matches("[0-9]+") || Integer.parseInt(answerTime) > 120 || Integer.parseInt(answerTime) < 15)
            return;
        intent.putExtra("answerTime", answerTime);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {}

}
