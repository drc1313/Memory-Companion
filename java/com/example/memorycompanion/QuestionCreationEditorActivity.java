package com.example.memorycompanion;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QuestionCreationEditorActivity extends AppCompatActivity
{
    Button buttonCreateQuestion;
    EditText categoryEditText;
    EditText questionEditText;
    EditText answerEditText;
    AutoComplete autoComplete = new AutoComplete();
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                questionEditText.setText(String.join(" ", selectedQuestion.question));
            }
            answerEditText.setText(selectedQuestion.answer);
            String foundCats = "";
            for(String cat : selectedQuestion.categories)
            {
                foundCats = foundCats + cat + ", ";
            }
            categoryEditText.setText(foundCats.substring(0, foundCats.length() - 2));
        }
    }

    private void editTextSetup()
    {

        //Below sets up the auto complete for categories typed into the edit text
        categoryEditText = findViewById(R.id.editQuestionCat);
        questionEditText = findViewById(R.id.editQuestion);
        answerEditText = findViewById(R.id.editAnswer);
        ListView catAutoComplete = findViewById(R.id.autoCompleteListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        autoComplete.addAutoCatComplete(categoryEditText, catAutoComplete, adapter);

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
                        autoComplete.questionHandler.saveQuestionNodes();
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

                boolean success = autoComplete.questionHandler.replaceNodeAtIndex(category, question, answer, selectedQuestion.index);
                if(success)
                {
                    System.out.println("The Question Has Been Replaced");
                }
            }
        });
    }
}
