package com.example.memorycompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QuestionCreationEditorActivity extends QuestionCreationInterfacing
{
    static QuestionNode selectedQuestion = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_creator);
        editTextSetup();
        buttonEditSetup();
        populateEditTextsWithNodeData();
        backButton();
    }

    private void populateEditTextsWithNodeData()
    {
        if (QuestionManagerActivity.selectedQuestion != null)
        {
            selectedQuestion = QuestionManagerActivity.selectedQuestion;
            questionEditText.setText(selectedQuestion.question);
            answerEditText.setText(selectedQuestion.answer);
            String foundCats = "";
            for(String cat : selectedQuestion.categories)
            {
                foundCats = foundCats + cat + ", ";
            }
            categoryEditText.setText(foundCats.substring(0, foundCats.length() - 2));
        }
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
                Intent activityIntent = new Intent(getApplicationContext(), QuestionManagerActivity.class);
                startActivity(activityIntent);
            }
        });
    }

    private void buttonEditSetup()
    {
        buttonCreateQuestion = findViewById(R.id.buttonQuestionCreate);
        buttonCreateQuestion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                String category = categoryEditText.getText().toString().trim();
                String question = questionEditText.getText().toString().trim();
                String answer   = answerEditText.getText().toString().trim();

                boolean success = questionHandler.replaceNodeAtIndex(category, question, answer, selectedQuestion.index);
                if(success)
                {
                    System.out.println("The Question Has Been Replaced");
                }
            }
        });
    }
}
