package com.example.memorycompanion;
import java.util.List;

class QuestionFilter
{

    QuestionFilter(){}
    QuestionFilter(String t, long[] dRange, int[] correctPRange, int streak, List<String> iCats,
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

    List<Integer> questionIndexes;


}

