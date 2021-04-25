package com.example.memorycompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class FilterCreatorActivity extends AppCompatActivity
{
    QuestionFilterHandler filterHandler = new QuestionFilterHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_creator);

        buttonCreateSetup();
        backButton();
    }

    private void backButton()
    {
        Button buttonStart = findViewById(R.id.buttonBackFilter);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Save the changes when backing out.
                new Thread(new Runnable() {
                    public void run()
                    {
                        //questionHandler.saveQuestionNodes();
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
        Button buttonCreateFilter = findViewById(R.id.buttonSubmitFilter);
        buttonCreateFilter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                String category = ((EditText)findViewById(R.id.editTextIncludeCat)).getText().toString().trim();
                //String question = ((EditText)findViewById(R.id.editQuestion)).getText().toString().trim();
               // String answer = ((EditText)findViewById(R.id.editAnswer)).getText().toString().trim();

                int newIndex = -1;

                long[] date = {0,0};
                int[] precent = {0,0};
                System.out.println("Creating Filter");
                filterHandler.createNewFilter("title",date,precent,category,"","","");


                if(newIndex != -1)
                {
                    System.out.println("QUESTION ADDED SUCCESFULLY");
                    //QuestionNode loadedQuestion = questionHandler.getQuestionAtIndex(newIndex);
//                    System.out.println(loadedQuestion.question);
//                    System.out.println(loadedQuestion.answer);
//                    System.out.println(loadedQuestion.dateCreated);
                }
            }
        });
    }
}
