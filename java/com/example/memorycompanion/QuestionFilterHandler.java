package com.example.memorycompanion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionFilterHandler
{

    private QuestionFilterHandler(){}

    boolean createNewFilter(String title, long[] dateRange, int[] correctPRange, String inclCats,
                            String exclCats, String inclKeywords, String  exclKeywords)
    {
        boolean validFilter = false;
        //Get the title
        List<String> obtainedList = stringValidator(title, true);
        if(obtainedList.size() > 0) { title = obtainedList.get(0); }        //Get the title
        List<String> includeCats;
        if(inclCats.length() > 0)
        {
            includeCats = Arrays.asList(inclCats.split(","));
        }
        List<String> excludeCats;
        if(inclCats.length() > 0)
        {
            excludeCats = Arrays.asList(inclCats.split(","));
        }

        List<String> includeKeywords = stringValidator(inclKeywords,false);
        List<String> excludeKeywords = stringValidator(exclKeywords,false);

        for(String s : includeKeywords)
        {
            System.out.println(s);
        }

        return  validFilter;
    }

    //Used to validate / organize text in filter specifications
    private ArrayList<String> stringValidator(String str, boolean isTitle)
    {
        ArrayList<String> returnList = new ArrayList<>();
        if(str.length() > 0)
        {
            //If it is a title this ensures it capitolizes all words
            //If not, then the string must be filter speicifications
            if(isTitle)
            {
                String[] tempArr = str.split(" ");
                StringBuilder newTitle = new StringBuilder();
                for(int i = 0; i < tempArr.length; i++)
                {
                    tempArr[i] = tempArr[i].substring(0,1).toUpperCase() + tempArr[i].substring(1);
                    newTitle.append(tempArr[i]);
                }
                returnList.add(newTitle.toString());
            }
            else
            {
                for(int i = 0; i < str.length(); i++)
                {
                    //If a quote is found it searches for the closing quote and the text within will
                    //be taken as literal
                    if(str.charAt(i) == '"')
                    {
                        boolean validQuote = false;
                        String quotedText;
                        int forwardIndex = i+2;
                        for(; forwardIndex < str.length(); forwardIndex++)
                        {
                            if(str.charAt(forwardIndex) == '"') { validQuote = true; break; }
                        }
                        if(validQuote)
                        {
                            //Set the new string to quoted text
                            //set the i index to just after the end quote
                            returnList.add(str.substring(i+1, forwardIndex).toLowerCase());
                            i = forwardIndex + 1;
                        }
                    }
                    else if(str.charAt(i) != ' ')
                    {
                        int forwardIndex = i+1;
                        for(; forwardIndex < str.length(); forwardIndex++)
                        {
                            if(str.charAt(forwardIndex) == ' ') { break; }
                        }
                        //Set the new string to quoted text
                        //set the i index to just after the end quote
                        returnList.add(str.substring(i, forwardIndex).toLowerCase());
                        i = forwardIndex;
                    }
                }
            }

        }
        return returnList;
    }
    public static QuestionFilterHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new QuestionFilterHandler();

        return single_instance;
    }
    private static QuestionFilterHandler single_instance = null;

    List<String> filters = new ArrayList<>();

}
