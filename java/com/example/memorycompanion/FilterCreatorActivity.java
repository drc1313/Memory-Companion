package com.example.memorycompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FilterCreatorActivity extends AppCompatActivity
{
    QuestionFilterHandler filterHandler = QuestionFilterHandler.getInstance();
    AutoComplete autoComplete = new AutoComplete();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_creator);

        buttonCreateSetup();
        editTextSetup();
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
    private void editTextSetup()
    {
        EditText includeCatEditTest = findViewById(R.id.editTextIncludeCat);
        EditText excludeCatEditTest = findViewById(R.id.editTextExcludeCat);
        ListView autoComplete1 = findViewById(R.id.autoCompleteListView1);
        ListView autoComplete2 = findViewById(R.id.autoCompleteListView2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        autoComplete.addAutoCatComplete(includeCatEditTest, autoComplete1, adapter);
        autoComplete.addAutoCatComplete(excludeCatEditTest, autoComplete2, adapter);
    }

    private void buttonCreateSetup()
    {
        Button buttonCreateFilter = findViewById(R.id.buttonSubmitFilter);
        buttonCreateFilter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                String title = ((EditText)findViewById(R.id.editTextFilterName)).getText().toString().trim();
                String includeCat = ((EditText)findViewById(R.id.editTextIncludeCat)).getText().toString().trim();
                String excludeCat = ((EditText)findViewById(R.id.editTextExcludeCat)).getText().toString().trim();
                String includeKeyword = ((EditText)findViewById(R.id.editTextQInclude)).getText().toString().trim();
                String excludeKeyword = ((EditText)findViewById(R.id.editTextQExclude)).getText().toString().trim();

                int newIndex = -1;

                long[] date = {0,0};
                int[] precent = {0,0};
                System.out.println("Creating Filter");
                filterHandler.createNewFilter(title,date,precent,includeCat,excludeCat,includeKeyword,excludeKeyword);


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
