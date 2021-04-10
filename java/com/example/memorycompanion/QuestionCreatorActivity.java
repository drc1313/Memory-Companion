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

public class QuestionCreatorActivity extends AppCompatActivity
{
    QuestionHandler questionHandler = QuestionHandler.getInstance();
    Button buttonCreateQuestion;
    EditText categoryEditText;

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

    private void editTextSetup()
    {
        //Below sets up the auto complete for categories typed into the edit text
        categoryEditText = findViewById(R.id.editQuestionCat);
        ListView catAutoComplete = findViewById(R.id.autoCompleteListView);
        catAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoryEditText.setText(((TextView)view).getText());
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        categoryEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String catString = categoryEditText.getText().toString();
                adapter.clear();
                if(questionHandler.knownCategories.size() > 0 && catString.length()  > 2)
                {
                    for(String knownCat : questionHandler.knownCategories)
                    {
                        if(catString.length() < knownCat.length() && knownCat.substring(0, catString.length()).toLowerCase().equals(catString.toLowerCase()))
                        adapter.add(knownCat);
                    }
                    if(adapter.getCount() > 0)
                    {
                        catAutoComplete.setAdapter(adapter);
                        catAutoComplete.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        //Remove the auto complete list on edit box leave
        categoryEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                catAutoComplete.setVisibility(View.GONE);
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
