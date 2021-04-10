package com.example.memorycompanion;
import java.util.List;

class QuestionFilter
{
    QuestionFilter(){}

    long minDate;
    long maxDate;

    List<String> categories;

    int minCorrectAnswers;
    int maxCorrectAnswers;
    int minIncorrectAnswers;
    int maxIncorrectAnswers;

    int AnswerAccuracy;

    int minStreak;
    int maxStreak;

    List<String> questionTags;
    List<String> answerTags;

}

