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

public class AutoComplete extends AppCompatActivity
{
    QuestionHandler questionHandler = QuestionHandler.getInstance();

    public void addAutoCatComplete(EditText editTextCat, ListView autoCompleteList, ArrayAdapter<String> adapter)
    {
        autoCompleteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] catsListed = editTextCat.getText().toString().split(",");
                StringBuilder newCatString = new StringBuilder();
                for(int i = 0; i < catsListed.length - 1; i++)
                {
                    newCatString.append(catsListed[i].trim()).append(", ");
                }
                newCatString.append(((TextView)view).getText().toString());
                editTextCat.setText(newCatString.toString());
                editTextCat.setSelection(newCatString.toString().length());//placing cursor at the end of the text

            }
        });

        editTextCat.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String[] catsListed = editTextCat.getText().toString().split(",");
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
                        if(editTextCat.hasFocus() && adapter.getCount() > 0)
                        {
                            autoCompleteList.setAdapter(adapter);
                            if(adapter.getCount() > 4)
                            {
                                autoCompleteList.getLayoutParams().height = 600;
                            }
                            else
                            {
                                autoCompleteList.getLayoutParams().height = WRAP_CONTENT;
                            }

                            autoCompleteList.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            autoCompleteList.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        autoCompleteList.setVisibility(View.GONE);
                    }
                }

            }
        });

        //Remove the auto complete list on edit box leave
        editTextCat.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(!hasFocus)
                {
                    autoCompleteList.setVisibility(View.GONE);
                }
            }
        });
    }
}
