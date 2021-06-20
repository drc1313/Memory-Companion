package com.example.memorycompanion;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FilterManagerActivity extends AppCompatActivity
{
    FilterHandler filterHandler = FilterHandler.getInstance();

    //public static Filter selectedFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_manager);
        loadContent();
        loadFilter();
    }

    private void loadFilter()
    {
        ListView filterView =  findViewById(R.id.filterList);

        populateFilterView(filterView, null);

        //Pressing a question loads it onto an editor for it
        filterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(filterView);
                FilterHandler.selectedFilter = filterHandler.filters.get(position);
               // System.out.println(selectedFilter.question);
                //Intent activityIntent = new Intent(getApplicationContext(), QuestionCreationEditorActivity.class);
               // startActivity(activityIntent);
            }
        });
        //Long hold deletes the question.
        //TODO: Add delete confirmation
        filterView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                System.out.println(filterHandler.filters.get(position).title + " Has been Removed");
                Filter removeNode = filterHandler.filters.remove(position);
               // filterHandler.removeQuestionFromArray(removeNode.index);
                populateFilterView(filterView, filterView.onSaveInstanceState());
                return true;
            }
        });
    }
    private void populateFilterView(ListView view , Parcelable state)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        for(Filter filter : filterHandler.filters)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                adapter.add(filter.title);
            }
        }
        view.setAdapter(adapter);
        if(state != null)
        {
            view.onRestoreInstanceState(state);
        }
    }

    //Load buttons and actions
    private void loadContent()
    {
        //TODO: Only save when needed
        Button buttonStart = findViewById(R.id.buttonBack);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                filterHandler.saveFilters();
                Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(activityIntent);
            }
        });
    }
}
