package com.example.memorycompanion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Filter implements Serializable
{

    private Filter(){}
    Filter(String t, long[] dRange, int[] correctPRange, List<String> iCats,
           List<String> eCats, List<String> iKeywords, List<String> eKeywords,
           boolean includeAllOrAny, boolean excludeAllOrAny)
    {
        title = t;
        dateRange = dRange;
        correctPercentageRange = correctPRange;
        includeCategories = iCats;
        isIncludeAllOrAny = includeAllOrAny;
        isExcludeAllOrAny = excludeAllOrAny;
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
    boolean isIncludeAllOrAny;
    List<String> excludeCategories;
    boolean isExcludeAllOrAny;

    List<String> questionIncludeKeywords;
    List<String> questionExcludeKeywords;

    List<Integer> questionIndexesList;
    //Clear this before storing
    List<Integer> questionReindex;


}

