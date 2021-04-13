package com.example.memorycompanion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class QuestionManagerActivity extends AppCompatActivity
{
    QuestionHandler questionHandler = QuestionHandler.getInstance();
    List<QuestionNode> questionsList = questionHandler.getAllQuestions();
    public static QuestionNode selectedQuestion = null;

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

        //Pressing a question loads it onto an editor for it
        questionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(questionView);
                selectedQuestion = questionsList.get(position);
                System.out.println(selectedQuestion.question);
                Intent activityIntent = new Intent(getApplicationContext(), QuestionCreationEditorActivity.class);
                startActivity(activityIntent);
            }
        });
        //Long hold deletes the question.
        //TODO: Add delete confirmation
        questionView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                System.out.println(questionsList.get(position).question + " Has been Removed");
                QuestionNode removeNode = questionsList.remove(position);
                questionHandler.removeQuestionFromArray(removeNode.index);
                populateQuestionView(questionView, questionView.onSaveInstanceState());
                return true;
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
