package com.example.memorycompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public class FilterCreatorActivity extends AppCompatActivity
{
    FilterHandler filterHandler = FilterHandler.getInstance();
    AutoComplete autoComplete = new AutoComplete();
    TextView minPercentText;
    TextView maxPercentText;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_creator);

        buttonCreateSetup();
        editTextSetup();
        backButton();
        sliderSetup();
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
                        filterHandler.saveFilters();
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

    private void sliderSetup()
    {
        minPercentText = findViewById(R.id.textMinDate);
        maxPercentText = findViewById(R.id.textMaxDate);
        RangeSlider slider = findViewById(R.id.dateSlider);
        RangeSlider.OnSliderTouchListener onSlide = new RangeSlider.OnSliderTouchListener()
        {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider)
            {

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider)
            {
                List<Float> percentRange = slider.getValues();
                String minText = (percentRange.get(0).intValue()) + "%";
                String maxText = (percentRange.get(1).intValue()) + "%";
                minPercentText.setText(minText);
                maxPercentText.setText(maxText);
            }
        };
        slider.addOnSliderTouchListener(onSlide);
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
                String minDate = ((EditText)findViewById(R.id.editTextMinDate)).getText().toString().trim();
                String maxDate = ((EditText)findViewById(R.id.editTextMaxDate)).getText().toString().trim();
                int minPercent = Integer.parseInt(minPercentText.getText().toString().substring(0, minPercentText.getText().length()-1));
                int maxPercent = Integer.parseInt(maxPercentText.getText().toString().substring(0, maxPercentText.getText().length()-1));
                boolean includedAllOrAny = ((Switch)findViewById(R.id.includedAnyAll)).isChecked();
                boolean excludedAllOrAny = ((Switch)findViewById(R.id.excludedAnyAll)).isChecked();

                String[] date = {minDate,maxDate};
                int[] percent = {minPercent, maxPercent};

                System.out.println("Creating Filter");
                filterHandler.createNewFilter(title,date,percent,includeCat,excludeCat,includeKeyword,excludeKeyword, includedAllOrAny, excludedAllOrAny);

            }
        });
    }
}
