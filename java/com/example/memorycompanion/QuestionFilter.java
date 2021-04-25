package com.example.memorycompanion;
import java.util.List;

class QuestionFilter
{

    private QuestionFilter(){}
    QuestionFilter(String t, long[] dRange, int[] correctPRange, List<String> iCats,
                   List<String> eCats, List<String> iKeywords, List<String> eKeywords)
    {


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

