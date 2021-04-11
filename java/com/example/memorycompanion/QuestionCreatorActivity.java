package com.example.memorycompanion;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QuestionCreatorActivity extends QuestionCreationInterfacing
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_creator);
        editTextSetup();
        buttonCreateSetup();
        backButton();
    }

    private void backButton()
    {
        Button buttonStart = findViewById(R.id.buttonBack);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Save the changes when backing out.
                new Thread(new Runnable() {
                    public void run()
                    {
                        questionHandler.saveQuestionNodes();
                        System.out.println("Save Complete");
                    }
                }).start();
                Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(activityIntent);
            }
        });
    }

    private void buttonCreateSetup()
    {
        buttonCreateQuestion = findViewById(R.id.buttonQuestionCreate);
        buttonCreateQuestion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                String category = categoryEditText.getText().toString().trim();
                String question = ((EditText)findViewById(R.id.editQuestion)).getText().toString().trim();
                String answer = ((EditText)findViewById(R.id.editAnswer)).getText().toString().trim();

                int newIndex = -1;

                {
                    newIndex = questionHandler.addQuestionToArray(category, question, answer);
                }

                if(newIndex != -1)
                {
                    System.out.println("QUESTION ADDED SUCCESFULLY");
                    QuestionNode loadedQuestion = questionHandler.getQuestionAtIndex(newIndex);
                    System.out.println(loadedQuestion.question);
                    System.out.println(loadedQuestion.answer);
                    System.out.println(loadedQuestion.dateCreated);
                }
            }
        });
    }
}
