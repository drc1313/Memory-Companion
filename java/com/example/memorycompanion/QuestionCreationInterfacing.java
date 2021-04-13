package com.example.memorycompanion;

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

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class QuestionCreationInterfacing extends AppCompatActivity
{
    QuestionHandler questionHandler = QuestionHandler.getInstance();
    Button buttonCreateQuestion;
    EditText categoryEditText;
    EditText questionEditText;
    EditText answerEditText;

    protected void editTextSetup()
    {
        //Below sets up the auto complete for categories typed into the edit text
        categoryEditText = findViewById(R.id.editQuestionCat);
        questionEditText = findViewById(R.id.editQuestion);
        answerEditText = findViewById(R.id.editAnswer);
        ListView catAutoComplete = findViewById(R.id.autoCompleteListView);
        catAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] catsListed = categoryEditText.getText().toString().split(",");
                StringBuilder newCatString = new StringBuilder();
                for(int i = 0; i < catsListed.length - 1; i++)
                {
                    newCatString.append(catsListed[i]).append(", ");
                }
                newCatString.append(((TextView)view).getText().toString());
                categoryEditText.setText(newCatString.toString());
                categoryEditText.setSelection(newCatString.toString().length());//placing cursor at the end of the text

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        categoryEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String[] catsListed = categoryEditText.getText().toString().split(",");
                if(catsListed.length > 0)
                {
                    String catString = catsListed[catsListed.length - 1].trim();
                    adapter.clear();
                    if(questionHandler.knownCategories.size() > 0 && catString.length()  > 2)
                    {
                        for(String knownCat : questionHandler.knownCategories)
                        {
                            if(catString.length() < knownCat.length() && knownCat.substring(0, catString.length()).toLowerCase().equals(catString.toLowerCase()))
                                adapter.add(knownCat);
                        }
                        if(categoryEditText.hasFocus() && adapter.getCount() > 0)
                        {
                            catAutoComplete.setAdapter(adapter);
                            if(adapter.getCount() > 4)
                            {
                                catAutoComplete.getLayoutParams().height = 600;
                            }
                            else
                            {
                                catAutoComplete.getLayoutParams().height = WRAP_CONTENT;
                            }

                            catAutoComplete.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            catAutoComplete.setVisibility(View.GONE);
                        }
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
                if(!hasFocus)
                {
                    catAutoComplete.setVisibility(View.GONE);
                }
            }
        });
    }

}
