package com.example.memorycompanion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionManagerActivity extends AppCompatActivity
{
    QuestionHandler questionHandler = QuestionHandler.getInstance();
    List<QuestionNode> questionsList = questionHandler.getAllQuestions();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_manager);
        loadContent();
        loadQuestions();
    }

    private void loadQuestions()
    {
        ListView questionView =  findViewById(R.id.questionList);

        populateQuestionView(questionView, null);

        questionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(questionView);

                System.out.println(questionsList.get(position).question + " Has been Removed");
                QuestionNode removeNode = questionsList.remove(position);
                questionHandler.removeQuestionFromArray(removeNode.index);
                populateQuestionView(questionView, questionView.onSaveInstanceState());
            }
        });
    }
    private void populateQuestionView(ListView view , Parcelable state)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        for(QuestionNode qn : questionsList)
        {
            adapter.add(qn.question);
        }
        view.setAdapter(adapter);
        if(state != null)
        {
            view.onRestoreInstanceState(state);
        }
    }

    private void loadContent()
    {
        Button buttonStart = findViewById(R.id.buttonBack);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Only perfroms action if 20+ slots are free.
                questionHandler.reduceArraySize();
                Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(activityIntent);
            }
        });
    }
}
