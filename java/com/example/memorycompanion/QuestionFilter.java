package com.example.memorycompanion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class QuestionFilter implements Serializable
{

    private QuestionFilter(){}
    QuestionFilter(String t, long[] dRange, int[] correctPRange, List<String> iCats,
                   List<String> eCats, List<String> iKeywords, List<String> eKeywords)
    {
        title = t;
        dateRange = dRange;
        correctPercentageRange = correctPRange;
        includeCategories = iCats;
        excludeCategories = eCats;
        questionIncludeKeywords = iKeywords;
        questionExcludeKeywords = eKeywords;
        correctStreak = 0;
        questionIndexesList = new ArrayList<>();
        questionReindex = new ArrayList<>();
    }

    String title;
    
    long[] dateRange;
    int[] correctPercentageRange;

    int correctStreak;

    List<String> includeCategories;
    List<String> excludeCategories;
    List<String> questionIncludeKeywords;
    List<String> questionExcludeKeywords;

    List<Integer> questionIndexesList;
    //Clear this before storing
    List<Integer> questionReindex;


}

